package Heckmeck.Components;

import Utils.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;

import static java.util.Map.entry;

public record Tile(int number) implements Comparable<Tile> {
    private final static String RESOURCE_FILE = "CLI/TILES";
    public final static int tileMinNumber = 21;
    public final static int tileMaxNumber = 36;

    private static final Map<Integer, Integer> numberToWorms = Map.ofEntries(
            entry(21,1),
            entry(22,1),
            entry(23,1),
            entry(24,1),
            entry(25,2),
            entry(26,2),
            entry(27,2),
            entry(28,2),
            entry(29,3),
            entry(30,3),
            entry(31,3),
            entry(32,3),
            entry(33,4),
            entry(34,4),
            entry(35,4),
            entry(36,4)
    );

    private static final Map<Integer, String> numberToTilesAsText = FileReader.readTilesFromSingleJson(
            getResourcePath()
    );

    private static Path getResourcePath() {
        URL tilesResource = Tile.class.getClassLoader().getResource(RESOURCE_FILE);
        Path resourcePath = null;
        try {
            resourcePath = Path.of(tilesResource.toURI());
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
        return resourcePath;
    }

    public int getWorms() {
        return numberToWorms.get(number);
    }

    public String asShortSingleRowString() {
        return number() + " " + "~".repeat(this.getWorms());
    }

    public static Tile generateTile(int number) {
        return new Tile(number);
    }

    @Override
    public int compareTo(Tile other) {
        return Integer.compare(this.number, other.number);
    }

    @Override
    public String toString() {
        return numberToTilesAsText.get(this.number);
    }
}
