package Heckmeck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameServer implements Runnable{
    public ServerSocket ss;
    //private PrintWriter out;
    //private BufferedReader in;

    public List<ClientHandler> clients = new ArrayList<ClientHandler>();

    public ClientHandler currentClientPlayer;
    private Socket clientSocket;
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
            int currentPlayerID = 0;

            //openRoom();
            while(!isRoomClosed()){

                System.out.println("Waiting for connections: ");
                clientSocket = ss.accept();

                ClientHandler clientHandler = new ClientHandler(clientSocket, playerID);

                System.out.println("Accepted incoming connection #: "+ playerID);
                this.clients.add(clientHandler);

                playerID++;
                if (playerID == 7 || playerID == numOfPlayers ) {
                    closeRoom();
                }




                //game = new Game(output, input);
                //game.init();
                //game.play();

            }


            clients.stream().forEach(e-> new Thread(e).start());
            this.currentClientPlayer = clients.get(0);
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

    private ClientHandler getClientById(int id){
        return clients.get(id);
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
