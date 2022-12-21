package Heckmeck;

import exception.IllegalTileAddition;

public interface TilesCollection {

    boolean isEmpty();

    boolean hasTile(Tile tile);

    void add(Tile newTile) throws IllegalTileAddition;

    void remove(Tile newTile);

    void removeLast();

}
