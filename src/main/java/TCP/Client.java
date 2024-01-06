package TCP;

import CLI.HeckmeckCLI;
import Heckmeck.Components.Player;
import Heckmeck.IOHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static TCP.Message.Action.*;


public class Client implements Runnable{

    private PrintWriter out;
    private BufferedReader in;

    private boolean connected = false;
    IOHandler io;
    private boolean botMode = false;
    private int playerID;
    private Map<Message.Action, MessageHandlerFunction> operationHandlers;
    public Client(IOHandler io, BufferedReader in, PrintWriter out){
        this.io = io;
        this.in = in;
        this.out = out;
        this.connected = true;

        operationHandlers = new HashMap<>();
        operationHandlers.put(INIT              , this::performInit);
        operationHandlers.put(GET_PLAYER_NAME   , this::perform_get_player_name);
        operationHandlers.put(BEGIN_TURN        , this::perform_ask_confirm);
        operationHandlers.put(PLAY_AGAIN        , this::perform_play_again);
        operationHandlers.put(UPDATE_TILES      , this::perform_update_tiles);
        operationHandlers.put(UPDATE_DICE       , this::perform_update_dice);
        operationHandlers.put(UPDATE_PLAYER     , this::perform_update_player);
        operationHandlers.put(ERROR             , this::perform_error);
        operationHandlers.put(INFO              , this::perform_info);
        operationHandlers.put(CHOOSE_DICE       , this::perform_choose_dice);
        operationHandlers.put(WANT_PICK         , this::performWantPick);
        operationHandlers.put(WANT_STEAL        , this::performWantSteal);

    }




    private interface MessageHandlerFunction {
        void handleMessage(Message rxMessage);
    }
    public void handleMessage(Message rxMessage) {
        MessageHandlerFunction handler = (MessageHandlerFunction) operationHandlers.get(rxMessage.operation);
        if (handler != null) {
            handler.handleMessage(rxMessage);
        } else {
            handleDefault(rxMessage);
        }
    }

    private void handleDefault(Message rxMessage) {
        io.printError("Unexpected problem, operation was " + rxMessage.operation.toString());
        if (io.wantToPlayAgain()) HeckmeckCLI.startMenu();
        else System.exit(0);
    }
    public String sendLine(String line) {
        out.println(line);
        String resp = "";
        return resp;
    }
    public String readRxBuffer(){
        String resp = null;
        try {
            resp = in.readLine();

        } catch (IOException e) {
            io.printError("Error in receiving data. Stopping client");
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
        sendMessage(
                Message.generateMessage().
                        setOperation(Message.Action.ACK)
        );
    }

    @Override
    public void run() {

        commandInterpreter(botMode);
    }

    public static void main(String args[]){
        HeckmeckCLI.startMenu();
    }

    public void commandInterpreter(boolean botMode){
        Message rxMessage = null;
        while (true){
            if(connected) { //TODO rimuovere questo if siccome se parte l'interpreter client sicuramente connesso?
                try {
                    rxMessage = readIncomingMessage();
                } catch (NullPointerException e) {
                    io.printError("Error reading incoming message" );
                    if(io.wantToPlayAgain()) HeckmeckCLI.startMenu(); //TODO lanciamo WantToPlayAgain? Però lasciamo che puoi tornare al menu?
                    else System.exit(0);
                }
                if (rxMessage != null) {
                    handleMessage(rxMessage);
                }
            }
        }
    }

    private void sendMessage(Message msg){
        sendLine(msg.toJSON());
    }


    private void performInit(Message rxMessage) {
        this.playerID = rxMessage.id;
        sendMessage(
                Message.generateMessage().
                        setOperation(Message.Action.RESPONSE).
                        setPlayerID(playerID)
        );
    }

    public void perform_play_again(Message rxMessage) {
        sendMessage(
                Message.generateMessage().
                        setOperation(Message.Action.RESPONSE).
                        setDecision(io.wantToPlayAgain())
        );
    }
    public void perform_choose_dice(Message rxMessage) {
        sendMessage(
                Message.generateMessage().
                        setOperation(Message.Action.RESPONSE).
                        setText(io.chooseDie(rxMessage.currentPlayer).toString())
        );
    }
    private void perform_ask_confirm(Message rxMessage) {
        io.showTurnBeginConfirm(rxMessage.currentPlayer);
        sendMessage(
                Message.generateMessage().
                        setOperation(Message.Action.RESPONSE).
                        setText("nothing")
        );
    }

    public void perform_get_player_name(Message rxMessage) {
        String playerName = io.choosePlayerName(rxMessage.currentPlayer);
        sendMessage(
                Message.generateMessage().
                        setOperation(Message.Action.RESPONSE).
                        setText(playerName)
        );
        io.printMessage("You chose " + playerName + ", wait for other players!");
    }

    public void performWantPick(Message rxMessage){
        sendMessage(
                Message.generateMessage().
                        setOperation(Message.Action.RESPONSE).
                        setDecision(io.wantToPick(rxMessage.currentPlayer, rxMessage.diceScore, rxMessage.availableTileNumber))
        );

    }
    public void performWantSteal(Message rxMessage){
        sendMessage(
                Message.generateMessage().
                        setOperation(Message.Action.RESPONSE).
                        setDecision(io.wantToSteal(rxMessage.currentPlayer, rxMessage.robbedPlayer))
        );
    }
    private void perform_info(Message rxMessage) {
        io.printMessage(rxMessage.text);
        sendAck();
    }
    private void perform_error(Message rxMessage) {
        sendAck();
        io.printError(rxMessage.text);
    }
    private void perform_update_player(Message rxMessage) {
        sendAck();
        io.showPlayerData(rxMessage.currentPlayer, rxMessage.dice, rxMessage.players);
    }
    private void perform_update_tiles(Message rxMessage) {
        sendAck();
        io.showBoardTiles(rxMessage.boardTiles);
    }

    private void perform_update_dice(Message rxMessage) {
        sendAck();
        io.showRolledDice(rxMessage.dice);
    }
}
