package Heckmeck;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

public class SocketHandler implements Runnable{
    public final int playerId;
    //private Message message;
    private String rxString = "";
    private String txString = "";
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private OutputStream outputStream;
    ObjectOutputStream objectOutputStream;
    InputStream inputStream;
    Gson gson = new Gson();


    public SocketHandler(Socket clientSocket, int playerId) {
        this.clientSocket = clientSocket;
        this.playerId = playerId;

        try {
            outputStream = clientSocket.getOutputStream();
            inputStream = clientSocket.getInputStream();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public void run(){
        System.out.println("Connection in client handler ok, this is client thread #" + playerId);

        this.out = new PrintWriter(outputStream, true);
        this.in = new BufferedReader(new InputStreamReader(inputStream));
        String incomingLine;

            /*while(true){
                incomingLine =  in.readLine();

                if(incomingLine.equals("hello server")){
                    //this.rxString = incomingLine;
                    Message initMessage = new Message();
                    initMessage.setOperation(Message.Action.INIT);
                    initMessage.setText("Hello player"+playerId);
                    initMessage.setPlayerID(playerId);
                    writeMessage(initMessage);
                    System.out.println("Server receiving: " + incomingLine);

                    //writeLine("{\"operation\":\"INIT\",\"text\":\"Hello player"+ playerId +"\",\"playerID\":\""+ playerId +"\"}" );
                }


                else{
                    //this.rxString = incomingLine;
                System.out.println("Server receiving: " + incomingLine);

                }
            }*/

    }




    public String readLine(){
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Message readReceivedMessage(){
        return gson.fromJson(this.rxString, Message.class);
    }

    public Message writeMessage(Message message){
        writeLine(gson.toJson(message));
        //System.out.println("RXSTRING after sending command was: "+ rxString);
        return gson.fromJson(this.rxString, Message.class);

    }



    public void writeLine(String message){
        out.println(message);
        //out.flush();
        try {
            this.rxString =  in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean initClient(){
        Message msg = new Message();
        System.out.println("Socket handler init client: " + playerId);
        msg.setPlayerID(playerId);
        msg.setOperation(Message.Action.INIT);
        msg.setText("Hello");
        Message respMsg = writeMessage(msg);
        return respMsg.playerID==playerId;

    }

}
