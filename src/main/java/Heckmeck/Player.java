package Heckmeck;

import java.util.LinkedList;

public class Player {

    private String playerName;
    private StackOfTiles playerTiles;
    private final int playerID;

    private Player(int playerID){
        this.playerID = playerID;
        this.playerTiles = StackOfTiles.generate();
    }
    public LinkedList<Tile> getPlayerTiles(){
        return playerTiles.getTileList();
    }

    public static Player generatePlayer(int playerID) {
        return new Player(playerID);
    }

    public String getName() {
        return playerName;
    }

    public boolean hasTile() {
        return !playerTiles.isEmpty();
    }

     public void pickTile(Tile desiredTile) {
        playerTiles.add(desiredTile);
     }

    public Tile getLastPickedTile() {
        return playerTiles.getLastTile();
    }

    public String getTopTileInfo() {
        if (hasTile()) {
            Tile topTile = getLastPickedTile();
            return topTile.getNumber() + " " + topTile.getWormString();
        } else {
            return "No tiles";
        }
    }

    public boolean canStealFrom(Player robbedPlayer, int playerScore){
        if(!robbedPlayer.hasTile()) return false;
        else return robbedPlayer.getLastPickedTile().getNumber() == playerScore;
    }

    public void stealTileFromPlayer(Player robbedPlayer) {
        playerTiles.add(robbedPlayer.getLastPickedTile());
        robbedPlayer.removeLastPickedTile();
    }

    public void removeLastPickedTile() {
        playerTiles.removeLastTile();
    }

    public int getPlayerID(){
        return playerID;
    }

    public void setPlayerName(String playerName) {
        this.playerName = fixWhiteCharacter(playerName);
    }

    private static String fixWhiteCharacter(String playerName) {
        return playerName.strip().replace("\t", "    ");
    }

    public int getWormScore() {
        return playerTiles.getTotalNumberOfWorms();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return playerName.equals(player.playerName);
    }

    @Override
    public int hashCode() {
        return playerName != null ? playerName.hashCode() : 0;
    }
}
