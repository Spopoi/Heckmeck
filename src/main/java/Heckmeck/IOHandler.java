package Heckmeck;

import exception.IllegalInput;

public class IOHandler {
    private final InputHandler input;
    private final OutputHandler output;

    public IOHandler(InputHandler input, OutputHandler output){
        this.input = input;
        this.output = output;
    }

    public void showTurnBeginConfirm(Player actualPlayer){
        output.showTurnBeginConfirm(actualPlayer);
        input.pressEnter();
    }

    public void showWelcomeMessage(){
        output.showWelcomeMessage();
    }

    public boolean wantToPlay(){
        output.showWantToPlay();
        while(true) {
            try {
                return input.wantToPlay();
            } catch (IllegalInput e) {
                output.showExceptionMessage(e);
            }
        }
    }
    public int chooseNumberOfPlayers(){
        output.askForNumberOfPlayers();
        while(true){
            try{
                int numberOfPlayer = input.chooseNumberOfPlayers();
                if(Rules.validNumberOfPlayer(numberOfPlayer)) return numberOfPlayer;
                else throw new IllegalInput("Invalid number of player, please select a number between 2 and 7");
            } catch (IllegalInput e) {
                output.showExceptionMessage(e);
            }
        }
    }
    public String choosePlayerName(int playerNumber){
        output.showSetPlayerName(playerNumber);
        while(true) {
            try {
                String playerName = input.choosePlayerName();
                if (playerName.isBlank()) throw new IllegalInput("Blank name, choose a valid a one");
                else return playerName;
            } catch (IllegalInput e) {
                output.showExceptionMessage(e);
            }
        }
    }
    public void showAlreadyPickedPlayerName() {
        output.showAlreadyPickedName();
    }

    public void showBoardTiles(BoardTiles boardTiles){
        output.showTiles(boardTiles);
    }

    public void showWinnerPlayerMessage(Player winnerPlayer) {
        output.showWinnerPlayerMessage(winnerPlayer);
    }

    public boolean wantToPick(){
        output.showWantToPick();
        while(true) {
            try {
                return input.wantToPick();
            } catch (IllegalInput e) {
                output.showExceptionMessage(e);
            }
        }
    }

    public void showPlayerScore(Player player, Dice dice){
        output.showPlayerScore(player,dice);
    }

    public boolean wantToSteal(Player robbedPlayer){
        output.showWantToSteal(robbedPlayer);
        while(true) {
            try {
                return input.wantToSteal();
            } catch (IllegalInput e) {
                output.showExceptionMessage(e);
            }
        }
    }

    public void showPlayerData(Player player, Dice dice){
        output.showPlayerData(player,dice);
    }

    public void showDice(Dice dice){
        output.showDice(dice);
    }

    public Die.Face chooseDieFace(Dice dice){
        output.showDiceChoice();
        while (true) {
            try {
                return input.chooseDiceFace();
            } catch (IllegalInput ex) {
                output.showExceptionMessage(ex);
            }
        }
    }

    public void showBustMessage(){
        output.showBustMessage();
    }

    public void showFaceNotPresentMessage() {
        output.showFaceNotPresentMessage();
    }

    public void showAlreadyPickedDice() {
        output.showAlreadyPickedDice();
    }
}
