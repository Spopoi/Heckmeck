package CLI;

import Heckmeck.FileReader;
import Heckmeck.Game;
import Heckmeck.IOHandler;
import TCP.Client;
import TCP.Server.GameServer;
import TCP.Server.TCPIOHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HeckmeckCLI {
    public static void main(String[] args) {
        startMenu();
    }

    public static void startMenu(){
        CliIOHandler io = new CliIOHandler();
        io.showWelcomeMessage();

        do {
            io.printMessage("""
                    Choose what you want to do:
                        - (1) Start Heckmeck
                        - (2) Multiplayer
                        - (3) Rules
                        - (4) Exit                        
                    """);

            String choice = io.getInitialChoice(
                    "Incorrect input, insert one possible choice (1, 2, 3, 4)"
            );

            switch (choice) {
                case "1":
                    Game game = new Game(io);
                    game.init();
                    game.play();
                    break;
                case "2":
                    if (io.wantToHost()) {
                        int numOfPlayers = io.chooseNumberOfPlayers();
                        startGameServer(numOfPlayers);
                        startLocalClient(io);
                    } else {
                        startLocalClient(io.askIPToConnect(), io);
                    }
                    break;
                case "3":
                    io.printMessage(FileReader.readTextFile(Utils.getRulesPath()));
                    break;
                case "4":
                    io.printMessage("Exiting Heckmeck. Goodbye!");
                    return; // Exit the program
                default:
                    break;
            }
        } while (true);
    }


    //TODO: Move methods?
    public static void startLocalClient(String IP, IOHandler io){


        Socket clientSocket = null;
        PrintWriter out;
        BufferedReader in;
        try {
            clientSocket = new Socket(IP, 51734);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Client cli = new Client(false, io, in, out);
        Thread cliThread = new Thread(cli);
        cliThread.start();
        io.printMessage("Local client started, waiting for your turn to begin");
        try {
            cliThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
    public static void startLocalClient(IOHandler io){
        startLocalClient("127.0.0.1", io);
    }
    public static void startGameServer(int  numOfPlayers){
        GameServer gameServer = new GameServer(numOfPlayers);
        new Thread(gameServer).start();
    }


}
