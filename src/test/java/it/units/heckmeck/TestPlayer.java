package it.units.heckmeck;

import Heckmeck.Components.Player;
import Heckmeck.Components.Tile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;


public class TestPlayer {

    @ParameterizedTest
    @CsvSource(value = {"Luigi, Luigi", "Mario, Mario", "Sara, Sara"})
    void check_player_initialization(String passedName, String expectedName) {
        Player player = Player.generatePlayer(0);
        player.setPlayerName(passedName);

        Assertions.assertAll(
                () -> Assertions.assertEquals(expectedName, player.getName()),
                () -> Assertions.assertFalse(player.hasTile())
        );
    }

    @ParameterizedTest
    @MethodSource("playerNamesWithWhiteCharactersAndTheirCleanedUpVersions")
    void check_player_name_with_white_characters_are_correctly_cleaned_up(String passedName, String expectedName) {
        Player player = Player.generatePlayer(0);
        player.setPlayerName(passedName);

        Assertions.assertEquals(expectedName, player.getName());
    }

    static Stream<Arguments> playerNamesWithWhiteCharactersAndTheirCleanedUpVersions() {
        return Stream.of(
                Arguments.of("Luigi    \t\n", "Luigi"),
                Arguments.of("\t\n     Luigi", "Luigi"),
                Arguments.of("\tLu\tigi\t", "Lu    igi"),
                Arguments.of("Lu igi", "Lu igi")
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {21, 22, 23, 24})
    void player_add_tile(int tileNumber) {
        Player player = Player.generatePlayer(0);
        Tile desiredTile = Tile.generateTile(tileNumber);
        player.pickTile(desiredTile);
        Assertions.assertEquals(desiredTile, player.getLastPickedTile());
    }

    @Test
    void check_player_pick_one_tile_from_another_player() {
        Player robbed = Player.generatePlayer(0);
        Player robber = Player.generatePlayer(1);
        Tile desiredTile = Tile.generateTile(21);
        robbed.pickTile(desiredTile);
        robber.stealTileFromPlayer(robbed);

        Assertions.assertEquals(desiredTile, robber.getLastPickedTile());
    }

    @Test
    void check_cannot_steal_from_an_empty_player_stack(){
        Player player = Player.generatePlayer(0);
        int score = 30;
        Assertions.assertFalse(player.canStealTile(score));
    }


    @ParameterizedTest
    @ValueSource(ints = {21, 23, 24, 25, 30})
    void check_cannot_steal_tile_with_not_equal_score(int score){
        Player robbedPlayer = Player.generatePlayer(1);
        robbedPlayer.pickTile(Tile.generateTile(22));
        Assertions.assertFalse(robbedPlayer.canStealTile(score));
    }

    @Test
    void check_correctness_of_last_picked_tile_info_while_has_no_tiles() {
        Player player = Player.generatePlayer(0);
        String expectedInfo = "No tiles";

        String actualInfo = player.getTopTileInfo();

        Assertions.assertEquals(expectedInfo, actualInfo);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', textBlock = """
            #--------------------------
            # tileNumber | expectedInfo
            #--------------------------
                21       |  21 ~
            #--------------------------
                25       |  25 ~~
            #--------------------------
                29       |  29 ~~~
            #--------------------------
                33       |  33 ~~~~
            """)
    void check_correctness_of_last_picked_tile_info_while_has_tiles(int tileNumber, String expectedInfo) {
        Player player = Player.generatePlayer(0);

        player.pickTile(Tile.generateTile(tileNumber));
        String actualInfo = player.getTopTileInfo();

        Assertions.assertEquals(expectedInfo, actualInfo);
    }

    @Test
    void check_total_worms_collected() {
        Player player = Player.generatePlayer(0);
        Tile tile21 = Tile.generateTile(21);
        Tile tile25 = Tile.generateTile(25);
        Tile tile29 = Tile.generateTile(29);
        Tile tile33 = Tile.generateTile(33);
        int expectedWorms = 10;

        player.pickTile(tile21);
        player.pickTile(tile25);
        player.pickTile(tile29);
        player.pickTile(tile33);
        int actualWorms = player.getWormScore();

        Assertions.assertEquals(expectedWorms, actualWorms);
    }

}
