package CLI;

import Heckmeck.Game;
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


        /*output.showMenu();
        int numberOfPlayers = input.chooseNumberOfPlayers();
        Player[] players = new Player[numberOfPlayers];
        for (int i = 0; i < players.length; i++) {
            output.showSetPlayerName();
            players[i] = Player.generatePlayer(input.choosePlayerName());
        }
        Game game = new Game(players,output, input);
        game.play();*/
    }

    public static void startMenu(){
        CliIOHandler io = new CliIOHandler();

        io.showWelcomeMessage();
        if (io.wantToPlayRemote()) {
            if(io.wantToHost()){
                int numOfPlayers = io.chooseNumberOfPlayers();
                startGameServer(numOfPlayers);

                startLocalClient(io);
            }
            else{
                startLocalClient(io.askIPToConnect(), io);
            }
        } else {
            Game game = new Game(io);
            game.init();
            game.play();
        }

    }

    public static void startLocalClient(String IP, CliIOHandler io){


        Socket clientSocket = null;
        PrintWriter out;
        BufferedReader in;
        try {
            clientSocket = new Socket("127.0.0.1", 51734);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Client cli = new Client(false, io, in, out);
        Thread cliThread = new Thread(cli);
        cliThread.start();
        cli.commandInterpreter(false);
        //cli.startConnection(IP, 51734);

    }
    public static void startLocalClient(CliIOHandler io){
        startLocalClient("127.0.0.1", io);
    }
    public static void startGameServer(int  numOfPlayers){
        GameServer gameServer = new GameServer();
        gameServer.setNumberOfPlayers(numOfPlayers);
        new Thread(gameServer).start();
    }

    }
