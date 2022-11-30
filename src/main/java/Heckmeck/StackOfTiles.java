package Heckmeck;

import exception.IllegalTileAddition;

import java.util.LinkedList;
import java.util.Objects;

public class StackOfTiles implements TilesCollection {

    private LinkedList<Tile> stackOfTiles;

    private StackOfTiles(){
        this.stackOfTiles = new LinkedList<Tile>();
    }

    static StackOfTiles generate(){
        return new StackOfTiles();
    }

    @Override
    public boolean isEmpty(){
        return stackOfTiles.isEmpty();
    }

    Tile peekLast(){
        return stackOfTiles.peekLast();  // exception-safe
    }

    @Override
    public void add(Tile newTile) throws IllegalTileAddition{
        if (hasTile(newTile)) {
            throw new IllegalTileAddition(newTile.getNumber());
        }
        stackOfTiles.add(newTile);
    }

    @Override
    public boolean hasTile(Tile tile) {
        return stackOfTiles.contains(tile);
    }

    @Override
    public void remove(Tile tile){
        if (Objects.equals(peekLast(), tile)) {
            stackOfTiles.pollLast();
        } else {
            // TO DO: throw some kind of exception
            // fall here if: - no tiles are present
            //               - 'tile' does not match the last tile
        }
    }

    @Override
    public Tile removeLast() {
        return stackOfTiles.pollLast();
    }

    public int computeScore() {
        return stackOfTiles.stream()
                .mapToInt(Tile::getWorms)
                .sum();
    }

}