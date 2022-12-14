package Heckmeck;

import java.io.IOException;

public interface OutputHandler {

    public void showDice(Dice dice);
    public void showTiles(BoardTiles boardTiles);
    public void showPlayerData(Player player, Dice dice);
    public void showWelcomeMessage();
    public void askForNumberOfPlayers();
    public void showSetPlayerName(int playerNumber);
    public void showDiceChoice();
    public void showExceptionMessage(Exception ex);
    public void showWantToPick();
    public void showBustMessage();
    public void showPlayerScore(Player actualPlayer, Dice dice);
    public void showTurnBeginConfirm(Player actualPlayer);
    public void showWantToSteal(Player robbedPlayer);
    public void showWinnerPlayerMessage(Player winnerPlayer);
    public void showAlreadyPickedDice();
    public void showFaceNotPresentMessage();
    public void showAlreadyPickedName();

    public void showWantToPlay();
}
