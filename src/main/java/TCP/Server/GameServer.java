package TCP.Server;

import Heckmeck.Launcher;
import Heckmeck.HeckmeckRules;
import Utils.TCP.ConnectionHandler;
import TCP.TCPIOHandler;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class GameServer extends Launcher implements Runnable {
    public ServerSocket ss;
    public final List<ClientHandler> clients = new ArrayList<>();
    private boolean hostClosedRoom = false;
    private final int numOfPlayers;

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
            TCPIOHandler io = new TCPIOHandler(clients);
            startGame(io);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        close();
    }

    public void acceptConnections() throws IOException {
        int playerID = 0;
        while (!isRoomClosed()) {
            Socket clientSocket;
            clientSocket = ss.accept();
            if (clientSocket.isConnected()) {
                this.clients.add(ConnectionHandler.startClientHandler(playerID, clientSocket));
                playerID++;
            }
            if (playerID == HeckmeckRules.MAX_NUM_OF_PLAYERS || playerID == numOfPlayers) {
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