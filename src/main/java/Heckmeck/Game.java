package Heckmeck;

import Heckmeck.Components.*;
import Utils.MessageManager;

import java.io.IOException;
import java.util.*;
import static Heckmeck.Components.Die.Face;
public class Game {
    private Player[] players;
    private Dice dice;
    private BoardTiles boardTiles;
    private final IOHandler io;
    private Player actualPlayer;
    private MessageManager messageManager;

    public Game(IOHandler io) throws IOException {
        this.io = io;
        messageManager = new MessageManager(MessageManager.PropertiesFileIdentifier.GAME_MESSAGES);
    }

    public void init(){
        int numberOfPlayers = io.chooseNumberOfPlayers();
        this.players = setupPlayers(numberOfPlayers);
        this.dice = Dice.init();
        this.boardTiles = BoardTiles.init();
        this.actualPlayer = this.players[0];
        io.printMessage(messageManager.getMessage("gameStartMessage"));
    }
    public void play(){
        int playerNumber = 0;
        actualPlayer = players[playerNumber];
        while(boardTiles.hasElement()){
            playerTurn();
            playerNumber++;
            if(playerNumber >= players.length) playerNumber = 0;
            actualPlayer = players[playerNumber];
        }
        Player winnerPlayer = Rules.whoIsTheWinner(players);
        if (winnerPlayer == null) {
            io.printMessage("This is a draw!!");
        } else {
            io.printMessage("Congratulation to "+winnerPlayer.getName() + ", you are the WINNER!!");
        }
        io.backToMenu();
    }

    private void playerTurn() {
        io.showTurnBeginConfirm(actualPlayer);
        boolean isOnRun;
        do {
            io.showBoardTiles(boardTiles);
            io.showPlayerData(actualPlayer, dice, players);
            if (dice.isFaceChosen(Face.WORM)) {
                if (playerAction()) isOnRun = false;
                else isOnRun = roll();
            } else isOnRun = roll();
        } while (isOnRun);
        dice.resetDice();
    }

    private boolean playerAction(){
        if (canSteal()){
            return steal();
        } else if (canPick()) return pick();
        else return false;
    }

    private boolean pick(){
        //assume worm chosen and can pick
        if(dice.getChosenDice().size() >= Rules.INITIAL_NUMBER_OF_DICE || wantToPick()) {
            pickTile();
            return true;
        }
        return false;
    }

    private boolean canPick(){
        Tile minValueTile = boardTiles.getTiles().first();
        return dice.getScore() >= minValueTile.number();
    }

    private boolean wantToPick() {
        // Assume canPick()
        int diceScore = dice.getScore();
        Tile availableTile = boardTiles.nearestTile(diceScore);
        return io.wantToPick(actualPlayer, diceScore, availableTile.number());
    }

    private void pickTile(){
        Tile availableTile = boardTiles.nearestTile(dice.getScore());
        boardTiles.remove(availableTile);
        actualPlayer.pickTile(availableTile);
        io.printMessage("You got tile number " + availableTile.number() +"!");
    }

    private boolean canSteal(){
        int playerScore = dice.getScore();
        if(playerScore < Tile.tileMinNumber) return false;
        for(Player robbedPlayer : players){
            if(!robbedPlayer.equals(actualPlayer) && actualPlayer.canStealFrom(robbedPlayer,playerScore)) return true;
        }
        return false;
    }

    private boolean steal(){
        //assume worm chosen and can steal
        int playerScore = dice.getScore();
        for(Player robbedPlayer : players){
            if(!robbedPlayer.equals(actualPlayer) && actualPlayer.canStealFrom(robbedPlayer,playerScore)){
                if(io.wantToSteal(actualPlayer, robbedPlayer)){
                    actualPlayer.stealTileFromPlayer(robbedPlayer);
                    return true;
                } else return false;
            }
        }
        return false;
    }

    private boolean roll(){
        if(dice.getNumOfDice() <= 0) {
            bust();
            return false;
        }
        dice.rollDice();
        io.showRolledDice(dice);
        if(dice.canPickAFace()){
            Die.Face chosenDieFace = pickDieFace();
            dice.chooseDice(chosenDieFace);
            return true;
        }else{
            bust();
            return false;
        }
    }

    private Die.Face pickDieFace() {
        while(true){
            Die.Face chosenDieFace = io.chooseDie(actualPlayer);
            if(!dice.isFacePresent(chosenDieFace)) io.printError("This face is not present.. Pick another one!");
            else if(dice.isFaceChosen(chosenDieFace)) io.printError("You have already chose this face, pick another one!");
            else return chosenDieFace;
        }
    }

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
            playersList[i] = Player.generatePlayer(i);
            this.actualPlayer = playersList[i];
            String playerName = io.choosePlayerName(playersList[i]);
            while(isNameAlreadyPicked(playerName,playersList)){
                io.printError(playerName + " Already picked name.. Please choose another one");
                playerName = io.choosePlayerName(playersList[i]);
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
}
