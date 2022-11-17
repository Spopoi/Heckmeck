import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestTiles {

    @Test
    void init_tile_number_21_with_2_worms(){
        Tile tile = new Tile(21,2);
        assertAll(
                () -> assertEquals(21, tile.number()),
                () -> assertEquals(2, tile.worms())
        );
    }

    @Test
    void test_generate_tile(){
        Tile expected = new Tile(21,2);
        assertEquals(expected, Tile.generateTile(21,2));
    }

}
