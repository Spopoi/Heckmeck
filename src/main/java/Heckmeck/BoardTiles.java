package Heckmeck;

import exception.IllegalTileSelection;

import java.util.*;
import java.util.stream.IntStream;

public class BoardTiles implements TilesCollection {

    public static final int numberOfTiles = 16;
    //private List<Heckmeck.Tile> tiles;
    private TreeSet<Tile> tiles;

    public BoardTiles(TreeSet<Tile> tiles){
        this.tiles = tiles;
    }
    public static BoardTiles init(){
        List<Tile> tiles = IntStream.range(Tile.tileMinNumber, Tile.tileMaxNumber+1).mapToObj(Tile::generateTile).toList();
        return new BoardTiles(new TreeSet<Tile>(tiles));
    }

    public TreeSet<Tile> getTiles(){
        return tiles;
    }
    public List<Tile> getTilesList(){
        return tiles.stream().toList();
    }

    @Override
    public boolean isEmpty() {
        return tiles.isEmpty();
    }

    @Override
    public boolean hasTile(Tile tile) {
        return tiles.contains(tile);
    }

    @Override
    public void add(Tile newTile) {
        tiles.add(newTile);
        //tiles.sort(Comparator.comparingInt(Heckmeck.Tile::getNumber));
    }

    @Override
    public void remove(Tile newTile) {
        if (!tiles.remove(newTile)) {
            throw new IllegalTileSelection("Can not remove tile " + newTile.getNumber() + ", it is not present in the board");
        };
    }

    @Override
    public Tile removeLast() {
        return tiles.pollLast();
    }

    public void bust() {
        tiles.remove(tiles.last());
    }

    public Tile getMinValueTile(){
        return getTilesList().get(0);
    }
    public int size(){
        return tiles.size();
    }
}
