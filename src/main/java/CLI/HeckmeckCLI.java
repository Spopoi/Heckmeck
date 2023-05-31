package CLI;

import Heckmeck.Game;
import TCP.Client;
import TCP.Server.GameServer;
import TCP.Server.TCPIOHandler;

import java.io.IOException;

public class HeckmeckCLI {
    public static void main(String[] args) {
        CliIOHandler io = new CliIOHandler();

        io.showWelcomeMessage();
        if (io.wantToPlayRemote()) {
            if(io.wantToHost()){
                startGameServer(io);
                startLocalClient();
            }
            else{
                startLocalClient(io.askIPToConnect());
            }
        } else {
            Game game = new Game(io);
            game.init();
            game.play();
        }



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

    public static void startLocalClient(String IP){
        Client cli = new Client(false);
        Thread cliThread = new Thread(cli);
        cliThread.start();
        cli.startConnection(IP, 51734);

    }
    public static void startLocalClient(){
        startLocalClient("127.0.0.1");
    }
    public static void startGameServer(CliIOHandler io){
        GameServer gameServer = new GameServer();
        gameServer.setNumberOfPlayers(io.chooseNumberOfPlayers());
        new Thread(gameServer).start();
    }

    }
