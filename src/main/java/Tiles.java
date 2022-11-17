import java.util.*;

public class Tiles {

    public static final int numberOfTiles = 16;
    private List<Tile> tiles;

    public Tiles(List<Tile> tiles){
        this.tiles = tiles;
    }
    public static Tiles init(){
        List<Tile> tiles = new ArrayList<>();
        tiles.add(Tile.generateTile(21,1));
        tiles.add(Tile.generateTile(22,1));
        tiles.add(Tile.generateTile(23,1));
        tiles.add(Tile.generateTile(24,1));
        tiles.add(Tile.generateTile(25,2));
        tiles.add(Tile.generateTile(26,2));
        tiles.add(Tile.generateTile(27,2));
        tiles.add(Tile.generateTile(28,2));
        tiles.add(Tile.generateTile(29,3));
        tiles.add(Tile.generateTile(30,3));
        tiles.add(Tile.generateTile(31,3));
        tiles.add(Tile.generateTile(32,3));
        tiles.add(Tile.generateTile(33,3));
        tiles.add(Tile.generateTile(34,3));
        tiles.add(Tile.generateTile(35,3));
        tiles.add(Tile.generateTile(36,3));
        return new Tiles(tiles);
    }

    public List<Tile> getTiles(){
        return tiles;
    }

    public void add(Tile newTile) {
        tiles.add(newTile);
        tiles.sort(Comparator.comparingInt(Tile::number));
    }

    public void remove(Tile newTile) {
        tiles.remove(newTile);
    }
}
