package it.units.heckmeck;

import Heckmeck.FileReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Path;
import java.util.List;

public class TestFileReader {

    @Test
    void readsSingleTileStringRepresentation() throws Exception {
        URL tile21Resource = TestFileReader.class.getClassLoader().getResource("TILE_21");
        String expectedTile21AsText = """
                .------.
                |  21  |
                |  ~   |
                |      |
                '------'
                """;

        String tileAsText = FileReader.readFile(Path.of(tile21Resource.toURI()));

        Assertions.assertEquals(expectedTile21AsText, tileAsText);
    }

    @Test
    void name() throws Exception {
        URL multipleTilesResource = TestFileReader.class.getClassLoader().getResource("MULTIPLE_TILES");
        List<String> expectedTilesAsText = List.of(
                """
                        .------.
                        |  21  |
                        |  ~   |
                        |      |
                        '------'
                        """,
                """
                        .------.
                        |  21  |
                        |  ~   |
                        |      |
                        '------'
                        """
        );

        List<String> tilesAsText = FileReader.readMultipleTilesFromFile(Path.of(multipleTilesResource.toURI()));

        Assertions.assertEquals(expectedTilesAsText, tilesAsText);
    }
}
