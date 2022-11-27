package Heckmeck;

import java.util.LinkedList;

public class Player {

    private final String playerName;
    private LinkedList<Tile> playerTiles;

    private Player(String playerName){
        this.playerName = playerName;
        this.playerTiles = new LinkedList<>();
    }

    public static Player generatePlayer(String playerName) {
        return playerName==null ? new Player("") : new Player(playerName);
    }

    public String getName() {
        return playerName;
    }

    public boolean hasTile() {
        return !playerTiles.isEmpty();
    }

     public void pickTileFromBoard(Tile desiredTile, Tiles board) {
        board.remove(desiredTile);
        playerTiles.add(desiredTile);
     }

    public Tile getLastPickedTile() {
        return hasTile() ? playerTiles.getLast() : null;
    }

    public boolean pickTileFromPlayer(Tile desiredTile, Player robbedPlayer) {
        if (canStealTileFrom(desiredTile, robbedPlayer)) {
            return stealTileFromPlayer(robbedPlayer);
        }
        return false;
    }

    private static boolean canStealTileFrom(Tile desiredTile, Player robbedPlayer) {
        return robbedPlayer.hasTile() && robbedPlayer.getLastPickedTile().equals(desiredTile);
    }

    private boolean stealTileFromPlayer(Player robbedPlayer) {
        return playerTiles.add(robbedPlayer.getLastPickedTile());
    }

    public void removeLastPickedTile() {
        if(playerTiles.size()>0) playerTiles.removeLast();
    }
}
