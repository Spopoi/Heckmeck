package Heckmeck.Components;


public interface TilesCollection {

    boolean isEmpty();
    boolean hasTile(Tile tile);
    void add(Tile newTile);
    void remove(Tile newTile);
    //TODO: removing removeLastTile? we could sobstitute it with remove(getLast()) since it's used only one/two times
    void removeLastTile();

}
