package it.units.heckmeck;

import Heckmeck.Player;
import Heckmeck.Tile;
import Heckmeck.BoardTiles;
import exception.IllegalTileAddition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class TestPlayer {

    @ParameterizedTest
    @CsvSource(value = {"Luigi, Luigi", "Mario, Mario", "Sara, Sara"})
    void check_player_initialization(String passedName, String expectedName) {
        Player player = Player.generatePlayer(passedName);

        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedName, player.getName()),
                () -> Assertions.assertFalse(player.hasTile())
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {21, 22, 23, 24})
    void player_add_tile(int tileNumber) {
        Player player = Player.generatePlayer("Luigi");
        Tile desiredTile = Tile.generateTile(tileNumber);
        player.pickTile(desiredTile);
        Assertions.assertEquals(desiredTile, player.getLastPickedTile());
    }

    @Test
    void check_player_pick_one_tile_from_another_player() {
        BoardTiles board = BoardTiles.init();
        Player robbed = Player.generatePlayer("Derubato");
        Player robber = Player.generatePlayer("Ladro");
        Tile desiredTile = Tile.generateTile(21);
        robbed.pickTile(desiredTile);
        robber.stealTileFromPlayer(robbed);

        Assertions.assertEquals(desiredTile, robber.getLastPickedTile());
    }

    @Test
    void check_that_player_can_not_add_twice_the_same_tile_on_its_own_stack() {
        Tile newTile = Tile.generateTile(21);
        Player player = Player.generatePlayer("Luigi");
        String expectedMessage = "Tile number 21 is already present in the collection";

        player.pickTile(newTile);

        Exception ex = Assertions.assertThrows(IllegalTileAddition.class, () ->
                player.pickTile(newTile));
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    void check_cannot_steal_from_an_empty_player_stack(){
        Player player = Player.generatePlayer("Ladro");
        Player robbedPlayer = Player.generatePlayer("Derubato");
        int score = 30;
        Assertions.assertFalse(player.canStealFrom(robbedPlayer, score));
    }


    @ParameterizedTest
    @ValueSource(ints = {21, 23, 24, 25, 30})
    void check_cannot_steal_tile_with_not_equal_score(int score){
        Player player = Player.generatePlayer("Ladro");
        Player robbedPlayer = Player.generatePlayer("Derubato");
        robbedPlayer.pickTile(Tile.generateTile(22));
        Assertions.assertFalse(player.canStealFrom(robbedPlayer, score));
    }

}
