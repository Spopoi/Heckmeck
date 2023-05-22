package TCP;

import CLI.CliIOHandler;
import Heckmeck.IOHandler;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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

    public Message rxMessage;

    private boolean connected = false;



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


    public boolean isYourTurn(Message msg){
        return msg.playerID == playerID;
    }

    @Override
    public void run() {
        CliIOHandler cliIo = new CliIOHandler();

        commandInterpreter(false, cliIo);
    }

    public static void main(String args[]){
        Client cli = new Client();
        cli.startConnection("127.0.0.1", 51734);
        CliIOHandler cliIo = new CliIOHandler();
        cliIo.showWelcomeMessage();
        cliIo.printMessage("Waiting for connection:");


        cli.commandInterpreter(false, cliIo);
    }

    private void commandInterpreter(boolean botMode, IOHandler io){

        int i = 0;
        while (true){
            if(connected){
                rxMessage = readIncomingMessage();
                Message txMessage;
                String[] choices = {"1", "2", "3", "4", "5", "w", "y"};

                if(rxMessage != null){
                    switch (rxMessage.operation) {
                        case INIT:
                            playerID = rxMessage.playerID;
                            txMessage = new Message();
                            //if(isYourTurn(rxMessage)) System.out.println("ID: " + playerID + " GET_PLAYER_NAME, message was: " + rxMessage.text);
                            io.printMessage(rxMessage.text);
                            io.printMessage("Select your name:");
                            txMessage.setOperation(Message.Action.RESPONSE);
                            txMessage.setPlayerID(playerID);
                            //if(botMode) txMessage.setText("Player"+ playerID);
                            if(isYourTurn(rxMessage)) txMessage.setText(io.getInputString());
                            io.printMessage("Waiting for other players...");
                            sendMessage(gson.toJson(txMessage));
                            //else sendAck();
                            break;

                        case GET_PLAYER_NAME:
                            txMessage = new Message();
                            //if(isYourTurn(rxMessage)) System.out.println("ID: " + playerID + " GET_PLAYER_NAME, message was: " + rxMessage.text);
                            io.printMessage(rxMessage.text);
                            txMessage.setOperation(Message.Action.RESPONSE);
                            txMessage.setPlayerID(playerID);
                            //if(botMode) txMessage.setText("Player"+ playerID);
                            if(isYourTurn(rxMessage)) txMessage.setText(io.getInputString());

                            if(isYourTurn(rxMessage)) sendMessage(gson.toJson(txMessage));
                            else sendAck();
                            break;

                        case GET_INPUT:
                            txMessage = new Message();
                            io.printMessage(rxMessage.text);

                            txMessage.setOperation(Message.Action.RESPONSE);
                            //if(botMode) txMessage.setText(choices[i%choices.length]) ;
                            if(isYourTurn(rxMessage)) txMessage.setText(io.getInputString());
                            else io.printMessage("Please wait for your turn");
                            i++;
                            txMessage.setPlayerID(playerID);

                            if(isYourTurn(rxMessage)) sendMessage(gson.toJson(txMessage));
                            else sendAck();

                            break;

                        case UPDATE_TILES:
                            if(isYourTurn(rxMessage)) System.out.println("ID: " + playerID + " UPDATE_TILES, message was: " + rxMessage);

                            io.showBoardTiles(rxMessage.boardTiles);

                            sendAck();
                            break;

                        case UPDATE_PLAYER:

                            io.showPlayerData(rxMessage.actualPlayer, rxMessage.dice, rxMessage.players);
                            io.showRolledDice(rxMessage.dice);
                            sendAck();
                            break;

                        case ERROR:
                            if(isYourTurn(rxMessage)) System.out.println("ID: " + playerID + " ERROR, message was: " + rxMessage.operation);
                            System.out.println(rxMessage.text);
                            sendAck();
                            break;

                        case INFO:
                            text = rxMessage.text;
                            io.printMessage(text);
                            //if(isYourTurn(rxMessage)) System.out.println("ID: " + playerID + " INFO, message was: " + text);
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
}
