package Heckmeck;

import Heckmeck.Components.*;
import Utils.PropertiesManager;

import java.io.IOException;
import java.util.*;
import static Heckmeck.Components.Die.Face;
import static Heckmeck.Rules.isNameAlreadyPicked;
import static Utils.PropertiesManager.getGameMessagePropertiesPath;

public class Game {
    private Player[] players;
    private Dice dice;
    private BoardTiles boardTiles;
    private final IOHandler io;
    private Player actualPlayer;
    private final PropertiesManager propertiesManager;

    public Game(IOHandler io) throws IOException {
        this.io = io;
        propertiesManager = new PropertiesManager(getGameMessagePropertiesPath());
    }

    public void init(){
        int numberOfPlayers = io.chooseNumberOfPlayers();
        this.players = setupPlayers(numberOfPlayers);
        this.dice = Dice.init();
        this.boardTiles = BoardTiles.init();
        this.actualPlayer = this.players[0];
        io.printMessage(propertiesManager.getMessage("gameStartMessage"));
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
            io.printMessage(propertiesManager.getMessage("draw"));
        } else {
            io.printMessage(propertiesManager.getMessage("winner").replace("$PLAYER_NAME", winnerPlayer.getName()));
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
        Tile minValueTile = boardTiles.tiles().first();
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
        io.printMessage(propertiesManager.getMessage("gotTile").replace("$TILE_NUMBER", Integer.toString(availableTile.number())));
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
            if(!dice.isFacePresent(chosenDieFace)) io.printError(propertiesManager.getMessage("faceNotPresent"));
            else if(dice.isFaceChosen(chosenDieFace)) io.printError(propertiesManager.getMessage("faceAlreadyChosen"));
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
                io.printError(propertiesManager.getMessage("nameAlreadyPicked").replace("$PLAYER_NAME", playerName));
                playerName = io.choosePlayerName(playersList[i]);
            }
            playersList[i].setPlayerName(playerName);
        }
        return playersList;
    }
}
