package it.units.heckmeck;

import Heckmeck.GameAction;
import Heckmeck.GameResult;
import Heckmeck.GameState;
import Heckmeck.Gateway.RemoteGameGateway;
import Heckmeck.Gateway.Protocol.Protocol.*;
import Heckmeck.Components.Player;
import Heckmeck.Components.Dice;
import Heckmeck.Components.BoardTiles;
import Heckmeck.Components.Die;
import org.junit.jupiter.api.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for RemoteGameGateway that verify expected behavior, not implementation.
 * Uses a mock server to simulate server responses.
 */
public class TestRemoteGameGateway {
    
    private static final int TEST_PORT = 51735; // Different from default to avoid conflicts
    private MockGameServer mockServer;
    private RemoteGameGateway gateway;
    
    @BeforeEach
    public void setUp() throws Exception {
        mockServer = new MockGameServer(TEST_PORT);
        mockServer.start();
        // Give server time to start
        Thread.sleep(100);
    }
    
    @AfterEach
    public void tearDown() {
        if (gateway != null && gateway.isActive()) {
            gateway.close();
        }
        if (mockServer != null) {
            mockServer.stop();
        }
    }
    
    @Test
    @DisplayName("Should connect successfully and receive initial state")
    public void testSuccessfulConnection() throws Exception {
        // Configure mock server to accept connection
        Player[] players = TestUtils.createTestPlayers();
        GameState initialState = GameState.initial(players, Dice.init(), BoardTiles.init());
        mockServer.setInitialState(initialState);
        mockServer.setPlayerIndex(0);
        
        // Connect
        gateway = new RemoteGameGateway("localhost", TEST_PORT, "TestPlayer");
        
        // Verify connection successful
        assertTrue(gateway.isActive(), "Gateway should be active after connection");
        assertNotNull(gateway.getState(), "Should have received initial state");
        assertEquals(0, gateway.getMyPlayerIndex(), "Should have received player index 0");
    }
    
    @Test
    @DisplayName("Should fail connection when server refuses")
    public void testConnectionRefused() {
        // Configure mock server to refuse connection
        mockServer.setRefuseConnection(true);
        
        // Attempt connection should throw IOException
        IOException exception = assertThrows(IOException.class, () -> {
            gateway = new RemoteGameGateway("localhost", TEST_PORT, "TestPlayer");
        });
        
        assertTrue(exception.getMessage().contains("refused"), 
            "Exception should indicate connection was refused");
    }
    
    @Test
    @DisplayName("Should send action and receive state update")
    public void testApplyActionSuccess() throws Exception {
        // Setup
        Player[] players = TestUtils.createTestPlayers();
        GameState initialState = GameState.initial(players, Dice.init(), BoardTiles.init());
        mockServer.setInitialState(initialState);
        mockServer.setPlayerIndex(0);
        
        gateway = new RemoteGameGateway("localhost", TEST_PORT, "TestPlayer");
        
        // Create new state for response
        GameState newState = initialState.withPhase(GameState.Phase.ROLL_OR_ACTION);
        mockServer.setNextActionResponse(new StateUpdate(newState));
        
        // Apply action
        GameResult result = gateway.applyAction(new GameAction.StartTurn());
        
        // Verify
        assertTrue(result.isSuccess(), "Action should succeed");
        assertEquals(GameState.Phase.ROLL_OR_ACTION, result.newState().getPhase(), 
            "Should receive updated state");
        assertEquals(GameState.Phase.ROLL_OR_ACTION, gateway.getState().getPhase(), 
            "Gateway state should be updated");
    }
    
    @Test
    @DisplayName("Should receive error response for invalid action")
    public void testApplyActionError() throws Exception {
        // Setup
        Player[] players = TestUtils.createTestPlayers();
        GameState initialState = GameState.initial(players, Dice.init(), BoardTiles.init());
        mockServer.setInitialState(initialState);
        mockServer.setPlayerIndex(0);
        
        gateway = new RemoteGameGateway("localhost", TEST_PORT, "TestPlayer");
        
        // Configure server to return error
        List<String> errors = List.of("Invalid action: game not started");
        mockServer.setNextActionResponse(new ErrorResponse(errors));
        
        // Apply action
        GameResult result = gateway.applyAction(new GameAction.RollDice());
        
        // Verify
        assertFalse(result.isSuccess(), "Action should fail");
        assertEquals(1, result.errors().size(), "Should have one error");
        assertEquals("Invalid action: game not started", result.errors().get(0));
    }
    
