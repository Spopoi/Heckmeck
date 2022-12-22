package Heckmeck;

import exception.IllegalInput;

public class IOHandler {
    private final InputHandler input;
    private final OutputHandler output;

    public IOHandler(InputHandler input, OutputHandler output){ //TODO qua input e output sono scambiati rispetto costruttore Game()
        this.input = input;
        this.output = output;
    }


    public void printMessage(String message){
        output.printMessage(message);
    }

    public void showTurnBeginConfirm(String playerName){
        output.showTurnBeginConfirm(playerName);
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
                output.printMessage(e.getMessage());
            }
        }
    }
    public int chooseNumberOfPlayers(){
        output.printMessage("Choose number of players between 2 and 7:");
        while(true){
            try{
                int numberOfPlayer = input.chooseNumberOfPlayers();
                if(Rules.validNumberOfPlayer(numberOfPlayer)) return numberOfPlayer;
                else throw new IllegalInput("Invalid number of player, please select a number between 2 and 7");
            } catch (IllegalInput e) {
                output.printMessage(e.getMessage());
            }
        }
    }
    public String choosePlayerName(int playerNumber){
        output.printMessage("Insert the name for player" + playerNumber);
        while(true) {
            try {
                String playerName = input.choosePlayerName();
                if (playerName.isBlank()) throw new IllegalInput("Blank name, choose a valid a one");
                else return playerName;
            } catch (IllegalInput e) {
                output.printMessage(e.getMessage());
            }
        }
    }
    public void showBoardTiles(BoardTiles boardTiles){
        output.showTiles(boardTiles);
    }

    public boolean wantToPick(int diceScore){
        output.printMessage("Actual score: " + diceScore);
        output.showWantToPick();
        while(true) {
            try {
                return input.wantToPick();
            } catch (IllegalInput e) {
                output.printMessage(e.getMessage());
            }
        }
    }

    public boolean wantToSteal(Player robbedPlayer){
        output.showWantToSteal(robbedPlayer);
        while(true) {
            try {
                return input.wantToSteal();
            } catch (IllegalInput e) {
                output.printMessage(e.getMessage());
            }
        }
    }

    public void showPlayerData(Player player, Dice dice, Player[] players){
        output.showPlayerData(player,dice, players);
    }

    public void showDice(Dice dice){
        output.showDice(dice);
    }

    public Die.Face chooseDieFace(){
        output.printMessage("Pick one unselected face");
        while (true) {
            try {
                return input.chooseDiceFace();
            } catch (IllegalInput e) {
                output.printMessage(e.getMessage());
            }
        }
    }

    public void showBustMessage(){
        output.showBustMessage();
    }

}
