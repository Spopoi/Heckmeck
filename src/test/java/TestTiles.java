import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
        List<Tile> expectedTiles = setupTiles();
        assertEquals(tiles.getTiles(), expectedTiles);
    }

    @Test
    void add_tile(){
        Tiles tiles = Tiles.init();
        Tile newTile = Tile.generateTile(50);
        tiles.add(newTile);

        List<Tile> expectedTiles = setupTiles();
        expectedTiles.add(Tile.generateTile(50));

       assertEquals(tiles.getTiles(), expectedTiles);
    }

    @Test
    void remove_first_tile(){
        Tiles tiles = Tiles.init();
        Tile newTile = Tile.generateTile(21);
        tiles.remove(newTile);

        List<Tile> expectedTiles = setupTiles();
        expectedTiles.remove(0);

        assertEquals(tiles.getTiles(), expectedTiles);
    }


    //aaaaa
    private List<Tile> setupTiles() {
        List<Tile> expected = new ArrayList<>();
        int tileNumber;
        for (int i = 0; i < Tiles.numberOfTiles ; i++) {
            tileNumber = i + 21;
            if(tileNumber < 25) expected.add(Tile.generateTile(tileNumber));
            else if( tileNumber < 29) expected.add(Tile.generateTile(tileNumber));
            else expected.add(Tile.generateTile(tileNumber));
        }
        return expected;
    }

    @Test
    void check_order(){
        Tiles tiles = Tiles.init();
        List<Tile> listTiles = tiles.getTiles();
        boolean correctAscendantOrder = true;
        for (int i = 0; i < Tiles.numberOfTiles - 1; i++) {
            if(listTiles.get(i).getNumber() > listTiles.get(i+1).getNumber()) correctAscendantOrder = false;
        }
        assertTrue(correctAscendantOrder);
    }

    @Test
    void check_order_after_adding_one_tile(){
        Tiles tiles = Tiles.init();
        tiles.add(Tile.generateTile(20));
        List<Tile> listTiles = tiles.getTiles();
        boolean correctAscendantOrder = true;
        for (int i = 0; i < Tiles.numberOfTiles - 1; i++) {
            if(listTiles.get(i).getNumber() > listTiles.get(i+1).getNumber()) correctAscendantOrder = false;
        }
        assertTrue(correctAscendantOrder);
    }


    //getter ultimo elemento e check non ci sia piu'
    @Test
    void check_bust(){
        Tiles tiles = Tiles.init();
        tiles.bust();
        List<Tile> tilesList = tiles.getTiles();
        Tile expectedBustedTile = Tile.generateTile(3);
        assertFalse(tilesList.contains(expectedBustedTile));

    }

}
