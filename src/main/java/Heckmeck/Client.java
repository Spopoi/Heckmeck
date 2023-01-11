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

    CliOutputHandler cliOut = new CliOutputHandler();
    CliInputHandler cliIn = new CliInputHandler();

    private int playerID;
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

        Random rand = new Random();
        commandInterpreter(true);
    }

    public static void main(String args[]){
        Client cli = new Client();
        cli.startConnection("127.0.0.1", 51734);
        CliInputHandler input = new CliInputHandler();
        CliOutputHandler output = new CliOutputHandler();
        output.showWelcomeMessage();

        //IOHandler io = new IOHandler(input, output);
        cli.commandInterpreter(false);
    }

    private void commandInterpreter(boolean botMode){
        Message rxMessage;
        int i = 0;
        while (true){
            if(connected){
                rxMessage = readIncomingMessage();
                Message txMessage;
                String[] choices = {"1", "2", "3", "4", "5", "w", "y"};


                switch (rxMessage.operation) {
                    case INIT:
                        playerID = rxMessage.playerID;
                        sendAck();
                        break;

                    case GET_PLAYER_NAME:
                        txMessage = new Message();
                        //if(isYourTurn(rxMessage)) System.out.println("ID: " + playerID + " GET_PLAYER_NAME, message was: " + rxMessage.text);
                        cliOut.printMessage(rxMessage.text);
                        txMessage.setOperation(Message.Action.RESPONSE);
                        txMessage.setPlayerID(playerID);
                        if(botMode) txMessage.setText("Player"+ playerID);
                        else if(isYourTurn(rxMessage)) txMessage.setText(cliIn.getInputString());

                        if(isYourTurn(rxMessage)) sendMessage(gson.toJson(txMessage));
                        else sendAck();
                        break;


                    case GET_INPUT:
                        txMessage = new Message();
                        //if(isYourTurn(rxMessage))
                        cliOut.printMessage(rxMessage.text);

                        txMessage.setOperation(Message.Action.RESPONSE);
                        if(botMode) txMessage.setText(choices[i%choices.length]) ;
                        else if(isYourTurn(rxMessage)) txMessage.setText(cliIn.getInputString());
                        i++;
                        txMessage.setPlayerID(playerID);

                        if(isYourTurn(rxMessage)) sendMessage(gson.toJson(txMessage));
                        else sendAck();

                        break;

                    case UPDATE_TILES:
                        if(isYourTurn(rxMessage)) System.out.println("ID: " + playerID + " UPDATE_TILES, message was: " + rxMessage);
                        if (isYourTurn(rxMessage)){
                        }
                        cliOut.showTiles(rxMessage.boardTiles);

                        sendAck();
                        break;

                    case UPDATE_PLAYER:
                        this.message = rxMessage;
                        if(isYourTurn(rxMessage)){

                        }
                        cliOut.showPlayerData(rxMessage.actualPlayer, rxMessage.dice, rxMessage.players);
                        cliOut.showDice(rxMessage.dice);
                        sendAck();
                        break;

                    case ERROR:
                        if(isYourTurn(rxMessage)) System.out.println("ID: " + playerID + " ERROR, message was: " + rxMessage.operation);
                        System.out.println(rxMessage.text);
                        sendAck();
                        break;

                    case INFO:
                        text = rxMessage.text;
                        cliOut.printMessage(text);
                        if(isYourTurn(rxMessage)) System.out.println("ID: " + playerID + " INFO, message was: " + text);
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
