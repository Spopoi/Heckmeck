package TCP;

import CLI.HeckmeckCLI;
import Heckmeck.IOHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;


public class Client implements Runnable{

    private PrintWriter out;
    private BufferedReader in;
    public Message rxMessage;
    private boolean connected = false;
    IOHandler io;
    private boolean botMode = false;
    private int playerID;

    public Client(boolean botMode, IOHandler io, BufferedReader in, PrintWriter out){
        this.botMode = botMode;
        this.io = io;
        this.in = in;
        this.out = out;
        connected = true;
    }
    public String sendMessage(String line) {
        out.println(line);
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
        return Message.fromJSON(serialized);
    }

    public void sendAck(){
        Message ack = Message.generateMessage();
        ack.setOperation(Message.Action.ACK);
        ack.setPlayerID(playerID);
        sendMessage(ack.toJSON());
    }

    public boolean isYourTurn(Message msg){
        return msg.playerID == playerID;
    }

    @Override
    public void run() {
        commandInterpreter(botMode);
    }

    public static void main(String args[]){
        HeckmeckCLI.startMenu();
    }

    public void commandInterpreter(boolean botMode){
        int i = 0;
        while (true){
            try{
                if(connected) {
                    rxMessage = readIncomingMessage();
                    if (rxMessage != null) {
                        handleMessage(rxMessage);
                    }
                }
            }
            catch (NullPointerException e){
                System.out.println(e);
                //if(io.wantToPlayAgain()) HeckmeckCLI.startMenu();
                //else System.exit(0);
            }
        }
    }

    public void perform_get_input() {
        String choice;
        Message txMessage;
        txMessage = Message.generateMessage();
        txMessage.setOperation(Message.Action.RESPONSE);
        choice = io.getInputString();
        txMessage.setText(choice);
        txMessage.setPlayerID(playerID);
        sendMessage(txMessage.toJSON());
    }

    private void perform_ask_confirm() {
        Message txMessage;
        String choice;
        txMessage = Message.generateMessage();
        txMessage.setOperation(Message.Action.RESPONSE);
        //promptEnterKey();
        choice = "nothing";
        txMessage.setText(choice);
        txMessage.setPlayerID(playerID);
        sendMessage(txMessage.toJSON());
    }

    public String perform_get_player_name(boolean botMode) {
        Message txMessage;
        txMessage = Message.generateMessage();
        txMessage.setOperation(Message.Action.RESPONSE);
        txMessage.setPlayerID(playerID);
        if(botMode) txMessage.setText("Player"+ playerID);
        else txMessage.setText(io.getInputString());
        sendMessage(txMessage.toJSON());
        return txMessage.text;
    }

    public void handleMessage(Message rxMessage){

        switch (rxMessage.operation) {
            case INIT:
                playerID = rxMessage.playerID;
                sendAck();
                break;

            case GET_PLAYER_NAME:
                io.printMessage(rxMessage.text);
                String playerName = perform_get_player_name(botMode);
                io.printMessage("You chose " + playerName + ", wait for other players!");
                break;

            case BEGIN_TURN:
                io.showTurnBeginConfirm(rxMessage.actualPlayer);
                perform_ask_confirm();
                break;

            case GET_INPUT:
                io.printMessage(rxMessage.text);
                if (!isYourTurn(rxMessage)) {
                    sendAck();
                    break;
                }
                perform_get_input();
                break;

            case UPDATE_TILES:
                io.showBoardTiles(rxMessage.boardTiles);
                sendAck();
                break;

            case UPDATE_DICE:
                io.showRolledDice(rxMessage.dice);
                sendAck();
                break;

            case UPDATE_PLAYER:
                io.showPlayerData(rxMessage.actualPlayer, rxMessage.dice, rxMessage.players);
                sendAck();
                break;

            case ERROR:
                if (isYourTurn(rxMessage))
                    io.printError(rxMessage.text);
                sendAck();
                break;

            case INFO:
                io.printMessage(rxMessage.text);
                sendAck();
                break;

            default:
                io.printError("Unexpected problem");
                if(io.wantToPlayAgain()) HeckmeckCLI.startMenu();
                else System.exit(0);
                break;
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
