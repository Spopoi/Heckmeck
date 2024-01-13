package Heckmeck;

import Heckmeck.Components.BoardTiles;
import Heckmeck.Components.Dice;
import Heckmeck.Components.Die;
import Heckmeck.Components.Player;

public interface IOHandler {
    void printMessage(String message);
    void showTurnBeginConfirm(Player player);
    void showWelcomeMessage();
    int chooseNumberOfPlayers();
    String choosePlayerName(Player player);
    void showBoardTiles(BoardTiles boardTiles);
    void askRollDiceConfirmation(Player playerName);
    void showRolledDice(Dice dice);
    boolean wantToPick(Player currentPlayer, int actualDiceScore, int availableTileNumber);
    boolean wantToSteal(Player currentPlayer, Player robbedPlayer);
    void showPlayerData(Player currentPlayer, Dice dice, Player[] players);
    Die.Face chooseDie(Player currentPlayer);
    void showBustMessage();
    void printError(String text);
    String askIPToConnect();
    void backToMenu();
}
