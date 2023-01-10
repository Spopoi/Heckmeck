package Heckmeck;

import CLI.CliInputHandler;
import CLI.CliOutputHandler;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


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

    private int playerID = -1;
    void waitOneSec(){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Client(){
        //Thread clientThread = new Thread(this);
        //clientThread.start();

    }
    public static void main(String args[]){
        Client cli = new Client();
        cli.startConnection("127.0.0.1", 51734);
        CliInputHandler input = new CliInputHandler();
        CliOutputHandler output = new CliOutputHandler();
        output.showWelcomeMessage();
        //IOHandler io = new IOHandler(input, output);

        //run();
    }

    public void startConnection(String ip, int port) {
        try {
            hostIP = ip;
            hostPortNumber = port;
            clientSocket = new Socket(hostIP, hostPortNumber);

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            connected = true;
            //sendMessage("hello server");

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
            in.readLine();

        } catch (IOException e) {
            System.out.println("Error client reading line");
        }*/
        return resp;
    }

    public String readRxBuffer(){
        String resp = null;
        try {
            resp = in.readLine();

        } catch (IOException e) {
            System.out.println("Error client reading line");

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



    public void sendAck(){
        Message ack = new Message();
        ack.setOperation(Message.Action.ACK);
        ack.setPlayerID(playerID);
        sendMessage(gson.toJson(ack));
    }

    public void resetMessage() {
        message.text = null;
        message.operation = null;

    }

    public boolean isYourTurn(Message msg){
        return msg.playerID == playerID;
    }

    @Override
    public void run() {
        System.out.println("Client started");
        CliOutputHandler cliOut = new CliOutputHandler();
        CliInputHandler cliIn = new CliInputHandler();
        Random rand = new Random();


        Message message;
        int i = 0;
        while (true){
            if(connected){
                message = readIncomingMessage();
                Message msg;
                String[] choices = {"1", "2", "3", "4", "5", "w", "y"};


                switch (message.operation) {
                    case INIT:
                        System.out.println("ID: " + playerID + " INIT, message was: " + message.playerID);
                        playerID = message.playerID;
                        sendAck();
                        break;

                    case GET_PLAYER_NAME:
                        msg = new Message();
                        if(isYourTurn(message)) System.out.println("ID: " + playerID + " GET_PLAYER_NAME, message was: " + message.text);

                        msg.setOperation(Message.Action.RESPONSE);
                        msg.setPlayerID(playerID);
                        //msg.setText("Player"+ rand.nextInt(1000));
                        msg.setText("Player"+ playerID);
                        if(isYourTurn(message)) sendMessage(gson.toJson(msg));
                        else sendAck();
                        break;


                    case GET_INPUT:
                        msg = new Message();
                        if(isYourTurn(message)) System.out.println("ID: " + playerID + " GET_INPUT, message was: " + message.text);

                        msg.setOperation(Message.Action.RESPONSE);
                        msg.setText(choices[i%choices.length]);
                        msg.setPlayerID(playerID);
                        i++;


                        if(isYourTurn(message)) sendMessage(gson.toJson(msg));
                        else sendAck();

                        break;

                    case UPDATE_TILES:
                        this.message = message;
                        if(isYourTurn(message)) System.out.println("ID: " + playerID + " UPDATE_TILES, message was: " + message);
                        if (isYourTurn(message)){
                            System.out.println("Printing tiles of player n. " + playerID);
                            cliOut.showTiles(message.boardTiles);

                        }
                        sendAck();
                        break;

                    case UPDATE_PLAYER:
                        this.message = message;
                        if(isYourTurn(message)){
                            cliOut.showPlayerData(message.actualPlayer, message.dice, message.players);
                            cliOut.showDice(message.dice);
                        }
                        sendAck();
                        break;

                    case ERROR:
                        this.message = message;
                        if(isYourTurn(message)) System.out.println("ID: " + playerID + " ERROR, message was: " + message.operation);
                        System.out.println(message.text);
                        sendAck();
                        break;

                    case INFO:
                        this.message = message;

                        text = message.text;
                        if(isYourTurn(message)) System.out.println("ID: " + playerID + " INFO, message was: " + text);
                        sendAck();
                        break;


                    default:
                        break;
                         //TODO mettere un default
                }

            }

        }

    }
}
