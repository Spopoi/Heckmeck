package it.units.heckmeck;

import CLI.CliInputHandler;
import CLI.CliOutputHandler;
import Heckmeck.Player;
import Heckmeck.Rules;
import Heckmeck.Tile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestGameWinner {

    @Test
    public void winnerWithHigherWormNumber(){
        Player expectedWinner = Player.generatePlayer("winner");
        Player loser1 = Player.generatePlayer("loser1");
        Player loser2 = Player.generatePlayer("loser2");
        expectedWinner.addTile(Tile.generateTile(30));
        loser1.addTile(Tile.generateTile(21));
        loser1.addTile(Tile.generateTile(23));
        loser2.addTile(Tile.generateTile(22));
        Player[] players = {loser1, expectedWinner,loser2};
        Player winner = Rules.whoIsTheWinner(players);
        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedWinner, winner),
                () -> Assertions.assertNotEquals(loser1, winner),
                () -> Assertions.assertNotEquals(loser2, winner)
        );
    }

    @Test
    public void winnerWithLowestNumberOfTiles(){
        Player expectedWinner = Player.generatePlayer("winner");
        Player loser1 = Player.generatePlayer("loser");
        Player loser2 = Player.generatePlayer("loser2");
        expectedWinner.addTile(Tile.generateTile(30));
        loser1.addTile(Tile.generateTile(21));
        loser1.addTile(Tile.generateTile(25));
        loser2.addTile(Tile.generateTile(22));
        Player[] players = {loser1, expectedWinner,loser2};
        Player winner = Rules.whoIsTheWinner(players);
        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedWinner, winner),
                () -> Assertions.assertNotEquals(loser1, winner),
                () -> Assertions.assertNotEquals(loser2, winner)
        );
    }

    @Test
    public void winnerWithHigherTileNumber(){
        Player expectedWinner = Player.generatePlayer("winner");
        Player loser1 = Player.generatePlayer("loser");
        Player loser2 = Player.generatePlayer("loser2");
        expectedWinner.addTile(Tile.generateTile(22));
        expectedWinner.addTile(Tile.generateTile(26));
        loser1.addTile(Tile.generateTile(21));
        loser1.addTile(Tile.generateTile(25));
        loser2.addTile(Tile.generateTile(22));
        Player[] players = {loser1, expectedWinner,loser2};
        Player winner = Rules.whoIsTheWinner(players);
        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedWinner, winner),
                () -> Assertions.assertNotEquals(loser1, winner),
                () -> Assertions.assertNotEquals(loser2, winner)
        );
    }
}
