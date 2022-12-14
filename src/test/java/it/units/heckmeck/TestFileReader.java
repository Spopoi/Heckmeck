package it.units.heckmeck;

import Heckmeck.FileReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Path;

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

        String tileAsString = FileReader.readFile(Path.of(tile21Resource.toURI()));

        Assertions.assertEquals(expectedTile21AsText, tileAsString);
    }
}
