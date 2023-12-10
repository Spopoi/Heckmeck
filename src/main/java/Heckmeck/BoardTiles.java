package Heckmeck;


import java.util.*;
import java.util.stream.IntStream;

public class BoardTiles implements TilesCollection {
    public static final int numberOfTiles = 16;
    private TreeSet<Tile> tiles;

    public BoardTiles(TreeSet<Tile> tiles) {
        this.tiles = tiles;
    }
    public static BoardTiles init(){
        List<Tile> tiles = IntStream.range(Tile.tileMinNumber, Tile.tileMaxNumber+1).mapToObj(Tile::generateTile).toList();
        return new BoardTiles(new TreeSet<>(tiles));
    }

    public TreeSet<Tile> getTiles() {
        return tiles;
    }

    @Override
    public boolean isEmpty() {
        return tiles.isEmpty();
    }

    @Override
    public boolean hasTile(Tile tile) {
        return tiles.contains(tile);
    }

    public Tile nearestTile(int score){
        Tile searchedTile = Tile.generateTile(score);
        return tiles.floor(searchedTile);
    }

    @Override
    public void add(Tile newTile){
        tiles.add(newTile);
    }

    @Override
    public void remove(Tile tileToRemove){
        tiles.remove(tileToRemove);
    }

    @Override
    public void removeLastTile() {
        tiles.remove(tiles.last());
    }
}
