package Heckmeck.Components;


import java.util.Comparator;
import java.util.LinkedList;


public class StackOfTiles implements TilesCollection {

    private final LinkedList<Tile> stackOfTiles;

    private StackOfTiles(){
        this.stackOfTiles = new LinkedList<Tile>();
    }

    static StackOfTiles generate(){
        return new StackOfTiles();
    }

    @Override
    public boolean hasElement(){
        return !stackOfTiles.isEmpty();
    }

    public Tile getLastTile(){
        return stackOfTiles.peekLast();  // exception-safe
    }

    @Override
    public void add(Tile newTile) {
        stackOfTiles.add(newTile);
    }

    @Override
    public void remove(Tile tile) {
        stackOfTiles.pollLast();
    }

    @Override
    public void removeLastTile() {
        stackOfTiles.pollLast();
    }

    int getTotalNumberOfWorms() {
        return stackOfTiles.stream()
                .mapToInt(Tile::getWorms)
                .sum();
    }

    public Tile getTheOneWithTheHighestNumber() {
        return stackOfTiles.stream()
                .max(Comparator.comparingInt(Tile::number))
                .orElse(null);
    }

}
