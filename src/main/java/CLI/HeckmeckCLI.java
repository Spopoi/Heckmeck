package CLI;

import Heckmeck.Game;
import TCP.Client;
import TCP.Server.GameServer;
import TCP.Server.TCPIOHandler;

import java.io.IOException;

public class HeckmeckCLI {
    public static void main(String[] args) {
        CliIOHandler io = new CliIOHandler();
        // (?)
        // io.getDesiredGame() to:
        //   - ask if want to play
        //   - ask if want remote or not
        io.showWelcomeMessage();
        if (io.wantToPlayRemote()) {
            GameServer gameServer = new GameServer();
            gameServer.setNumberOfPlayers(2);
            Thread serverThread = new Thread(gameServer);
            serverThread.start();
            startLocalClient();
            // gameServer autonomously asks and manage if you want to be host or client
            // gameServer.init();
            // gameServer.play();
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

    public static void startLocalClient(){
        Client cli = new Client();
        Thread cliThread = new Thread(cli);
        cliThread.start();
        cli.startConnection("127.0.0.1", 51734);

    }
}
