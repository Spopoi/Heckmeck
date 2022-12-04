package Heckmeck;

import java.io.IOException;
import java.util.TreeSet;

public class Game {

    // final (?)
    private Player[] players;
    private Dice dice;
    private BoardTiles boardTiles;
    private final OutputHandler output;
    private final InputHandler input;

    private boolean gameFinished;

    private Player actualPlayer;

    public Game(OutputHandler output, InputHandler input) {
        this.output = output;
        this.input = input;
    }

    public void init(){
        output.showWelcomeMessage();
        if (input.wantToPlay()){
            int numberOfPlayers = getNumberOfPlayer();
            this.players = setupPlayersFromUserInput(numberOfPlayers);
            this.dice = Dice.generateDice(); // TODO ha senso rinominare in init()?
            this.boardTiles = BoardTiles.init();
            gameFinished = false;
        }
        else {
            System.exit(0);
        }
    }

    private int getNumberOfPlayer() {
        output.askForNumberOfPlayers();
        while(true) {
            try {
                int numberOfPlayers = input.chooseNumberOfPlayers();
                if (isValidNumberOfPlayers(numberOfPlayers)) {
                    return numberOfPlayers;
                } else {
                    output.showIncorrectNumberOfPlayersMessage();
                }
            } catch (NumberFormatException ex) {
                output.showIncorrectNumberOfPlayersMessage();
            }
        }
    }

    private boolean isValidNumberOfPlayers(int numberOfPlayer) {
        return (numberOfPlayer >= 2 && numberOfPlayer <= 7);
    }

    public Game(Player[] players, OutputHandler output, InputHandler input){
        this.players = players;
        this.dice = Dice.generateDice();// TODO ha senso rinominare in init()?
        this.boardTiles = BoardTiles.init();
        this.output = output;
        this.input = input;
        gameFinished = false;
    }
    public void play() throws IOException {
        int playerNumber = 0;
        actualPlayer = players[playerNumber];
        while(!boardTiles.isEmpty()){
            output.showTiles(boardTiles);
            playerTurn();
            playerNumber++;
            if(playerNumber >= players.length) playerNumber = 0;
            actualPlayer = players[playerNumber];
        }
        Player winnerPlayer = whoIsTheWinner(players);
    }

    //TODO: extend to multiple winners
    private Player whoIsTheWinner(Player[] players) {
        Player winner = players[0];
        for(Player player : players){
            if(winner.getWormNumber() < player.getWormNumber()) winner = player;
        }
        return winner;
    }

    private void playerTurn() throws IOException {
        //output.showTurnBeginConfirm(actualPlayer);  // pass only the String (?)
        //input.pressAnyKey();
        boolean isOnRun = roll();
        while (isOnRun){
            if (dice.isWormChosen()) {
                if (steal() || pick()) {
                    isOnRun = false;
                } else isOnRun = roll();
            } else isOnRun = roll();
        }
        dice.resetDice();
    }

    private boolean pick(){
        if(!canPick()) return false;
        output.showWantToPick();
        output.showPlayerScore(actualPlayer,dice);
        if(input.wantToPick()) {
            pickBoardTile(dice.getScore());
            return true;
        }else return false;
    }

    private boolean steal() {
        if(!canSteal()) return false;
        output.showWantToSteal();
        if(input.wantToSteal()){
            stealTile();
            return true;
        }else return false;
    }

    private void stealTile() {
        int playerScore = dice.getScore();
        for(Player robbedPlayer : players){
            if(!robbedPlayer.equals(actualPlayer) && robbedPlayer.hasTile() && playerScore == robbedPlayer.getLastPickedTile().getNumber()){
                actualPlayer.pickTileFromPlayer(robbedPlayer.getLastPickedTile(),robbedPlayer);
            }
        }
    }

    private boolean canPick(){
        return dice.getScore() >= boardTiles.getMinValueTile().getNumber();
    }

    //TODO: equals method for players
    private boolean canSteal() {
        int playerScore = dice.getScore();
        if(playerScore < Tile.tileMinNumber) return false;
        for(Player player : players){
            if(!player.equals(actualPlayer) && player.hasTile() && playerScore == player.getLastPickedTile().getNumber()){
                return true;
            }
        }
        return false;
    }

    private void pickBoardTile(int actualPlayerScore){
        TreeSet<Tile> acquirableTiles = new TreeSet<>(boardTiles.getTilesList().stream().filter(tile-> tile.getNumber() <= actualPlayerScore).toList());
        actualPlayer.pickTileFromBoard(acquirableTiles.last(), boardTiles);
    }

    private boolean roll() throws IOException {
        dice.rollDice();
        output.showPlayerData(actualPlayer, dice);
        output.showDice(dice);
        if(dice.canPickAFace()){
            //verify that the chosen die is okay
            output.showDiceChoice();
//            dice.chooseDice();

            dice.chooseDice(Die.intToFace(input.chooseDiceNumber()));
            return true;
        } else{
            bust();
            return false;
        }
    }

    private void bust(){
        output.showBustMessage();
        actualPlayer.removeLastPickedTile();
        boardTiles.bust();
    }

    private Player[] setupPlayersFromUserInput(int numberOfPlayers) {
        // Why not ArrayList?
        Player[] playersList = new Player[numberOfPlayers];
        for(int i=0; i<numberOfPlayers; i++) {
            output.showSetPlayerName(i+1);
            String playerName = input.choosePlayerName();
            while (playerName.isBlank()) {
                output.showBlankPlayerNameWarning();
                output.showSetPlayerName(i+1);
                playerName = input.choosePlayerName();
            }
            playersList[i] = Player.generatePlayer(playerName);
        }
        return playersList;
    }
}
