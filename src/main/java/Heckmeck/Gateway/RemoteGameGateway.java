package Heckmeck.Gateway;

import Heckmeck.GameAction;
import Heckmeck.GameResult;
import Heckmeck.GameState;
import Heckmeck.Gateway.Protocol.Protocol.*;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Gateway implementation that communicates with a remote game server over TCP.
 * Handles serialization/deserialization of GameAction and GameState.
 * 
 * Architecture:
 * - Client sends ActionRequest to server
 * - Server processes action via GameEngine
 * - Server broadcasts StateUpdate to all clients
 * - All clients update their local state copy
 * 
 * Thread-safe: Uses blocking queues for async message handling.
 */
public class RemoteGameGateway implements GameGateway {
    
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final int myPlayerIndex;
    private final int timeoutSeconds;
    
    private GameState currentState;
    private volatile boolean active = true;
    
    // Queue for incoming state updates
    private final BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();
    
    // Background thread for receiving messages
    private final Thread receiverThread;
    
    /**
     * Connects to a remote game server.
     * 
     * @param host Server host/IP
     * @param port Server port
     * @param playerName Name for this player
     * @throws IOException if connection fails
     */
    public RemoteGameGateway(String host, int port, String playerName) throws IOException {
        this(host, port, playerName, 30);
    }
    
    /**
     * Connects to a remote game server with custom timeout.
     * 
     * @param host Server host/IP
     * @param port Server port
     * @param playerName Name for this player
     * @param timeoutSeconds Timeout in seconds for server responses
     * @throws IOException if connection fails
     */
    public RemoteGameGateway(String host, int port, String playerName, int timeoutSeconds) throws IOException {
        this.timeoutSeconds = timeoutSeconds;
        this.socket = new Socket(host, port);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        
        try {
            // Send connection request
            sendMessage(new ConnectRequest(playerName));
            
            // Wait for connection response
            Message response = receiveMessage();
            if (!(response instanceof ConnectResponse conn)) {
                throw new IOException("Expected ConnectResponse, got: " + response);
            }
            
            if (!conn.success()) {
                throw new IOException("Connection refused: " + conn.message());
            }
            
            this.myPlayerIndex = conn.assignedPlayerIndex();
            
            // Wait for initial state
            Message stateMsg = receiveMessage();
            if (!(stateMsg instanceof StateUpdate update)) {
                throw new IOException("Expected initial StateUpdate, got: " + stateMsg);
            }
            
            this.currentState = update.state();
        } catch (ClassNotFoundException e) {
            throw new IOException("Failed to deserialize server response", e);
        }
        
        // Start background receiver thread
        receiverThread = new Thread(this::receiveLoop, "RemoteGateway-Receiver");
        receiverThread.setDaemon(true);
        receiverThread.start();
    }
    
    @Override
    public GameState getState() {
        return currentState;
    }
    
    @Override
    public GameResult applyAction(GameAction action) {
        if (!active) {
            return new GameResult(currentState, List.of("Gateway is closed"));
        }
        
        try {
            // Send action request to server
            sendMessage(new ActionRequest(action, myPlayerIndex));
            
            // Wait for response (either StateUpdate or ErrorResponse)
            Message response = waitForResponse();
            
            return switch (response) {
                case StateUpdate update -> {
                    currentState = update.state();
                    yield new GameResult(currentState);
                }
                case ErrorResponse error -> new GameResult(currentState, error.errors());
                case DisconnectNotice notice -> {
                    active = false;
                    yield new GameResult(currentState, List.of("Player disconnected: " + notice.reason()));
                }
                default -> new GameResult(currentState, List.of("Unexpected response: " + response.getType()));
            };
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new GameResult(currentState, List.of("Action interrupted"));
        } catch (Exception e) {
            return new GameResult(currentState, List.of("Network error: " + e.getMessage()));
        }
    }
    
    @Override
    public boolean isActive() {
        return active && !socket.isClosed();
    }
    
    @Override
    public void close() {
        active = false;
        try {
            sendMessage(new DisconnectNotice(myPlayerIndex, "Client closing"));
        } catch (Exception ignored) {
            // Best effort
        }
        
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        receiverThread.interrupt();
    }
    
    /**
     * Background loop for receiving messages from server.
     */
    private void receiveLoop() {
        while (active && !Thread.currentThread().isInterrupted()) {
            try {
                Message msg = receiveMessage();
                messageQueue.offer(msg);
                
                // Handle state updates immediately
                if (msg instanceof StateUpdate update) {
                    currentState = update.state();
                }
                
                // Handle disconnection
                if (msg instanceof DisconnectNotice) {
                    active = false;
                    break;
                }
                
            } catch (EOFException e) {
                // Server closed connection
                active = false;
                break;
            } catch (IOException | ClassNotFoundException e) {
                if (active) {
                    System.err.println("Error receiving message: " + e.getMessage());
                }
                break;
            }
        }
    }
    
    /**
     * Waits for a response message from the queue.
     */
    private Message waitForResponse() throws InterruptedException {
        Message msg = messageQueue.poll(timeoutSeconds, TimeUnit.SECONDS);
        if (msg == null) {
            throw new RuntimeException("Timeout waiting for server response");
        }
        return msg;
    }
    
    /**
     * Sends a message to the server.
     */
    private synchronized void sendMessage(Message msg) throws IOException {
        out.writeObject(msg);
        out.flush();
    }
    
    /**
     * Receives a message from the server (blocking).
     */
    private Message receiveMessage() throws IOException, ClassNotFoundException {
        Object obj = in.readObject();
        if (!(obj instanceof Message)) {
            throw new IOException("Received non-Message object: " + obj.getClass());
        }
        return (Message) obj;
    }
    
    /**
     * Gets the player index assigned to this client.
     */
    public int getMyPlayerIndex() {
        return myPlayerIndex;
    }
}
