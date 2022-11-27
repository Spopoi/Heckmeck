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
            System.out.println(players[playerNumber].getName()+ "  " + playerNumber);
            output.showTiles(groundTiles);
            playerTurn();
            playerNumber++;
            if(playerNumber >= players.length) playerNumber = 0;
            actualPlayer = players[playerNumber];
            //output.showPlayerData(actualPlayer,dice);
        }
        //who's the winner logic
    }

    private void playerTurn(){
        int actualPlayerScore;
        boolean isOnRun = roll();
        while(isOnRun){
            actualPlayerScore = dice.getScore();
            if(dice.isWormChosen() && actualPlayerScore >= groundTiles.getMinValueTile().getNumber()){
                output.showWantToPick();
                output.showPlayerData(actualPlayer,dice);
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
