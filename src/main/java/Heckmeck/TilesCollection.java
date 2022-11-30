package Heckmeck;

public interface TilesCollection {

    boolean isEmpty();

    boolean hasTile(Tile tile);

    void add(Tile newTile);

    void remove(Tile newTile);

    Tile removeLast();

}