    @Test
    @DisplayName("Should handle server disconnect gracefully")
    public void testServerDisconnect() throws Exception {
        // Setup
        Player[] players = TestUtils.createTestPlayers();
        GameState initialState = GameState.initial(players, Dice.init(), BoardTiles.init());
        mockServer.setInitialState(initialState);
        mockServer.setPlayerIndex(0);
        
        gateway = new RemoteGameGateway("localhost", TEST_PORT, "TestPlayer");
        assertTrue(gateway.isActive());
        
        // Server disconnects
        mockServer.disconnectClients();
        
        // Give time for disconnect to propagate
        Thread.sleep(200);
        
        // Gateway should detect disconnection
        assertFalse(gateway.isActive(), "Gateway should detect server disconnect");
    }
    
    @Test
    @DisplayName("Should remain inactive after close")
    public void testCloseGateway() throws Exception {
        // Setup
        Player[] players = TestUtils.createTestPlayers();
        GameState initialState = GameState.initial(players, Dice.init(), BoardTiles.init());
        mockServer.setInitialState(initialState);
        mockServer.setPlayerIndex(0);
        
        gateway = new RemoteGameGateway("localhost", TEST_PORT, "TestPlayer");
        assertTrue(gateway.isActive());
        
        // Close
        gateway.close();
        
        // Verify
        assertFalse(gateway.isActive(), "Gateway should be inactive after close");
        
        // Attempting actions should fail
        GameResult result = gateway.applyAction(new GameAction.StartTurn());
        assertFalse(result.isSuccess());
        assertTrue(result.errors().get(0).contains("closed"));
    }
    
    /**
     * Mock server for testing RemoteGameGateway.
     * Simulates server-side protocol handling.
     */
    private static class MockGameServer {
        private final ServerSocket serverSocket;
        private volatile boolean running = false;
        private Thread acceptThread;
        private Socket clientSocket;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        
        private GameState initialState;
        private int playerIndex = 0;
        private boolean refuseConnection = false;
        private Message nextActionResponse;
        private boolean delayResponse = false;
        private final CountDownLatch clientConnected = new CountDownLatch(1);
        
        public MockGameServer(int port) throws IOException {
            this.serverSocket = new ServerSocket(port);
            this.serverSocket.setSoTimeout(5000); // 5 second timeout for accept
        }
        
        public void start() {
            running = true;
            acceptThread = new Thread(this::acceptLoop);
            acceptThread.start();
        }
        
        public void stop() {
            running = false;
            try {
                if (clientSocket != null) clientSocket.close();
                serverSocket.close();
            } catch (IOException ignored) {}
            if (acceptThread != null) acceptThread.interrupt();
        }
        
        public void setInitialState(GameState state) {
            this.initialState = state;
        }
        
        public void setPlayerIndex(int index) {
            this.playerIndex = index;
        }
        
        public void setRefuseConnection(boolean refuse) {
            this.refuseConnection = refuse;
        }
        
        public void setNextActionResponse(Message response) {
            this.nextActionResponse = response;
        }
        
        public void setDelayResponse(boolean delay) {
            this.delayResponse = delay;
        }
        
        public void disconnectClients() {
            try {
                if (clientSocket != null) clientSocket.close();
            } catch (IOException ignored) {}
        }
        
        private void acceptLoop() {
            try {
                clientSocket = serverSocket.accept();
                clientSocket.setSoTimeout(2000); // 2 second timeout for read operations
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                in = new ObjectInputStream(clientSocket.getInputStream());
                
                handleClient();
                clientConnected.countDown();
                
            } catch (IOException e) {
                if (running && !(e instanceof java.net.SocketTimeoutException)) {
                    System.err.println("Mock server error: " + e.getMessage());
                }
            }
        }
        
        private void handleClient() throws IOException {
            try {
                // Read ConnectRequest
                Message msg = (Message) in.readObject();
                if (!(msg instanceof ConnectRequest)) {
                    return;
                }
                
                // Send ConnectResponse
                if (refuseConnection) {
                    out.writeObject(new ConnectResponse(false, -1, "Connection refused"));
                    out.flush();
                    return;
                }
                
                out.writeObject(new ConnectResponse(true, playerIndex, "Connected"));
                out.flush();
                
                // Send initial StateUpdate
                if (initialState != null) {
                    out.writeObject(new StateUpdate(initialState));
                    out.flush();
                }
                
                // Handle action requests
                while (running && !clientSocket.isClosed()) {
                    try {
                        Message actionMsg = (Message) in.readObject();
                        
                        if (actionMsg instanceof ActionRequest) {
                            if (!delayResponse && nextActionResponse != null) {
                                out.writeObject(nextActionResponse);
                                out.flush();
                            }
                            // If delayResponse is true, don't send anything (simulate timeout)
                        } else if (actionMsg instanceof DisconnectNotice) {
                            // Client is closing, break the loop
                            break;
                        }
                        
                    } catch (EOFException e) {
                        break;
                    } catch (java.net.SocketTimeoutException e) {
                        // Timeout on read, check if still running
                        continue;
                    }
                }
                
            } catch (ClassNotFoundException e) {
                throw new IOException("Failed to deserialize message", e);
            }
        }
    }
}
