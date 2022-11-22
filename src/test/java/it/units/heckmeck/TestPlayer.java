package it.units.heckmeck;

import Heckmeck.Player;
import org.junit.jupiter.api.Test;
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
}
