package TCP.Server;

import TCP.Message;
import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

public class SocketHandler implements Runnable{
    public final int playerId;
    private String rxString = "";
    private String txString = "";
    private Socket clientSocket;
    public PrintWriter out;
    public BufferedReader in;
    String playerName;
    ObjectOutputStream objectOutputStream;
    InputStream inputStream;
    Gson gson = new Gson();

    public SocketHandler(int playerId, BufferedReader in,  PrintWriter out) {
        this.in = in;
        this.out = out;
        this.playerId = playerId;

    }
    public void run(){
        System.out.println("Connection in socket handler ok, this is client thread #" + playerId);
        initClient();
    }
    public String readLine(){
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void writeLine(String message){
        out.println(message);
        try {
            rxString =  in.readLine();
        } catch (IOException e) {
            System.out.println("ERROR: Player"+ playerId+ " " + playerName + " seems to be disconnected");
        }
    }

    public Message readReceivedMessage(){
        return gson.fromJson(rxString, Message.class);
    }

    public Message writeMessage(Message message){
        writeLine(gson.toJson(message));
        return gson.fromJson(rxString, Message.class);
    }

    public boolean initClient(){
        Message msg = new Message();
        System.out.println("Socket handler init client: " + playerId);
        msg.setPlayerID(playerId);
        msg.setOperation(Message.Action.INIT);
        msg.setText("Hello");
        Message respMsg = writeMessage(msg);
        //playerName = respMsg.text;
        return respMsg.playerID==playerId;
    }
}
