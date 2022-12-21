package Heckmeck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer implements Runnable{
    public ServerSocket ss;
    private PrintWriter out;
    private BufferedReader in;

    private String message;

    public GameServer(){
        try {
            ss = new ServerSocket(51734);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void run() {
        try {
            //ss = new ServerSocket(51734);
            System.out.println("Waiting for client");
            Socket clientSocket = ss.accept();
            System.out.println("Connection ok");
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.message =  in.readLine();
            out.println("hello client");
        } catch (IOException e) {
            System.out.println("Error in acceptConnections()");
        }
    }

    public String readReceivedMessage(){
        return this.message;
    }

    public void close(){
        try {
            ss.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        GameServer gameServer =new GameServer();
        Thread t1 =new Thread(gameServer);
        t1.start();
    }


}
