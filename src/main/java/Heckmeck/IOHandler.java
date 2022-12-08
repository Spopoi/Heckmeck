package Heckmeck;

import exception.HeckmeckException;
import exception.IllegalInput;

import java.io.IOException;

public class IOHandler {
    private final InputHandler input;
    private final OutputHandler output;

    public IOHandler(InputHandler input, OutputHandler output){
        this.input = input;
        this.output = output;
    }

    public void showWelcomeMessage(){
        output.showWelcomeMessage();
    }

    public boolean wantToPlay(){
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

    public void showBoardTiles(BoardTiles boardTiles) throws IOException{
        output.showTiles(boardTiles);
    }

    public void showWinnerPlayerMessage(Player winnerPlayer) {
        output.showWinnerPlayerMessage(winnerPlayer);
    }
}
