package Heckmeck;

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

    Die.Face chooseDie(Player currentPlayer, Dice dice);

    void showBustMessage();

    String getInputString();

    void printError(String text);

    String askIPToConnect();

    boolean wantToPlayAgain();

}
