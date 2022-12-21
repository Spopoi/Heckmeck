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

    public Tile getLastTile(){
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
    public void remove(Tile tile) {
        stackOfTiles.pollLast();
    }

    @Override
    public void removeLastTile() {
        stackOfTiles.pollLast();
    }

    public LinkedList<Tile> getTileList(){
        return stackOfTiles;
    }

}