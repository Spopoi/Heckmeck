package Heckmeck;

import java.util.*;

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
        io.showWinnerPlayerMessage(winnerPlayer);
    }

    private void playerTurn(){
        io.showTurnBeginConfirm(actualPlayer.getName());
        io.showBoardTiles(boardTiles);
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
        io.showPlayerScore(actualPlayer,dice);
        if(io.wantToPick()) {
            pickBoardTile(dice.getScore());
            return true;
        }else return false;
    }

    private boolean canPick(){
        return dice.getScore() >= boardTiles.getMinValueTile().getNumber();
    }

    private boolean steal(){
        int playerScore = dice.getScore();
        if(playerScore < Tile.tileMinNumber) return false;
        for(Player robbedPlayer : players){
            if(!robbedPlayer.equals(actualPlayer) && robbedPlayer.hasTile() && playerScore == robbedPlayer.getLastPickedTileNumber()){
                if(io.wantToSteal(robbedPlayer)){
                    actualPlayer.stealTileFromPlayer(robbedPlayer);
                    return true;
                } else return false;
            }
        }
        return false;
    }

    private void pickBoardTile(int actualPlayerScore){
        TreeSet<Tile> acquirableTiles = new TreeSet<>(boardTiles.getTilesList().stream().filter(tile-> tile.getNumber() <= actualPlayerScore).toList());
        actualPlayer.pickTileFromBoard(acquirableTiles.last(), boardTiles);
    }

    private boolean roll(){
        dice.rollDice();
        io.showPlayerData(actualPlayer, dice);
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
            Die.Face chosenDieFace = io.chooseDieFace(dice);
            if(!dice.isFacePresent(chosenDieFace)) io.showFaceNotPresentMessage();
            else if(dice.isFaceChosen(chosenDieFace)) io.showAlreadyPickedDice();
            else return chosenDieFace;
        }
    }

    private void bust(){
        io.showBustMessage();
        actualPlayer.removeLastPickedTile();
        boardTiles.bust();
    }

    private Player[] setupPlayers(int numberOfPlayers) {
        Player[] playersList = new Player[numberOfPlayers];
        for(int i=0; i<numberOfPlayers; i++){
            String playerName = io.choosePlayerName(i + 1);
            while(isNameAlreadyPicked(playerName,playersList)){
                io.showAlreadyPickedPlayerName();
                playerName = io.choosePlayerName(i+1);
            }
            playersList[i] = Player.generatePlayer(playerName);
        }
        return playersList;
    }

    private boolean isNameAlreadyPicked(String name, Player[] playersList){
        return Arrays.stream(playersList).filter(Objects::nonNull).anyMatch(player -> player.getName().equals(name));
    }
}
