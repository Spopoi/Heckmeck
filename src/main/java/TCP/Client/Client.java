package TCP.Client;

import CLI.HeckmeckCLI;
import Heckmeck.IOHandler;
import TCP.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static TCP.Message.Action.*;


public class Client implements Runnable{

    private final PrintWriter out;
    private final BufferedReader in;
    private final IOHandler io;
    private final MessageHandler messageHandler;
    private Map<Message.Action, MessageHandlerFunction> operationHandlers;
    private interface MessageHandlerFunction {
        Message handleMessage(Message rxMessage);
    }
    public Client(IOHandler io,  BufferedReader in, PrintWriter out){


        this.in = in;
        this.out = out;
        this.io = io;
        messageHandler = new MessageHandler(io);
        initCommands();
    }

    private void initCommands(){
        operationHandlers = new HashMap<>();
        operationHandlers.put(INIT              , messageHandler::performInit);
        operationHandlers.put(GET_PLAYER_NAME   , messageHandler::perform_get_player_name);
        operationHandlers.put(BEGIN_TURN        , messageHandler::perform_ask_confirm);
        operationHandlers.put(PLAY_AGAIN        , messageHandler::perform_play_again);
        operationHandlers.put(UPDATE_TILES      , messageHandler::perform_update_tiles);
        operationHandlers.put(UPDATE_DICE       , messageHandler::perform_update_dice);
        operationHandlers.put(UPDATE_PLAYER     , messageHandler::perform_update_player);
        operationHandlers.put(ERROR             , messageHandler::perform_error);
        operationHandlers.put(INFO              , messageHandler::perform_info);
        operationHandlers.put(CHOOSE_DICE       , messageHandler::perform_choose_dice);
        operationHandlers.put(WANT_PICK         , messageHandler::performWantPick);
        operationHandlers.put(WANT_STEAL        , messageHandler::performWantSteal);
    }


    private void handleMessage(Message rxMessage) {
        MessageHandlerFunction handler = operationHandlers.get(rxMessage.operation);
        if (handler != null) {
           /* if (rxMessage.isBroadCast) {
                sendMessage(actionHandler.ack());
                handler.handleMessage(rxMessage);
            }
            else{
            }*/
            Message replyMessage = handler.handleMessage(rxMessage);
            sendMessage(replyMessage);

        } else {
            messageHandler.handleDefault();
        }
    }

    public void sendLine(String line) {
        out.println(line);
    }
    public String readRxBuffer(){
        String resp = null;
        try {
            resp = in.readLine();
        } catch (IOException e) {
            messageHandler.stopClient();
        }
        return resp;
    }

    public Message readIncomingMessage(){
        String serialized = readRxBuffer();
        return Message.fromJSON(serialized);
    }

    public void sendMessage(Message msg){
        sendLine(msg.toJSON());
    }

    @Override
    public void run() {
        commandInterpreter();
    }

    public static void main(String args[]){
        HeckmeckCLI.startMenu();
    }

    public void commandInterpreter(){
        Message rxMessage = null;
        while (true){
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
