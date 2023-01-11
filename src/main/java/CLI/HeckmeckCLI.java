package CLI;

import CLI.CliIOHandler;
import CLI.CliInputHandler;
import CLI.CliOutputHandler;
import Heckmeck.Game;

import java.io.IOException;

public class HeckmeckCLI {
    public static void main(String[] args) throws IOException {
        CliIOHandler io = new CliIOHandler();
        Game game = new Game(io);
        game.init();
        game.play();

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
}
