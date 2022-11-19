import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Tiles {

    public static final int numberOfTiles = 16;
    private List<Tile> tiles;

    public Tiles(List<Tile> tiles){
        this.tiles = tiles;
    }
    public static Tiles init(){
        List<Tile> tiles = IntStream.range(21,37).mapToObj(Tile::generateTile).collect(Collectors.toList());
        return new Tiles(tiles);
    }

    public List<Tile> getTiles(){
        return tiles;
    }

    public void add(Tile newTile) {
        tiles.add(newTile);
        tiles.sort(Comparator.comparingInt(Tile::getNumber));
    }

    public void remove(Tile newTile) {
        tiles.remove(newTile);
    }

    public void bust() {
        tiles.remove(tiles.size()-1);
    }
}
