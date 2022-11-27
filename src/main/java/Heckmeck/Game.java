package Heckmeck;

import com.sun.source.tree.Tree;

import java.util.List;
import java.util.TreeSet;

public class Game {

    private final Player[] players;
    private Dice dice;
    private Tiles groundTiles;
    private final OutputHandler output;
    private final InputHandler input;

    private boolean gameFinished;

    private Player actualPlayer;

    public Game(Player[] players, OutputHandler output, InputHandler input){
        this.players = players;
        this.dice = Dice.generateDice();
        this.groundTiles = Tiles.init();
        this.output = output;
        this.input = input;
        gameFinished = false;
    }
    public void play(){
        int playerNumber = 0;
        actualPlayer = players[playerNumber];
        while(groundTiles.size() > 0){
            output.showTiles(groundTiles);
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

    private void playerTurn(){
        int actualPlayerScore;
        boolean isOnRun = roll();
        while(isOnRun){
            actualPlayerScore = dice.getScore();
            if(dice.isWormChosen() && actualPlayerScore >= groundTiles.getMinValueTile().getNumber()){
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
        TreeSet<Tile> acquirableTiles = new TreeSet<>(groundTiles.getTilesList().stream().filter(tile-> tile.getNumber() <= actualPlayerScore).toList());
        actualPlayer.pickTileFromBoard(acquirableTiles.last(), groundTiles);
    }

    private boolean roll(){
        dice.rollDice();
        output.showDice(dice);
        output.showPlayerData(actualPlayer, dice);
        if(dice.canPickAnyFace()){
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
        groundTiles.bust();
        actualPlayer.removeLastPickedTile();
    }
}
