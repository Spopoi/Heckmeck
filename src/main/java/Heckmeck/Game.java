package Heckmeck;

import java.util.*;
import static Heckmeck.Die.Face;

public class Game {

    private Player[] players;
    private Dice dice;
    private BoardTiles boardTiles;
    private final IOHandler io;

    private Player actualPlayer;
    private int actualPlayerID;

    public Game(IOHandler io) {
       this.io = io;
    }

    public void init(){
        //TODO: Risolvere bug quando prendi ultimo dado bust automatico
        int numberOfPlayers = io.chooseNumberOfPlayers();

        this.players = setupPlayers(numberOfPlayers);
        this.dice = Dice.init();
        this.boardTiles = BoardTiles.init();
        this.actualPlayer = this.players[0];
        io.printMessage("OK, let's begin!");

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
        //TODO il nome non serve più come parametro per showBeginConfirm()
        boolean isOnRun;
        do{
            io.showBoardTiles(boardTiles);
            if (dice.isFaceChosen(Face.WORM)) {
                if (steal() || pick()) {
                    isOnRun = false;
                } else isOnRun = roll();
            } else isOnRun = roll();
        } while (isOnRun);

        dice.resetDice();
    }

    private boolean pick(){
        return (canPick() && wantToPick());
    }

    private boolean canPick(){
        Tile minValueTile = boardTiles.getTiles().first();
        return dice.getScore() >= minValueTile.getNumber();
    }

    private boolean wantToPick() {
        // Assume canPick()
        Tile searchedTile = Tile.generateTile(dice.getScore());
        Tile availableTile = boardTiles.smallerTileNearestTo(searchedTile);  // if canPick() should never return null "theoretically"
        if (io.wantToPick(searchedTile.getNumber(), availableTile.getNumber())) {
            boardTiles.remove(availableTile);
            actualPlayer.pickTile(availableTile);
            return true;
        }
        return false;
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

    //TODO: rolli anche se finisci i dadi
    private boolean roll(){

        io.askRollDiceConfirmation(actualPlayer.getName());
        dice.rollDice();

        io.showPlayerData(actualPlayer, dice, players);
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
            io.showRolledDice(dice);
            Die.Face chosenDieFace = io.chooseDie(dice);
            if(!dice.isFacePresent(chosenDieFace)) io.printError("This face is not present.. Pick another one!");
            else if(dice.isFaceChosen(chosenDieFace)) io.printError("You have already chose this face, pick another one!");
            else return chosenDieFace;
        }
    }

    //TODO: Rimettere in gioco tessere persa dal giocatore
    private void bust(){
        io.showRolledDice(dice);
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
            playersList[i] = Player.generatePlayer(i);
            this.actualPlayer = playersList[i];
            String playerName = io.choosePlayerName(i);
            while(isNameAlreadyPicked(playerName,playersList)){
                io.printError(playerName + " Already picked name.. Please choose another one");
                playerName = io.choosePlayerName(i);
            }
            playersList[i].setPlayerName(playerName);

        }
        return playersList;
    }

    private boolean isNameAlreadyPicked(String name, Player[] playersList){
        return Arrays.stream(playersList).filter(Objects::nonNull).anyMatch(player -> {
            String playerName = player.getName();
            return playerName != null && playerName.equals(name);
        });
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



