package TCP.Server;

import Heckmeck.Launcher;
import Utils.TCP.ConnectionHandler;
import Heckmeck.Game;
import TCP.TCPIOHandler;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class GameServer extends Launcher implements Runnable {
    public ServerSocket ss;
    public List<ClientHandler> clients = new ArrayList<>();
    private boolean hostClosedRoom = false;
    private final int numOfPlayers;
    public Game game;

    public GameServer(int numOfPlayers) {
        try {
            ss = new ServerSocket(51734); //TODO magic number.. facciamo un file di configurazione?
            this.numOfPlayers = numOfPlayers;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void run() {
        acceptConnections();
        TCPIOHandler io = new TCPIOHandler(clients);
        startGame(io);
        close();
    }

    public void acceptConnections(){
        System.out.println("Room open");
        int playerID = 0;
        while (!isRoomClosed()) {
            Socket clientSocket;
            try {
                clientSocket = ss.accept();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Accepted incoming connection #: " + playerID);
            if (clientSocket.isConnected()) {
                this.clients.add(ConnectionHandler.startClientHandler(playerID, clientSocket));
                playerID++;
            }
            if (playerID == 7 || playerID == numOfPlayers) { //TODO metetre anche magicNumber 7 dentro a delle rules?
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