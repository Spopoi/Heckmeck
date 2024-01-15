package TCP.Server;

import TCP.Message;

import java.io.*;

public class ClientHandler implements Runnable{
    private final int playerId;
    //private final IOBufferInterface ioBuffer;
    private final BufferedReader in;
    private final PrintWriter out;


    public ClientHandler(int playerId, BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
        this.playerId = playerId;

    }
    public void run(){
        System.out.println("Connection established for player number: " + playerId);
        initClient();
    }

    public int getPlayerID(){
        return this.playerId;
    }
    public String readLine(){
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String writeAndRead(String message){
        out.println(message);
        return readLine();
    }

    public Message writeMessage(Message message){
        return Message.fromJSON(writeAndRead(message.toJSON()));
    }

    public boolean initClient(){
        Message msg = Message.generateMessage();
        System.out.println("Player " + playerId + " initialised"); //TODO stringhe presenti nel clientHandler?
        msg.setPlayerID(playerId);
        msg.setOperation(Message.Action.INIT);
        Message respMsg = writeMessage(msg);
        return respMsg.id==playerId;
    }

}