package TCP.Server;

import Heckmeck.Game;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class GameServer implements Runnable {
    public ServerSocket ss;
    public List<ClientHandler> clients = new ArrayList<ClientHandler>();
    private boolean hostClosedRoom = false;
    private Thread t1;
    private final int numOfPlayers;
    public Game game;

    public GameServer(int numOfPlayers) {
        try {
            ss = new ServerSocket(51734);
            this.numOfPlayers = numOfPlayers;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void run() {
        try {
            //TODO spostare messaggi in un writer
            System.out.println("You are now hosting on this machine: tell your IP address to your friends!");
            System.out.println(getIPAddress());
            acceptConnections();
            //initClients();

        } catch (IOException e) {
            System.out.println("Error in acceptConnections()");
        }

        TCPIOHandler io = new TCPIOHandler(clients);
        io.showWelcomeMessage();

        game = new Game(io);
        game.init();
        game.play();
    }


    public void acceptConnections() throws IOException {
        System.out.println("Room open");
        int playerID = 0;
        while (!isRoomClosed()) {
            Socket clientSocket;
            clientSocket = ss.accept();
            System.out.println("Accepted incoming connection #: " + playerID);

            if (clientSocket.isConnected()) {
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

                this.clients.add(new ClientHandler(playerID, in, out));
                playerID++;
            }
            if (playerID == 7 || playerID == numOfPlayers) {
                closeRoom();
            }
        }
    }

    public boolean isRoomClosed() {
        return hostClosedRoom;
    }

    public void closeRoom() {
        hostClosedRoom = true;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }



    public void close() {
        try {
            ss.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getIPAddress(){
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }

                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    String ipAddress = address.getHostAddress();
                    if (ipAddress.startsWith("192.168.1.")) {
                        return ipAddress;
                    }
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";

    }
}