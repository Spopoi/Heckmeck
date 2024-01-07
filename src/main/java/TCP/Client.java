package TCP;

import CLI.HeckmeckCLI;
import Heckmeck.IOHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static TCP.Message.Action.*;


public class Client implements Runnable{

    private final PrintWriter out;
    private final BufferedReader in;
    private IOHandler io;
    private ActionHandler msgHandler;
    private Map<Message.Action, MessageHandlerFunction> operationHandlers;
    private interface MessageHandlerFunction {
        void handleMessage(Message rxMessage);
    }
    public Client(IOHandler io,  BufferedReader in, PrintWriter out){


        this.in = in;
        this.out = out;
        this.io = io;
        msgHandler = new ActionHandler(this);
        initCommands();
    }

    private void initCommands(){
        operationHandlers = new HashMap<>();
        operationHandlers.put(INIT              , msgHandler::performInit);
        operationHandlers.put(GET_PLAYER_NAME   , msgHandler::perform_get_player_name);
        operationHandlers.put(BEGIN_TURN        , msgHandler::perform_ask_confirm);
        operationHandlers.put(PLAY_AGAIN        , msgHandler::perform_play_again);
        operationHandlers.put(UPDATE_TILES      , msgHandler::perform_update_tiles);
        operationHandlers.put(UPDATE_DICE       , msgHandler::perform_update_dice);
        operationHandlers.put(UPDATE_PLAYER     , msgHandler::perform_update_player);
        operationHandlers.put(ERROR             , msgHandler::perform_error);
        operationHandlers.put(INFO              , msgHandler::perform_info);
        operationHandlers.put(CHOOSE_DICE       , msgHandler::perform_choose_dice);
        operationHandlers.put(WANT_PICK         , msgHandler::performWantPick);
        operationHandlers.put(WANT_STEAL        , msgHandler::performWantSteal);
    }


    public void handleMessage(Message rxMessage) {
        MessageHandlerFunction handler = (MessageHandlerFunction) operationHandlers.get(rxMessage.operation);
        if (handler != null) {
            handler.handleMessage(rxMessage);
        } else {
            msgHandler.handleDefault(rxMessage);
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
            msgHandler.stopClient();
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

    public IOHandler getIo(){
        return io;
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
