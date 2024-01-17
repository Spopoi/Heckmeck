package Heckmeck.Components;


import java.util.*;
import java.util.stream.IntStream;

public record BoardTiles(TreeSet<Tile> tiles) implements TilesCollection {
    public static BoardTiles init() {
        List<Tile> tiles = IntStream.range(Tile.tileMinNumber, Tile.tileMaxNumber + 1).mapToObj(Tile::generateTile).toList();
        return new BoardTiles(new TreeSet<>(tiles));
    }

    @Override
    public boolean hasElement() {
        return !tiles.isEmpty();
    }

    public Tile nearestTile(int score) {
        Tile searchedTile = Tile.generateTile(score);
        return tiles.floor(searchedTile);
    }

    @Override
    public void add(Tile newTile) {
        tiles.add(newTile);
    }

    @Override
    public void remove(Tile tileToRemove) {
        tiles.remove(tileToRemove);
    }

    @Override
    public void removeLastTile() {
        tiles.remove(tiles.last());
    }
}
