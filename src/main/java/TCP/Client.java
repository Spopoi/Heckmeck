package TCP;

import CLI.CliIOHandler;
import Heckmeck.IOHandler;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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

    public Message rxMessage;

    private boolean connected = false;

    CliIOHandler cliIo;

    private boolean botMode = false;



    private int playerID;

    public Client(boolean botMode){
        this.botMode = botMode;
        cliIo = new CliIOHandler();
    }


    public void startConnection(String ip, int port) {
        try {
            hostIP = ip;
            hostPortNumber = port;
            clientSocket = new Socket(hostIP, hostPortNumber);

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            connected = true;

        } catch (IOException e) {
            System.out.println("Error in starting client connection, check the IP address and try again");
            startConnection(cliIo.askIPToConnect(), 51734);
        }
    }

public String sendMessage(String msg) {
        out.println(msg);
        String resp = "";
        return resp;
    }

    public String readRxBuffer(){
        String resp = null;
        try {
            resp = in.readLine();

        } catch (IOException e) {
            System.out.println("Error in receiving data. Stopping client");
            System.exit(1);

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
        commandInterpreter(botMode, cliIo);
    }

    public static void main(String args[]){
        Client cli = new Client(false);
        cli.startConnection("127.0.0.1", 51734);
        CliIOHandler cliIo = new CliIOHandler();
        cliIo.showWelcomeMessage();
        cliIo.printMessage("Waiting for your turn to chose your name:");


        cli.commandInterpreter(false, cliIo);
    }

    void commandInterpreter(boolean botMode, IOHandler io){

        int i = 0;
        while (true){
            if(connected){
                rxMessage = readIncomingMessage();
                Message txMessage;
                String choice;
                if(rxMessage != null){
                    switch (rxMessage.operation) {
                        case INIT:
                            playerID = rxMessage.playerID;
                            sendAck();
                            break;

                        case GET_PLAYER_NAME:
                            txMessage = new Message();
                            io.printMessage(rxMessage.text);
                            txMessage.setOperation(Message.Action.RESPONSE);
                            txMessage.setPlayerID(playerID);
                            io.printMessage("Select your name:");
                            if(botMode) txMessage.setText("Player"+ playerID);
                            else txMessage.setText(io.getInputString());
                            cliIo.printMessage("You chose " + txMessage.text + ", wait for other players!");
                            sendMessage(gson.toJson(txMessage));
                            break;

                        case ASK_CONFIRM:
                            txMessage = new Message();
                            io.printMessage(rxMessage.text);
                            txMessage.setOperation(Message.Action.RESPONSE);
                            promptEnterKey();
                            choice = "nothing";
                            txMessage.setText(choice);
                            txMessage.setPlayerID(playerID);
                            sendMessage(gson.toJson(txMessage));
                            break;

                        case GET_INPUT:
                            if(!isYourTurn(rxMessage)) {
                                sendAck();
                                break;
                            }
                            txMessage = new Message();
                            io.printMessage(rxMessage.text);
                            txMessage.setOperation(Message.Action.RESPONSE);
                            choice = io.getInputString();
                            txMessage.setText(choice);
                            txMessage.setPlayerID(playerID);
                            sendMessage(gson.toJson(txMessage));

                            break;

                        case UPDATE_TILES:
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
                            sendAck();

                            break;

                        case INFO:
                            text = rxMessage.text;
                            io.printMessage(text);
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


    public static void promptEnterKey(){
        System.out.println("Press \"ENTER\" to continue...");
        try {
            int read = System.in.read(new byte[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
