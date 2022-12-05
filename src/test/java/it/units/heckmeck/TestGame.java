package it.units.heckmeck;

import CLI.CliInputHandler;
import CLI.CliOutputHandler;
import Heckmeck.Game;
import Heckmeck.Player;
import Heckmeck.Tile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestGame {

    @Test
    public void winnerWithHigherWormNumber(){
        Player winner = Player.generatePlayer("winner");
        Player loser1 = Player.generatePlayer("loser1");
        Player loser2 = Player.generatePlayer("loser2");
        winner.addTile(Tile.generateTile(30));
        loser1.addTile(Tile.generateTile(21));
        loser2.addTile(Tile.generateTile(22));
        Player[] players = {loser1, winner,loser2};
        Game game = new Game(players,new CliOutputHandler(), new CliInputHandler());
        Assertions.assertEquals(winner, game.whoIsTheWinner());
    }

    @Test
    public void winnerWithHigherNumberOfTiles(){
        Player winner = Player.generatePlayer("winner");
        Player loser1 = Player.generatePlayer("loser");
        Player loser2 = Player.generatePlayer("loser2");
        winner.addTile(Tile.generateTile(21));
        winner.addTile(Tile.generateTile(25));
        loser1.addTile(Tile.generateTile(30));
        loser2.addTile(Tile.generateTile(22));
        Player[] players = {loser1, winner,loser2};
        Game game = new Game(players,new CliOutputHandler(), new CliInputHandler());
        Assertions.assertEquals(winner, game.whoIsTheWinner());
    }
}
