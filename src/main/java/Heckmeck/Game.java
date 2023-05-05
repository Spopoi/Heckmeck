package Heckmeck;

import java.util.*;
import static Heckmeck.Die.Face;

public class Game {

    private Player[] players;
    private Dice dice;
    private BoardTiles boardTiles;
    private final IOHandler io;

    private Player actualPlayer;

    public Game(IOHandler io) {
       this.io = io;
    }

    public void init(){
        // io.showWelcomeMessage();

        int numberOfPlayers = io.chooseNumberOfPlayers();
        this.players = setupPlayers(numberOfPlayers);
        this.dice = Dice.init();
        this.boardTiles = BoardTiles.init();
        this.actualPlayer = this.players[0];

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
//        io.showBoardTiles(boardTiles);

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
        //return dice.getScore() >= boardTiles.getMinValueTile().getNumber();
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
        io.showPlayerData(actualPlayer, dice, players);
        io.askRollDiceConfirmation(actualPlayer.getName());
        dice.rollDice();
        //io.showDice(dice);
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
            if(!dice.isFacePresent(chosenDieFace)) io.printMessage("This face is not present.. Pick another one!");
            else if(dice.isFaceChosen(chosenDieFace)) io.printMessage("You have already chose this face, pick another one!");
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
            String playerName = io.choosePlayerName(i);
            while(isNameAlreadyPicked(playerName,playersList)){
                io.printMessage(playerName + " Already picked name.. Please choose another one");
                playerName = io.choosePlayerName(i+1);
            }
            playersList[i] = Player.generatePlayer(playerName);
            playersList[i].setPlayerID(i); // TODO Modificato da dew54.. da fare "meglio"
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



