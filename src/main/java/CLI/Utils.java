package CLI;

import Heckmeck.FileReader;
import Heckmeck.IOHandler;
import TCP.Client;
import TCP.Server.ClientHandler;

import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Enumeration;

public class Utils {

    private static final String LOGO_FILE = "LOGO";
    private static final String RULES = "RULES";
    private static final String MENU = "MENU";

    private static final String MULTIPLAYER = "MULTIPLAYER";


    private static final String ACTUAL_PLAYER_INFO_TEMPLATE_FILE = "PLAYER_INFO_TEMPLATE";
//TODO togliere costruttore non usato?
    private Utils() {
    }
//Todo: refactoring?

    public static String getLogo() {
        return getFileString(getPath(LOGO_FILE));
    }
    public static String getMultyplayerPath(){
        return getFileString(getPath(MULTIPLAYER));
    }

    public static String getMenu(){
        return getFileString(getPath(MENU));
    }

    public static String getRules(){
        return getFileString(getPath(RULES));
    }

    public static Path getPath(String fileName) {
        URL tilesResource = CliIOHandler.class.getClassLoader().getResource(fileName);
        Path resourcePath = null;
        try {
            resourcePath = Path.of(tilesResource.toURI());
        } catch (URISyntaxException ex) {
            System.out.println(ex);
        }
        return resourcePath;
    }

    public static String getActualPlayerInfoTemplate() {
        return getFileString(getPath(ACTUAL_PLAYER_INFO_TEMPLATE_FILE));
    }

    public static String getFileString(Path filepath){
        return FileReader.readTextFile(filepath);
    }

    public static String collectionToString(Collection<?> collection) {
        TextBlock collectionAsTextBlock = new TextBlock("");
        for (var item : collection) {
            // at first iteration collectionAsText will have height=0 --> 2 spaces
            collectionAsTextBlock.concatenateWith(new TextBlock(item.toString()), 1);
        }
        return collectionAsTextBlock.toString();
    }



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

    public static Client startClient( String IP, IOHandler io){
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
        return new Client(false, io, in, out);
    }


}
