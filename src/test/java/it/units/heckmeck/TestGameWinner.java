package it.units.heckmeck;

import Heckmeck.Components.Player;
import Heckmeck.Rules;
import Heckmeck.Components.Tile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestGameWinner {

    @Test
    public void winnerWithHigherWormNumber(){
        Player expectedWinner = Player.generatePlayer(0);
        expectedWinner.setPlayerName("winner");
        Player loser1 = Player.generatePlayer(1);
        loser1.setPlayerName("loser1");
        Player loser2 = Player.generatePlayer(2);
        loser2.setPlayerName("loser2");

        expectedWinner.pickTile(Tile.generateTile(30));
        loser1.pickTile(Tile.generateTile(21));
        loser1.pickTile(Tile.generateTile(23));
        loser2.pickTile(Tile.generateTile(22));
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
        Player expectedWinner = Player.generatePlayer(0);
        expectedWinner.setPlayerName("winner");
        Player loser1 = Player.generatePlayer(1);
        loser1.setPlayerName("loser1");
        Player loser2 = Player.generatePlayer(2);
        loser2.setPlayerName("loser2");
        expectedWinner.pickTile(Tile.generateTile(30));
        loser1.pickTile(Tile.generateTile(21));
        loser1.pickTile(Tile.generateTile(25));
        loser2.pickTile(Tile.generateTile(22));
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
        Player expectedWinner = Player.generatePlayer(0);
        expectedWinner.setPlayerName("winner");
        Player loser1 = Player.generatePlayer(1);
        loser1.setPlayerName("loser1");
        Player loser2 = Player.generatePlayer(2);
        loser2.setPlayerName("loser2");
        expectedWinner.pickTile(Tile.generateTile(22));
        expectedWinner.pickTile(Tile.generateTile(26));
        loser1.pickTile(Tile.generateTile(21));
        loser1.pickTile(Tile.generateTile(25));
        loser2.pickTile(Tile.generateTile(22));
        Player[] players = {loser1, expectedWinner,loser2};
        Player winner = Rules.whoIsTheWinner(players);
        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedWinner, winner),
                () -> Assertions.assertNotEquals(loser1, winner),
                () -> Assertions.assertNotEquals(loser2, winner)
        );
    }
}
