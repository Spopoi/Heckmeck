package it.units.heckmeck;

import Heckmeck.Tile;
import Heckmeck.Tiles;
import exception.IllegalTileNumber;
import exception.IllegalTileSelection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

public class TestTiles {

    @Test
    void init_tile_number_21_with_2_worms(){
        Tile tile = new Tile(21);
        assertAll(
                () -> Assertions.assertEquals(21, tile.getNumber()),
                () -> Assertions.assertEquals(1, tile.getWorms())
        );
    }

    @Test
    void generate_tile(){
        Tile expected = new Tile(21);
        Assertions.assertEquals(expected, Tile.generateTile(21));
    }

    @Test
    void check_that_is_not_possible_to_create_a_tile_that_is_not_present_in_the_game(){
        String expectedMessage = "Tile numbers must be between 21 and 36 included. 40 has been given";

        Exception ex = Assertions.assertThrows(IllegalTileNumber.class, () -> Tile.generateTile(40));
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    void check_tiles_initialization(){
        Tiles tiles = Tiles.init();
        TreeSet<Tile> expectedTiles = setupTiles();
        Assertions.assertEquals(tiles.getTiles(), expectedTiles);
    }

    @Test
    void add_tile(){
        Tile newTile = Tile.generateTile(26);

        Tiles tiles = Tiles.init();
        tiles.add(newTile);

        TreeSet<Tile> expectedTiles = setupTiles();
        expectedTiles.add(newTile);
        Assertions.assertEquals(tiles.getTiles(), expectedTiles);
    }

    @Test
    void remove_first_tile(){
        Tiles tiles = Tiles.init();
        Tile firstTile = Tile.generateTile(21);
        tiles.remove(firstTile);

        TreeSet<Tile> expectedTiles = setupTiles();
        expectedTiles.remove(expectedTiles.first());

        Assertions.assertEquals(tiles.getTiles(), expectedTiles);
    }

    @Test
    void check_that_is_not_possible_to_remove_a_tile_alredy_removed(){
        Tiles tiles = Tiles.init();
        Tile tileToRemove = Tile.generateTile(21);
        String expectedMessage = "Can not remove tile 21, it is not present in the board";

        tiles.remove(tileToRemove);

        Exception ex = Assertions.assertThrows(IllegalTileSelection.class, () ->
                tiles.remove(tileToRemove));
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    //to refactor
    private TreeSet<Tile> setupTiles() {
        TreeSet<Tile> expected = new TreeSet<>();
        for (int tileNumber = 21; tileNumber < 21 + Tiles.numberOfTiles ; tileNumber++) {
            if(tileNumber < 25) expected.add(Tile.generateTile(tileNumber));
            else if( tileNumber < 29) expected.add(Tile.generateTile(tileNumber));
            else expected.add(Tile.generateTile(tileNumber));
        }
        return expected;
    }

    @Test
    void check_order(){
        Tiles tiles = Tiles.init();
        List<Tile> listTiles = tiles.getTiles().stream().toList();
        boolean correctAscendantOrder = true;
        for (int i = 0; i < Tiles.numberOfTiles - 1; i++) {
            if (listTiles.get(i).getNumber() > listTiles.get(i + 1).getNumber()) {
                correctAscendantOrder = false;
                break;
            }
        }
        assertTrue(correctAscendantOrder);
    }

    @Test
    void check_order_after_adding_one_tile(){
        Tiles tiles = Tiles.init();
        tiles.add(Tile.generateTile(28));
        List<Tile> listTiles = tiles.getTiles().stream().toList();
        boolean correctAscendantOrder = true;
        for (int i = 0; i < Tiles.numberOfTiles - 1; i++) {
            if (listTiles.get(i).getNumber() > listTiles.get(i + 1).getNumber()) {
                correctAscendantOrder = false;
                break;
            }
        }
        assertTrue(correctAscendantOrder);
    }


    //getter ultimo elemento e check non ci sia piu'
    @Test
    void check_bust(){
        Tiles tiles = Tiles.init();
        tiles.bust();
        TreeSet<Tile> tilesList = tiles.getTiles();
        Tile expectedBustedTile = Tile.generateTile(36);
        assertFalse(tilesList.contains(expectedBustedTile));
    }

}
