package TCP.Server;

import Heckmeck.Game;
import TCP.Client;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class GameServer implements Runnable {
    public ServerSocket ss;
    public List<SocketHandler> sockets = new ArrayList<SocketHandler>();
    private boolean hostClosedRoom = false;
    private Thread t1;
    private int numOfPlayers;
    public Game game;

    private TCPIOHandler io;

    public GameServer() {
        try {
            ss = new ServerSocket(51734);
            io = new TCPIOHandler(this);
            this.numOfPlayers = 0;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    //TODO da cambiare probabilmente.. sarebbe bello se il numero venisse calcolato in base ai player nella stanza
    public void setNumberOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public void run() {
        try {
            System.out.println("You are now hosting on this machine: tell your IP address to your friends!");

            System.out.println(getIPAddress());
            acceptConnections();
            initClients();

        } catch (IOException e) {
            System.out.println("Error in acceptConnections()");
        }

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
                this.sockets.add(new SocketHandler(clientSocket, playerID));
                playerID++;
            }
            if (playerID == 8 || playerID == numOfPlayers) {
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

    public void initClients() {
        sockets.stream().forEach(e -> new Thread(e).start());
    }

    public void close() {
        try {
            ss.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        GameServer gameServer = new GameServer();
        Thread t1 = new Thread(gameServer);
        t1.start();
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