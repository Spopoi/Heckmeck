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
}
