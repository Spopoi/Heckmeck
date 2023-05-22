package TCP.Server;

import Heckmeck.Game;
import TCP.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer implements Runnable{
    public ServerSocket ss;
    public List<SocketHandler> sockets = new ArrayList<SocketHandler>();
    private boolean hostClosedRoom = false;
    private Thread t1;
    private int numOfPlayers;
    public Game game;

    private TCPIOHandler io;
    public GameServer(){
        try {
            ss = new ServerSocket(51734);
            io = new TCPIOHandler(this);
            this.numOfPlayers = 0;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
//TODO da cambiare probabilmente.. sarebbe bello se il numero venisse calcolato in base ai player nella stanza
    public void setNumberOfPlayers(int numOfPlayers){
        this.numOfPlayers = numOfPlayers;
    }

    public void run() {
        try {
            acceptConnections();
            initClients();

            game = new Game(io);
            while(!areClientsReady()){

            }
            game.init();
            game.play();


        } catch (IOException e) {
        System.out.println("Error in acceptConnections()");
        }
    }

    public void acceptConnections() throws IOException {
        System.out.println("Waiting for client");
        int playerID = 0;
        while(!isRoomClosed()){
            Socket clientSocket;
            System.out.println("Waiting for connections: ");
            clientSocket = ss.accept();
            System.out.println("Accepted incoming connection #: "+ playerID);

            if (clientSocket.isConnected()) {
                SocketHandler socketHandler = new SocketHandler(clientSocket, playerID);
                this.sockets.add(socketHandler);
                playerID++;
            }
            if (playerID == 8 || playerID == numOfPlayers) {
                closeRoom();
            }
        }
    }




    public boolean isRoomClosed(){
        return hostClosedRoom;
    }

    public void closeRoom(){
        hostClosedRoom = true;
    }

    public int getNumOfPlayers(){
        return numOfPlayers;
    }

    public void initClients(){
        sockets.stream().forEach(e-> new Thread(e).start());
    }

    private SocketHandler getClientById(int id){
        return sockets.get(id);
    }


    public void close(){
        try {
            ss.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) {
        GameServer gameServer = new GameServer();
        Thread t1 = new Thread(gameServer);
        t1.start();
    }

    private boolean areClientsReady(){
        return sockets.stream().allMatch(e-> e.isInit());
    }

    public void openRoom() {
        hostClosedRoom = false;
    }
}
