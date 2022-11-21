package Heckmeck;

import java.util.*;
import java.util.stream.IntStream;

public class Tiles {

    public static final int numberOfTiles = 16;
    //private List<Heckmeck.Tile> tiles;
    private TreeSet<Tile> tiles;

    public Tiles(TreeSet<Tile> tiles){
        this.tiles = tiles;
    }
    public static Tiles init(){
        List<Tile> tiles = IntStream.range(21, 37).mapToObj(Tile::generateTile).toList();
        return new Tiles(new TreeSet<Tile>(tiles));
    }

    public TreeSet<Tile> getTiles(){
        return tiles;
    }
    public List<Tile> getTilesList(){
        return tiles.stream().toList();
    }

    public void add(Tile newTile) {
        tiles.add(newTile);
        //tiles.sort(Comparator.comparingInt(Heckmeck.Tile::getNumber));
    }

    public void remove(Tile newTile) {
        tiles.remove(newTile);
    }

    public void bust() {
        tiles.remove(tiles.last());
    }
}
