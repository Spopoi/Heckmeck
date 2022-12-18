package Heckmeck;

import exception.IllegalTileTheft;

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

    public boolean canStealFrom(Player robbedPlayer, int playerScore){
        if(!robbedPlayer.hasTile()) return false;
        else return robbedPlayer.getLastPickedTileNumber() == playerScore;
    }


    public void stealTileFromPlayer(Player robbedPlayer) {
        playerTiles.add(robbedPlayer.getLastPickedTile());
        robbedPlayer.removeLastPickedTile();
    }

    public void removeLastPickedTile() {
        playerTiles.removeLast();
    }

    public int getLastPickedTileNumber(){ // TODO Spostare sopra controllo hasTile
        if (hasTile()){
            return getLastPickedTile().getNumber();
        }
        else return 0;
    }

    public String getLastPickedTileWormString(){ // TODO Spostare sopra controllo hasTile
        if (hasTile()){
            return  getLastPickedTile().getWormString();
        }
        else return "";
    }

    public int getNumberOfPlayerTile(){
        return playerTiles.size();
    }

    public int getHighestTileNumber(){
        return playerTiles.highestTileNumber();
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
