package Heckmeck;

import java.io.*;
import java.net.Socket;

public class SocketHandler implements Runnable{
    public final int playerId;
    private String message = "";
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private OutputStream outputStream;
    ObjectOutputStream objectOutputStream;
    InputStream inputStream;
    ObjectInputStream objectInputStream;

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
        try {

            this.out = new PrintWriter(outputStream, true);
            this.in = new BufferedReader(new InputStreamReader(inputStream));
            //objectOutputStream = new ObjectOutputStream(outputStream);
            //objectInputStream = new ObjectInputStream(inputStream);
            String incomingLine;

            while(true){
                incomingLine =  in.readLine();

                if(incomingLine.equals("hello server")){
                    this.message = incomingLine;
                    writeLine("{\"operation\":\"INFO\",\"text\":\"Hello player"+ playerId +"\",\"playerID\":\""+ playerId +"\"}" );
                }


                else{
                    this.message = incomingLine;
                System.out.println("Server receiving: " + this.message);

                    //writeLine("{\"operation\":\"INFO\",\"text\":\"OK\"}");

                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




    public String readReceivedMessage(){
        return this.message;
    }

    public String ReadIncomingMessage(){
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeLine(String message){
        out.println(message);
        out.flush();
    }

}
