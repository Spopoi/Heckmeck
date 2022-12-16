package it.units.heckmeck;

import Heckmeck.FileReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

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
    void readsMultipleTileStringRepresentation() throws Exception {
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
                        |  22  |
                        |  ~   |
                        |      |
                        '------'
                        """,
                """
                        .------.
                        |  23  |
                        |  ~   |
                        |      |
                        '------'
                        """
        );

        List<String> tilesAsText = FileReader.readMultipleTilesFromFile(Path.of(multipleTilesResource.toURI()));

        Assertions.assertEquals(expectedTilesAsText, tilesAsText);
    }

    @Test
    void readsIntegerToStringMapFromJson() throws URISyntaxException {
        URL multipleTilesMapResource = TestFileReader.class.getClassLoader().getResource("MULTIPLE_TILES_MAP");
        Map<Integer, String> obtainedMap;
        Map<Integer, String> expectedMap = Map.ofEntries(
                entry(21, """
                        .------.
                        |  21  |
                        |  ~   |
                        |      |
                        '------'
                        """),
                entry(22, """
                        .------.
                        |  22  |
                        |  ~   |
                        |      |
                        '------'
                        """),
                entry(23, """
                        .------.
                        |  23  |
                        |  ~   |
                        |      |
                        '------'
                        """)
        );

        obtainedMap = FileReader.readTilesFromSingleJson(Path.of(multipleTilesMapResource.toURI()));

        // Not exactly as expected. Can we live well the same or not??
        // System.out.println(expectedMap.getClass() + " VS " + obtainedMap.getClass());
        Assertions.assertEquals(expectedMap, obtainedMap);
    }
}
