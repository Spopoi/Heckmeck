package TCP.Server;

import Utils.TCP.ConnectionHandler;
import Heckmeck.Game;
import TCP.TCPIOHandler;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class GameServer implements Runnable {
    public ServerSocket ss;
    public List<ClientHandler> clients = new ArrayList<>();
    private boolean hostClosedRoom = false;
    private Thread t1;
    private final int numOfPlayers;
    public Game game;

    public GameServer(int numOfPlayers) {
        try {
            ss = new ServerSocket(51734);
            this.numOfPlayers = numOfPlayers;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void run() {
        try {
            acceptConnections();

        } catch (IOException e) {
            System.out.println("Error in acceptConnections()");
        }

        TCPIOHandler io = new TCPIOHandler(clients);
        //io.showWelcomeMessage();

        game = new Game(io);
        game.init();
        game.play();
    }


    public void acceptConnections() throws IOException {
        System.out.println("Room open");
        int playerID = 0;
        while (!isRoomClosed()) {
            Socket clientSocket;
            clientSocket = ss.accept();
            System.out.println("Accepted incoming connection #: " + playerID);
            if (clientSocket.isConnected()) {
                this.clients.add(ConnectionHandler.startClientHandler(playerID, clientSocket));
                playerID++;
            }
            if (playerID == 7 || playerID == numOfPlayers) {
                closeRoom();
            }
        }
    }

    public boolean isRoomClosed() {
        return hostClosedRoom;
    }

    public void closeRoom() {
        hostClosedRoom = true;
    }

    public void close() {
        try {
            ss.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}