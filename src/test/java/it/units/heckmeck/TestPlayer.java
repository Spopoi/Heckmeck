package it.units.heckmeck;

import Heckmeck.Player;
import Heckmeck.Tile;
import Heckmeck.Tiles;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class TestPlayer {

    @ParameterizedTest
    @CsvSource(value = {"Luigi, Luigi", ", ''"})
    void check_player_initialization(String passedName, String expectedName){
        Player player = Player.generatePlayer(passedName);

        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedName, player.getName()),
                () -> Assertions.assertFalse(player.hasTile())
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {21, 22, 23, 24})
    void player_add_tile_from_board(int tileNumber) {
        Tiles board = Tiles.init();
        Player player = Player.generatePlayer("Luigi");
        Tile desiredTile = Tile.generateTile(tileNumber);

        player.pickTileFromBoard(desiredTile, board);

        Assertions.assertEquals(desiredTile, player.getLastPickedTile());
    }
    
}
