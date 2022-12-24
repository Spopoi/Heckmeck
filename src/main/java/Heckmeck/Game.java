package Heckmeck;

import java.util.*;
import static Heckmeck.Die.Face;

public class Game {

    private Player[] players;
    private Dice dice;
    private BoardTiles boardTiles;
    private final IOHandler io;

    private Player actualPlayer;

    public Game(OutputHandler output, InputHandler input) {
        io = new IOHandler(input,output);
    }

    public void init(){
       io.showWelcomeMessage();
        if (io.wantToPlay()){
            int numberOfPlayers = io.chooseNumberOfPlayers();
            this.players = setupPlayers(numberOfPlayers);
            this.dice = Dice.init();
            this.boardTiles = BoardTiles.init();
        }
        else {
            System.exit(0);
        }
    }
    public void play(){
        int playerNumber = 0;
        actualPlayer = players[playerNumber];
        while(!boardTiles.isEmpty()){
            playerTurn();
            playerNumber++;
            if(playerNumber >= players.length) playerNumber = 0;
            actualPlayer = players[playerNumber];
        }
        Player winnerPlayer = Rules.whoIsTheWinner(players);
        io.printMessage("Congratulation to "+winnerPlayer.getName() + ", you are the WINNER!!");
    }

    private void playerTurn(){
        io.showTurnBeginConfirm(actualPlayer.getName());
        io.showBoardTiles(boardTiles);
        boolean isOnRun = roll();
        while (isOnRun){
            if (dice.isFaceChosen(Face.WORM)) {
                if (steal() || pick()) {
                    isOnRun = false;
                } else isOnRun = roll();
            } else isOnRun = roll();
        }
        dice.resetDice();
    }

    private boolean pick(){
        if(canPick() && io.wantToPick(dice.getScore())){
            pickBoardTile(dice.getScore());
            return true;
        }else return false;
    }

    private boolean canPick(){
        Tile minValueTile = boardTiles.getTiles().first();
        return dice.getScore() >= minValueTile.getNumber();
        //return dice.getScore() >= boardTiles.getMinValueTile().getNumber();
    }

    private boolean steal(){
        int playerScore = dice.getScore();
        if(playerScore < Tile.tileMinNumber) return false;
        for(Player robbedPlayer : players){
            if(!robbedPlayer.equals(actualPlayer) && actualPlayer.canStealFrom(robbedPlayer,playerScore)){
                if(io.wantToSteal(robbedPlayer)){
                    actualPlayer.stealTileFromPlayer(robbedPlayer);
                    return true;
                } else return false;
            }
        }
        return false;
    }

    //TODO: maybe moving this method into boardTiles?
    private void pickBoardTile(int actualPlayerScore){
        TreeSet<Tile> acquirableTiles = new TreeSet<>(boardTiles.getTiles().stream().filter(tile-> tile.getNumber() <= actualPlayerScore).toList());
        actualPlayer.pickTile(acquirableTiles.last());
        boardTiles.remove(acquirableTiles.last());
    }

    //TODO: rolli anche se finisci i dadi
    private boolean roll(){
        dice.rollDice();
        io.showPlayerData(actualPlayer, dice, players);
        io.showDice(dice);
        if(dice.canPickAFace()){
            Die.Face chosenDieFace = pickDieFace();
            dice.chooseDice(chosenDieFace);
            return true;
        } else{
            bust();
            return false;
        }
    }

    private Die.Face pickDieFace() {
        while(true){
            Die.Face chosenDieFace = io.chooseDieFace();
            if(!dice.isFacePresent(chosenDieFace)) io.printMessage("This face is not present.. Pick another one!");
            else if(dice.isFaceChosen(chosenDieFace)) io.printMessage("You have already chose this face, pick another one!");
            else return chosenDieFace;
        }
    }

    //TODO: Rimettere in gioco tessere persa dal giocatore
    private void bust(){
        io.showBustMessage();
        if(actualPlayer.hasTile()){
            boardTiles.add(actualPlayer.getLastPickedTile());
            actualPlayer.removeLastPickedTile();
        }
        boardTiles.removeLastTile();
    }

    private Player[] setupPlayers(int numberOfPlayers) {
        Player[] playersList = new Player[numberOfPlayers];
        for(int i=0; i<numberOfPlayers; i++){
            String playerName = io.choosePlayerName(i);
            while(isNameAlreadyPicked(playerName,playersList)){
                io.printMessage("Already picked name.. Please choose another one");
                playerName = io.choosePlayerName(i+1);
            }
            playersList[i] = Player.generatePlayer(playerName);
        }
        return playersList;
    }

    private boolean isNameAlreadyPicked(String name, Player[] playersList){
        return Arrays.stream(playersList).filter(Objects::nonNull).anyMatch(player -> player.getName().equals(name));
    }

    public Player[] getPlayers(){
        return players;
    }


    public BoardTiles getBoardTiles(){
        return boardTiles;
    }
    public Dice getDice(){
        return dice;
    }
    public Player getActualPlayer(){
        return actualPlayer;
    }
}



