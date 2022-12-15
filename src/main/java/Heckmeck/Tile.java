package Heckmeck;

import exception.IllegalTileNumber;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Tile implements Comparable<Tile> {
    public final static int tileMinNumber = 21;
    public final static int tileMaxNumber = 36;
    private final int number;

    private static final Map<Integer, Integer> numberToWorms =
            Collections.unmodifiableMap(new HashMap<Integer, Integer>() {{
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
                put(33, 3);
                put(34, 3);
                put(35, 3);
                put(36, 3);
            }});
    public Tile(int number) { this.number=number; }

    public int getNumber(){
        return number;
    }
    public int getWorms(){
        return numberToWorms.get(number);
    }

    public String getWormString(){
        return "~".repeat(this.getWorms());
    }

    public static Tile generateTile(int number) throws IllegalTileNumber{
        if (number < 21 || number > 36) {
            throw new IllegalTileNumber(number);
        }
        return new Tile(number);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return number == tile.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }
    
    @Override
    public int compareTo(Tile other) {
        return Integer.compare(this.number, other.number);
    }

}
