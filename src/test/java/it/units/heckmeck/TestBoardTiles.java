package it.units.heckmeck;

import Heckmeck.Tile;
import Heckmeck.BoardTiles;
import exception.IllegalTileAddition;
import exception.IllegalTileNumber;
import exception.IllegalTileSelection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

public class TestBoardTiles {

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
        BoardTiles boardTiles = BoardTiles.init();
        TreeSet<Tile> expectedTiles = setupTiles();
        Assertions.assertEquals(boardTiles.getTiles(), expectedTiles);
    }

    @Test
    void add_tile(){
        Tile newTile = Tile.generateTile(26);

        BoardTiles boardTiles = BoardTiles.init();
        boardTiles.add(newTile);

        TreeSet<Tile> expectedTiles = setupTiles();
        expectedTiles.add(newTile);
        Assertions.assertEquals(boardTiles.getTiles(), expectedTiles);
    }

    @Test
    void remove_first_tile(){
        BoardTiles boardTiles = BoardTiles.init();
        Tile firstTile = Tile.generateTile(21);
        boardTiles.remove(firstTile);

        TreeSet<Tile> expectedTiles = setupTiles();
        expectedTiles.remove(expectedTiles.first());

        Assertions.assertEquals(boardTiles.getTiles(), expectedTiles);
    }

    @Test
    void check_that_remove_a_tile_alredy_removed_throws_exception(){
        BoardTiles boardTiles = BoardTiles.init();
        Tile tileToRemove = Tile.generateTile(21);
        String expectedMessage = "Can not remove tile 21, it is not present in the board";

        boardTiles.remove(tileToRemove);

        Exception ex = Assertions.assertThrows(IllegalTileSelection.class, () ->
                boardTiles.remove(tileToRemove));
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    void check_that_adding_twice_the_same_tile_throws_exception(){
        BoardTiles boardTiles = BoardTiles.init();
        Tile tileToAdd = Tile.generateTile(21);
        String expectedMessage = "Tile number 21 is already present in the collection";

        Exception ex = Assertions.assertThrows(IllegalTileAddition.class, () ->
                boardTiles.add(tileToAdd));
        Assertions.assertEquals(expectedMessage, ex.getMessage());
    }

    //to refactor
    private TreeSet<Tile> setupTiles() {
        TreeSet<Tile> expected = new TreeSet<>();
        for (int tileNumber = 21; tileNumber < 21 + BoardTiles.numberOfTiles ; tileNumber++) {
            if(tileNumber < 25) expected.add(Tile.generateTile(tileNumber));
            else if( tileNumber < 29) expected.add(Tile.generateTile(tileNumber));
            else expected.add(Tile.generateTile(tileNumber));
        }
        return expected;
    }

    @Test
    void check_order(){
        BoardTiles boardTiles = BoardTiles.init();
        List<Tile> listTiles = boardTiles.getTiles().stream().toList();
        boolean correctAscendantOrder = true;
        for (int i = 0; i < BoardTiles.numberOfTiles - 1; i++) {
            if (listTiles.get(i).getNumber() > listTiles.get(i + 1).getNumber()) {
                correctAscendantOrder = false;
                break;
            }
        }
        assertTrue(correctAscendantOrder);
    }

    @Test
    void check_order_after_adding_one_tile(){
        BoardTiles boardTiles = BoardTiles.init();
        boardTiles.add(Tile.generateTile(28));
        List<Tile> listTiles = boardTiles.getTiles().stream().toList();
        boolean correctAscendantOrder = true;
        for (int i = 0; i < BoardTiles.numberOfTiles - 1; i++) {
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
        BoardTiles boardTiles = BoardTiles.init();
        boardTiles.bust();
        TreeSet<Tile> tilesList = boardTiles.getTiles();
        Tile expectedBustedTile = Tile.generateTile(36);
        assertFalse(tilesList.contains(expectedBustedTile));
    }

}
