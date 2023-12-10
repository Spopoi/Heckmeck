package TCP;

import CLI.CliIOHandler;
import CLI.HeckmeckCLI;
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
    IOHandler io;
    private boolean botMode = false;
    private int playerID;

    public Client(boolean botMode, IOHandler io){
        this.botMode = botMode;
        this.io = io;
        //cliIo = new CliIOHandler();
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
            startConnection(io.askIPToConnect(), 51734);
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
            if(io.wantToPlayAgain()) HeckmeckCLI.startMenu();
            else System.exit(0);
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
        commandInterpreter(botMode);
    }

    public static void main(String args[]){
        CliIOHandler cliIo = new CliIOHandler();
        Client cli = new Client(false, cliIo);
        cli.startConnection("127.0.0.1", 51734);

        cliIo.showWelcomeMessage();
        cliIo.printMessage("Waiting for your turn to chose your name:");


        cli.commandInterpreter(false);
    }

    void commandInterpreter(boolean botMode){

        int i = 0;
        while (true){
            if(connected){
                rxMessage = readIncomingMessage();

                if(rxMessage != null){
                    switch (rxMessage.operation) {
                        case INIT:
                            playerID = rxMessage.playerID;
                            sendAck();
                            break;

                        case GET_PLAYER_NAME:
                            io.printMessage(rxMessage.text);
                            io.printMessage("Select your name:");
                            String playerName = perform_get_player_name(botMode);
                            io.printMessage("You chose " + playerName + ", wait for other players!");
                            break;

                        case ASK_CONFIRM:
                            io.printMessage(rxMessage.text);
                            perform_ask_confirm();
                            break;

                        case GET_INPUT:
                            io.printMessage(rxMessage.text);
                            if(!isYourTurn(rxMessage)) {
                                sendAck();
                                break;
                            }
                            perform_get_input();
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

    private void perform_get_input() {
        String choice;
        Message txMessage;
        txMessage = new Message();
        txMessage.setOperation(Message.Action.RESPONSE);
        choice = io.getInputString();
        txMessage.setText(choice);
        txMessage.setPlayerID(playerID);
        sendMessage(gson.toJson(txMessage));
    }

    private void perform_ask_confirm() {
        Message txMessage;
        String choice;
        txMessage = new Message();
        txMessage.setOperation(Message.Action.RESPONSE);
        promptEnterKey();
        choice = "nothing";
        txMessage.setText(choice);
        txMessage.setPlayerID(playerID);
        sendMessage(gson.toJson(txMessage));
    }

    private String perform_get_player_name(boolean botMode) {
        Message txMessage;
        txMessage = new Message();
        txMessage.setOperation(Message.Action.RESPONSE);
        txMessage.setPlayerID(playerID);
        if(botMode) txMessage.setText("Player"+ playerID);
        else txMessage.setText(io.getInputString());
        sendMessage(gson.toJson(txMessage));
        return txMessage.text;
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
