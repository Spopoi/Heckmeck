package Heckmeck.Components;

import Utils.FileReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public record Tile(int number) implements Comparable<Tile> {
    private final static String RESOURCE_FILE = "CLI/TILES";
    public final static int tileMinNumber = 21;
    public final static int tileMaxNumber = 36;
    private static final Map<Integer, Integer> numberToWorms =
            Collections.unmodifiableMap(new HashMap<>() {{
                put(21, 1);
                put(22, 1);
                put(23, 1);
                put(24, 1);
                put(25, 2);
                put(26, 2);
                put(27, 2);
                put(28, 2);
                put(29, 3);
                put(30, 3);
                put(31, 3);
                put(32, 3);
                put(33, 4);
                put(34, 4);
                put(35, 4);
                put(36, 4);
            }});

    private static final Map<Integer, String> numberToTilesAsText = FileReader.readTilesFromSingleJson(
            getResourcePath()
    );

    private static Path getResourcePath() {
        URL tilesResource = Tile.class.getClassLoader().getResource(RESOURCE_FILE);
        Path resourcePath = null;
        try {
            resourcePath = Path.of(tilesResource.toURI());
        } catch (URISyntaxException ex) {
            System.out.println(ex);
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
