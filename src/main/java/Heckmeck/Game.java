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

    public void init() {
        output.showWelcomeMessage();
        if (input.wantToPlay()) {
            setupPlayersFromUserInput();
            this.dice = Dice.generateDice(); // TODO ha senso rinominare in init()?
            this.boardTiles = BoardTiles.init();
            gameFinished = false;
        }
        else {
            System.exit(0);
        }
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
        while(boardTiles.size() > 0){
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
        output.showTurnBeginConfirm(actualPlayer);
        input.pressAnyKey();
        int actualPlayerScore;
        boolean isOnRun = roll();
        while(isOnRun){
            actualPlayerScore = dice.getScore();
            if(dice.isWormChosen() && actualPlayerScore >= boardTiles.getMinValueTile().getNumber()){
                output.showWantToPick();
                output.showPlayerScore(actualPlayer,dice);
                if(input.wantToPick()) {
                    pickBoardTile(actualPlayerScore);
                    isOnRun = false;
                }else isOnRun = roll();
            } else isOnRun = roll();
        }
        dice.resetDice();
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

    private void setupPlayersFromUserInput() {
        output.askForNumberOfPlayers();
        int numberOfPlayers = input.chooseNumberOfPlayers();
        this.players = new Player[numberOfPlayers];
        for(Player player : players) {
            output.showSetPlayerName();
            String playerName = input.choosePlayerName();
            player = Player.generatePlayer(playerName);
        }
    }
}
