package Heckmeck;

import java.io.IOException;

public interface OutputHandler {

    public void showDice(Dice dice) throws IOException;
    public void showTiles(Tiles tiles) throws IOException;
    public void showPlayerTile(Player player);
    public void showPlayerData(Player player, Dice dice) throws IOException;
    public void showMenu();
    public void showSetPlayerName();
    public void showDiceChoice();
    public void showWantToPick();
    public void showBustMessage();
    public void showPlayerScore(Player actualPlayer, Dice dice);
    public void showTurnBeginConfirm();
}
