package it.units.heckmeck;

import Utils.FileReader;
import Heckmeck.Components.Tile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URL;
import java.nio.file.Path;
import java.util.Map;

public class TestTile {

    @ParameterizedTest
    @ValueSource(ints = {21, 22, 23, 24, 28, 32, 36})
    void convertTileIntoItsTextRepresentation(int tileNumber) throws Exception {
        URL tilesAsTextResource = TestTile.class.getClassLoader().getResource("MULTIPLE_TILES_MAP");
        Map<Integer, String> tileNumberToItsTextRepresentation = FileReader.
                readTilesFromSingleJson(Path.of(tilesAsTextResource.toURI()));
        Tile tileToTest = Tile.generateTile(tileNumber);

        String expectedTextRepresentation = tileNumberToItsTextRepresentation.get(tileNumber);
        String actualTextRepresentation = tileToTest.toString();

        Assertions.assertEquals(expectedTextRepresentation, actualTextRepresentation);
    }

}
