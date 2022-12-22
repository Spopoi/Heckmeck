package Heckmeck;

import CLI.CliInputHandler;
import CLI.CliOutputHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable{

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String hostIP;
    private int hostPortNumber;


    public Client(){
        //Thread clientThread = new Thread(this);
        //clientThread.start();

    }

    public void startConnection(String ip, int port) {
        try {
            hostIP = ip;
            hostPortNumber = port;
            clientSocket = new Socket(hostIP, hostPortNumber);
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("Connection established");
        } catch (IOException e) {
            System.out.println("Error in starting client connection()");
        }

    }

public String sendMessage(String msg) {
        out.println(msg);
        String resp = "";
        try {
            resp = in.readLine();
            out.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resp;
    }

    public String readRxBuffer(){
        String resp = null;
        try {
            resp = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resp;
    }

    public void stopConnection() {
        out.close();
        try {
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void main(String[] args) throws IOException {
        Client cli = new Client();
        cli.startConnection("127.0.0.1", 51734);
        CliInputHandler input = new CliInputHandler();
        CliOutputHandler output = new CliOutputHandler();
        output.showWelcomeMessage();
        IOHandler io = new IOHandler(input, output);
        if(readRxBuffer().equals("GET PLAYER_NAME")){
            String playerName = input.choosePlayerName();
            cli.sendMessage(playerName);
        }

    }

    @Override
    public void run() {
        //startConnection(hostIP, hostPortNumber);
    }
}
