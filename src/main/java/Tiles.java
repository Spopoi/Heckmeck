import java.util.*;

public class Tiles {

    public static final int numberOfTiles = 16;
    private List<Tile> tiles;

    public Tiles(List<Tile> tiles){
        this.tiles = tiles;
    }
    public static Tiles init(){
        List<Tile> tiles = new ArrayList<>();
        tiles.add(Tile.generateTile(21));
        tiles.add(Tile.generateTile(22));
        tiles.add(Tile.generateTile(23));
        tiles.add(Tile.generateTile(24));
        tiles.add(Tile.generateTile(25));
        tiles.add(Tile.generateTile(26));
        tiles.add(Tile.generateTile(27));
        tiles.add(Tile.generateTile(28));
        tiles.add(Tile.generateTile(29));
        tiles.add(Tile.generateTile(30));
        tiles.add(Tile.generateTile(31));
        tiles.add(Tile.generateTile(32));
        tiles.add(Tile.generateTile(33));
        tiles.add(Tile.generateTile(34));
        tiles.add(Tile.generateTile(35));
        tiles.add(Tile.generateTile(36));
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
