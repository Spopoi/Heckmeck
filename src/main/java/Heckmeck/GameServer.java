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
    private Socket clientSocket;
    private boolean hostClosedRoom = false;
    private Thread t1;


    public GameServer(){
        try {
            ss = new ServerSocket(51734);
            //startThread(this);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void run() {
        try {
            //ss = new ServerSocket(51734);
            System.out.println("Waiting for client");
            int numPlayers = 1;

            while(!isRoomClosed()){
                clientSocket = ss.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, numPlayers);
                clients.add(clientHandler);
                numPlayers++;
                if (numPlayers == 4) {
                    closeRoom();
                }

            }

            clients.stream().forEach(e-> new Thread(e).start());



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


}
