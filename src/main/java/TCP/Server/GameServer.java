package TCP.Server;

import Heckmeck.Game;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer implements Runnable{
    public ServerSocket ss;
    //private PrintWriter out;
    //private BufferedReader in;
    public List<SocketHandler> sockets = new ArrayList<SocketHandler>();
    private boolean hostClosedRoom = false;
    private Thread t1;
    private int numOfPlayers;
    public Game game;
    public GameServer(){
        try {
            ss = new ServerSocket(51734);
            this.numOfPlayers = 0;
            //startThread(this);

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
            //ss = new ServerSocket(51734);
            System.out.println("Waiting for client");
            int playerID = 0;
            //openRoom();
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


            sockets.stream().forEach(e-> new Thread(e).start());
            sockets.stream().forEach(e-> e.initClient());
            //System.out.println(this.currentClientPlayer);

            //currentClientPlayer = getClientById(currentPlayerID);



        } catch (IOException e) {
        System.out.println("Error in acceptConnections()");
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

    public void initClients(){          // TODO mettere return type boolean
        sockets.stream().forEach(e -> e.initClient());
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

    private void startThread(GameServer gameServer){
        Thread t1 = new Thread(gameServer);
        t1.start();
    }


   /* public String readReceivedMessage(){
        return "";//this.message;
    }

    public void writeMessage(String message){
        return; //out.println(message);
    }*/

    public void stopThread(){
        try {
            t1.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        GameServer gameServer = new GameServer();
        Thread t1 = new Thread(gameServer);
        t1.start();

    }

    public void openRoom() {
        hostClosedRoom = false;
    }
}
