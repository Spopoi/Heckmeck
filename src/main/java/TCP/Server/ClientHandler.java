package TCP.Server;

import TCP.Message;

import java.io.*;

public class ClientHandler implements Runnable{
    public final int playerId;
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
    public String writeLine(String message){
        out.println(message);
        String rxString = readLine();
        //rxString =  readLine();
        return rxString;
    }

    //public Message readReceivedMessage(){
    //    return Message.fromJSON(rxString);
    //}

    public Message writeMessage(Message message){
        return Message.fromJSON(writeLine(message.toJSON()));
    }

    public boolean initClient(){
        Message msg = Message.generateMessage();
        System.out.println("Player " + playerId + " initialised");
        msg.setPlayerID(playerId);
        msg.setOperation(Message.Action.INIT);
        msg.setText("Hello");
        Message respMsg = writeMessage(msg);
        return respMsg.playerID==playerId;
    }

}

//TODO Via Lorenzo Butti 3