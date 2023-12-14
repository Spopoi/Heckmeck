package TCP.Server;

import TCP.Message;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

public class SocketHandler implements Runnable{
    public final int playerId;
    //private final IOBufferInterface ioBuffer;
    private final BufferedReader in;
    private final PrintWriter out;
    private String rxString = "";
    private String txString = "";

    String playerName;
    ObjectOutputStream objectOutputStream;
    InputStream inputStream;

    public SocketHandler(int playerId, BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
        this.playerId = playerId;

    }
    public void run(){
        System.out.println("Connection in socket handler ok, this is client thread #" + playerId);
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

        try {
            rxString =  in.readLine();
        } catch (IOException e) {
            System.out.println("ERROR: Player"+ playerId+ " " + playerName + " seems to be disconnected");
        }
        return rxString;
    }

    public Message readReceivedMessage(){
        return Message.fromJSON(rxString);
    }

    public Message writeMessage(Message message){

        writeLine(message.toJSON());
        return Message.fromJSON(rxString);
    }

    public boolean initClient(){
        Message msg = Message.generateMessage();
        System.out.println("Socket handler init client: " + playerId);
        msg.setPlayerID(playerId);
        msg.setOperation(Message.Action.INIT);
        msg.setText("Hello");
        Message respMsg = writeMessage(msg);

        //playerName = respMsg.text;
        return respMsg.playerID==playerId;
    }

    public void testGetOtherPlayers(){

    }
}

//TODO Via Lorenzo Butti 3