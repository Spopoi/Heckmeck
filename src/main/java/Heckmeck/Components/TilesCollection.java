package Heckmeck.Components;

public interface TilesCollection {
    boolean hasElement();
    void add(Tile newTile);
    void remove(Tile newTile);
    void removeLastTile();
}
