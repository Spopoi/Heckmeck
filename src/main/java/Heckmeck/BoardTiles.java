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

    @Override
    public String toString() {
        int maxNumberOfLines = getShortestTileHeight();

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < maxNumberOfLines; i++) {
            for (var tile : tiles) {
                result.append(tile.toString().split("\n")[i]);
                result.append(" ");
            }
            result.append("\n");
        }

        return result.toString();  // depends if we want or not the ending \n
    }

    private int getShortestTileHeight() {
        return tiles.stream()
                .map(Tile::toString)
                .mapToInt(tilesAsText -> (int) tilesAsText.lines().count())
                .min()
                .orElse(0);
    }

    public boolean allTilesHaveSameHeight() {
        int numberOfDifferentHeights = (int) tiles.stream()
                .map(Tile::toString)
                .mapToInt(tilesAsText -> (int) tilesAsText.lines().count())
                .distinct()
                .count();
        return numberOfDifferentHeights == 1;
    }
}
