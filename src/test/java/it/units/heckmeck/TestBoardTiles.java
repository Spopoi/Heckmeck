package it.units.heckmeck;

import Heckmeck.Components.Tile;
import Heckmeck.Components.BoardTiles;
import Heckmeck.HeckmeckRules;
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
                () -> Assertions.assertEquals(21, tile.number()),
                () -> Assertions.assertEquals(1, tile.getWorms())
        );
    }

    @Test
    void generate_tile(){
        Tile expected = new Tile(21);
        Assertions.assertEquals(expected, Tile.generateTile(21));
    }

    @Test
    void check_tiles_initialization(){
        BoardTiles boardTiles = BoardTiles.init();
        TreeSet<Tile> expectedTiles = setupTiles();
        Assertions.assertEquals(boardTiles.tiles(), expectedTiles);
    }

    @Test
    void add_tile(){
        Tile tileToTest = Tile.generateTile(26);

        BoardTiles boardTiles = BoardTiles.init();
        boardTiles.remove(tileToTest);
        boardTiles.add(tileToTest);

        TreeSet<Tile> expectedTiles = setupTiles();
        expectedTiles.add(tileToTest);
        Assertions.assertEquals(boardTiles.tiles(), expectedTiles);
    }

    @Test
    void remove_first_tile(){
        BoardTiles boardTiles = BoardTiles.init();
        Tile firstTile = Tile.generateTile(21);
        boardTiles.remove(firstTile);

        TreeSet<Tile> expectedTiles = setupTiles();
        expectedTiles.remove(expectedTiles.first());

        Assertions.assertEquals(boardTiles.tiles(), expectedTiles);
    }

    private TreeSet<Tile> setupTiles() {
        TreeSet<Tile> expected = new TreeSet<>();
        for (int tileNumber = 21; tileNumber < 21 + HeckmeckRules.NUMBER_OF_TILES; tileNumber++) {
            if(tileNumber < 25) expected.add(Tile.generateTile(tileNumber));
            else if( tileNumber < 29) expected.add(Tile.generateTile(tileNumber));
            else expected.add(Tile.generateTile(tileNumber));
        }
        return expected;
    }

    @Test
    void check_order(){
        BoardTiles boardTiles = BoardTiles.init();
        List<Tile> listTiles = boardTiles.tiles().stream().toList();
        boolean correctAscendantOrder = true;
        for (int i = 0; i < HeckmeckRules.NUMBER_OF_TILES - 1; i++) {
            if (listTiles.get(i).number() > listTiles.get(i + 1).number()) {
                correctAscendantOrder = false;
                break;
            }
        }
        assertTrue(correctAscendantOrder);
    }

    @Test
    void check_order_after_adding_one_tile(){
        BoardTiles boardTiles = BoardTiles.init();
        Tile tileToTest = Tile.generateTile(28);

        boardTiles.remove(tileToTest);
        boardTiles.add(tileToTest);
        List<Tile> listTiles = boardTiles.tiles().stream().toList();
        boolean correctAscendantOrder = true;
        for (int i = 0; i < HeckmeckRules.NUMBER_OF_TILES - 1; i++) {
            if (listTiles.get(i).number() > listTiles.get(i + 1).number()) {
                correctAscendantOrder = false;
                break;
            }
        }

        assertTrue(correctAscendantOrder);
    }


    @Test
    void check_bust(){
        BoardTiles boardTiles = BoardTiles.init();
        boardTiles.removeLastTile();
        TreeSet<Tile> tilesList = boardTiles.tiles();
        Tile expectedBustedTile = Tile.generateTile(36);
        assertFalse(tilesList.contains(expectedBustedTile));
    }

}
