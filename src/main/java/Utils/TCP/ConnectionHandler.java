package Utils.TCP;

import Heckmeck.IOHandler;
import TCP.Client;
import TCP.Server.ClientHandler;
import TCP.Server.GameServer;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

public class ConnectionHandler {


    public static String getLanIpAddress() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();

                // Check if the interface is not a loopback and is up
                if (!networkInterface.isLoopback() && networkInterface.isUp()) {
                    Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();

                    while (addresses.hasMoreElements()) {
                        InetAddress address = addresses.nextElement();

                        // Check if it's an IPv4 address
                        if (address.getHostAddress().matches("\\d+\\.\\d+\\.\\d+\\.\\d+")) {
                            return address.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        // Return a default value or handle the absence of a LAN IP address
        return "Unknown";
    }
    public static ClientHandler startClientHandler(int playerID, Socket clientSocket){
        OutputStream outputStream;
        InputStream inputStream;
        try {
            outputStream = clientSocket.getOutputStream();
            inputStream = clientSocket.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        PrintWriter out = new PrintWriter(outputStream, true);
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        return new ClientHandler(playerID, in, out);
    }

    public static Client startClient(String IP, IOHandler io){
        Socket clientSocket;
        PrintWriter out;
        BufferedReader in;
        try {
            clientSocket = new Socket(IP, 51734);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Client(io, in, out);
    }

    //TODO: Move methods?
    public static void startLocalClient(String IP, IOHandler io){
        Client cli = startClient(IP, io);
        io.printMessage("Local client started, waiting for your turn to begin");
        cli.commandInterpreter(false);
    }

    public static void startLocalClient(IOHandler io){
        startLocalClient("127.0.0.1", io);
    }

    public static void startGameServer(int  numOfPlayers){
        GameServer gameServer = new GameServer(numOfPlayers);
        new Thread(gameServer).start();
    }
}
