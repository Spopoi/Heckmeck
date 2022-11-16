import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTiles {

    @Test
    void init_tile_number_21_with_2_worms(){
        Tile tile = new Tile(21,2);
        assertAll(
                () -> assertEquals(21, tile.number()),
                () -> assertEquals(2, tile.worms())
        );
    }
}
