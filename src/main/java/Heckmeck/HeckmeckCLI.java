package Heckmeck;

import CLI.CliInputHandler;
import CLI.CliOutputHandler;

import java.io.IOException;

public class HeckmeckCLI {
    public static void main(String[] args) throws IOException {
        CliInputHandler input = new CliInputHandler();
        CliOutputHandler output = new CliOutputHandler();
        output.showMenu();
        int numberOfPlayers = input.chooseNumberOfPlayers();
        Player[] players = new Player[numberOfPlayers];
        for (int i = 0; i < players.length; i++) {
            output.showSetPlayerName();
            players[i] = Player.generatePlayer(input.choosePlayerName());
        }
        Game game = new Game(players,output, input);
        game.play();
    }
}
