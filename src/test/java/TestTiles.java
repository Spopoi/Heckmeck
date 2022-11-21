import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

public class TestTiles {

    @Test
    void init_tile_number_21_with_2_worms(){
        Tile tile = new Tile(21);
        assertAll(
                () -> assertEquals(21, tile.getNumber()),
                () -> assertEquals(1, tile.getWorms())
        );
    }

    @Test
    void generate_tile(){
        Tile expected = new Tile(21);
        assertEquals(expected, Tile.generateTile(21));
    }

    @Test
    void check_tiles_initialization(){
        Tiles tiles = Tiles.init();
        TreeSet<Tile> expectedTiles = setupTiles();
        assertEquals(tiles.getTiles(), expectedTiles);
    }

    @Test
    void add_tile(){
        Tile newTile = Tile.generateTile(26);

        Tiles tiles = Tiles.init();
        tiles.add(newTile);

        TreeSet<Tile> expectedTiles = setupTiles();
        expectedTiles.add(newTile);
        assertEquals(tiles.getTiles(), expectedTiles);
    }

    @Test
    void remove_first_tile(){
        Tiles tiles = Tiles.init();
        Tile firstTile = Tile.generateTile(21);
        tiles.remove(firstTile);

        TreeSet<Tile> expectedTiles = setupTiles();
        expectedTiles.remove(expectedTiles.first());

        assertEquals(tiles.getTiles(), expectedTiles);
    }

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
