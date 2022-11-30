package Heckmeck;

import java.util.Objects;

public class Player {

    private final String playerName;
    private StackOfTiles playerTiles;

    private Player(String playerName){
        this.playerName = playerName;
        this.playerTiles = StackOfTiles.generate();
    }

    public int getWormNumber(){
        return playerTiles.computeScore();
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

    public void addTile(Tile tile){
        playerTiles.add(tile);
    }

     public void pickTileFromBoard(Tile desiredTile, BoardTiles board) {
        board.remove(desiredTile);
        playerTiles.add(desiredTile);
     }

    public Tile getLastPickedTile() {
        return playerTiles.peekLast();
    }

    public void pickTileFromPlayer(Tile desiredTile, Player robbedPlayer) {
        // Should throw an exception if it is not possible to steal tile
        if (canStealTileFrom(desiredTile, robbedPlayer)) {
            stealTileFromPlayer(robbedPlayer);
        }
    }

    private static boolean canStealTileFrom(Tile desiredTile, Player robbedPlayer) {
        return robbedPlayer.hasTile() && robbedPlayer.getLastPickedTile().equals(desiredTile);
    }

    private void stealTileFromPlayer(Player robbedPlayer) {
        playerTiles.add(robbedPlayer.getLastPickedTile());
    }

    public void removeLastPickedTile() {
        playerTiles.removeLast();
    }
}
