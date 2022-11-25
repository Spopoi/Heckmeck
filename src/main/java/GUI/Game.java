package GUI;

import Heckmeck.Player;

public class Game {
    private Player player1;
    private Player player2;
    private ActionLog actionLog;

    public Game(Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
    }

    public static Game newGame(Player player1, Player player2){
        return new Game(player1, player2);
    }
}
