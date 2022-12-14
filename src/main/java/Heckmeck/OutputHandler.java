package Heckmeck;

import java.io.IOException;

public interface OutputHandler {

    public void printMessage(String message);
    public void showDice(Dice dice);
    public void showTiles(BoardTiles boardTiles);
    public void showPlayerData(Player player, Dice dice);
    public void showWelcomeMessage();
    public void showWantToPick();
    public void showBustMessage();
    public void showTurnBeginConfirm(String playerName);
    public void showWantToSteal(Player robbedPlayer);
    public void showWantToPlay();
}
