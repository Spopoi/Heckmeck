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


public class Client{

    private final PrintWriter out;
    private final BufferedReader in;
    private final MessageHandler messageHandler;
    private Map<Message.Action, MessageHandlerFunction> operationHandlers;
    private interface MessageHandlerFunction {
        Message handleMessage(Message rxMessage);
    }
    public Client(IOHandler io,  BufferedReader in, PrintWriter out){
        this.in = in;
        this.out = out;
        messageHandler = new MessageHandler(io);
        initCommands();
    }

    private void initCommands(){
        operationHandlers = new HashMap<>();
        operationHandlers.put(INIT              , messageHandler::performInit);
        operationHandlers.put(GET_PLAYER_NAME   , messageHandler::perform_get_player_name);
        operationHandlers.put(BEGIN_TURN        , messageHandler::perform_ask_confirm);
        operationHandlers.put(UPDATE_TILES      , messageHandler::perform_update_tiles);
        operationHandlers.put(UPDATE_DICE       , messageHandler::perform_update_dice);
        operationHandlers.put(UPDATE_PLAYER     , messageHandler::perform_update_player);
        operationHandlers.put(ERROR             , messageHandler::perform_error);
        operationHandlers.put(INFO              , messageHandler::perform_info);
        operationHandlers.put(CHOOSE_DICE       , messageHandler::perform_choose_dice);
        operationHandlers.put(WANT_PICK         , messageHandler::performWantPick);
        operationHandlers.put(WANT_STEAL        , messageHandler::performWantSteal);
        operationHandlers.put(BUST              , messageHandler::performBust);
        operationHandlers.put(WAIT              , messageHandler::performWaitYourTurn);
    }

    public void sendLine(String line) {
        out.println(line);
    }
    public String readRxBuffer() throws IOException {
        String resp = null;
        resp = in.readLine();
        return resp;
    }

    public Message readIncomingMessage() throws IOException {
        String serialized = readRxBuffer();
        return Message.fromJSON(serialized);
    }

    public void sendMessage(Message msg){
        sendLine(msg.toJSON());
    }


    public static void main(String[] args){
        HeckmeckCLI.startMenu();
    }

    public void commandInterpreter() throws IOException {
        Message rxMessage;
        while (true){
            rxMessage = readIncomingMessage();
            if (rxMessage != null) {
                Message.Action operation = rxMessage.operation;

                MessageHandlerFunction handler = operationHandlers.get(operation);
                if(handler!= null){
                    Message replyMessage = handler.handleMessage(rxMessage);
                    sendMessage(replyMessage);
                }
                else{
                    messageHandler.io.printMessage("Connection with server is closed");
                    return;
                }
            }
        }
    }



}
