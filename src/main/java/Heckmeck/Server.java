package Heckmeck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{
    public ServerSocket ss;
    private PrintWriter out;
    private BufferedReader in;

    private String message;

    public Server(){
        System.out.println("This is a game server demo");
        try{
            ss = new ServerSocket(51734);

        } catch(IOException e){
            System.out.print("Error on Server constructor");
        }
    }

    public void run() {
        try {

            System.out.println("Waiting for client");
            Socket clientSocket = ss.accept();
            System.out.println("Connection ok");
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            message =  in.readLine();
            out.println("hello client");
        } catch (IOException e) {
            System.out.println("Error in acceptConnections()");
        }
    }

    public String readReceivedMessage(){
        return this.message;
    }

    public static void main(String[] args) {
        Server server=new Server();
        server.run();
    }


}
