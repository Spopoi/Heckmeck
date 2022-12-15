package it.units.heckmeck;

import Heckmeck.FileReader;
import Heckmeck.Tile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.nio.file.Path;

public class TestTile {

    @Test
    void convertTile21IntoAString() throws Exception {
        Tile tileToTest = Tile.generateTile(21);
        URL tile21Resource = Tile.class.getClassLoader().getResource("TILE_21");
        String expectedString = FileReader.readFile(Path.of(tile21Resource.toURI()));

        Assertions.assertEquals(expectedString, tileToTest.toString());
    }

    @Test
    void convertTile22IntoAString() throws Exception {
        Tile tileToTest = Tile.generateTile(22);
        URL tile21Resource = Tile.class.getClassLoader().getResource("TILE_22");
        String expectedString = FileReader.readFile(Path.of(tile21Resource.toURI()));

        Assertions.assertEquals(expectedString, tileToTest.toString());
    }

    @Test
    void convertTile23IntoAString() throws Exception {
        Tile tileToTest = Tile.generateTile(23);
        URL tile21Resource = Tile.class.getClassLoader().getResource("TILE_23");
        String expectedString = FileReader.readFile(Path.of(tile21Resource.toURI()));

        Assertions.assertEquals(expectedString, tileToTest.toString());
    }
}
