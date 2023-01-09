package Heckmeck;

import CLI.CliInputHandler;
import CLI.CliOutputHandler;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Client implements Runnable{

    Gson gson = new Gson();
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String hostIP;
    private int hostPortNumber;

    public Message message;

    public String text;

    private boolean connected = false;

    private int playerID;

    public Client(){
        //Thread clientThread = new Thread(this);
        //clientThread.start();

    }

    public void startConnection(String ip, int port) {
        try {
            hostIP = ip;
            hostPortNumber = port;
            clientSocket = new Socket(hostIP, hostPortNumber);

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            connected = true;
            sendMessage("hello server");

            //System.out.println("Connection established" + clientSocket.isConnected());
        } catch (IOException e) {
            System.out.println("Error in starting client connection()");
        }

    }

public String sendMessage(String msg) {
        out.println(msg);
        out.flush();
        String resp = "";
        /*try {
            resp = "";// in.readLine();

        } catch (IOException e) {
            System.out.println("Error client reading message");
        }*/
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

    public Message readIncomingMessage(){
        String serialized = readRxBuffer();

        return gson.fromJson(serialized , Message.class);
    }

    public Message getMessage(){
        return message;
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
        //IOHandler io = new IOHandler(input, output);
        if(readRxBuffer().equals("GET PLAYER_NAME")){
            String playerName = input.choosePlayerName(0);
            cli.sendMessage(playerName);
        }

    }

    public void resetMessage() {
        message.text = null;
        message.operation = null;

    }

    @Override
    public void run() {
        System.out.println("Client started");
        CliOutputHandler cliOut = new CliOutputHandler();

        Message message;
        while (true){
            if(connected){
                message = readIncomingMessage();

                switch (message.operation) {

                    case GET_INPUT:
                        this.message = message;
                        System.out.println("GET_INPUT, message was: " + message.text);

                        message.setOperation(Message.Action.RESPONSE);
                        message.setText("Player"+ playerID);
                        message.setPlayerID(playerID);

                        sendMessage(gson.toJson(message));

                        break;

                    case UPDATE_TILES:
                        this.message = message;
                        System.out.println("UPDATE, message was: " + message.text);
                        if (message.boardTiles!=null){
                            cliOut.showTiles(message.boardTiles);
                            message.boardTiles = null;
                        }
                        break;

                    case UPDATE_PLAYER:
                        this.message = message;
                        if(message.actualPlayer!=null && message.dice!=null){
                            cliOut.showPlayerData(message.actualPlayer, message.dice, message.players);
                            cliOut.showDice(message.dice);
                        }
                        break;

                    case ERROR:
                        this.message = message;
                        System.out.println("ERROR, message was: " + message.operation);
                        break;

                    case INFO:
                        this.message = message;

                        playerID = message.playerID;
                        text = message.text;
                        System.out.println("INFO, message was: " + playerID);
                        break;


                    default:
                        break;
                         //TODO mettere un default
                }

            }

        }

    }
}
