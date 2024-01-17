package TCP;

import Heckmeck.Components.BoardTiles;
import Heckmeck.Components.Dice;
import Heckmeck.Components.Player;
import com.google.gson.Gson;

import java.io.Serializable;

public class Message implements Serializable {

    static Gson gson = new Gson();
    public Dice dice;
    public int id;
    public boolean decision;
    public int diceScore;
    public int availableTileNumber;
    public BoardTiles boardTiles;
    public Player currentPlayer;
    public  Player[] players;
    public  Action operation;
    public  String text;
    public Player robbedPlayer;

    private Message(){
    }
    public static Message generateMessage(){
        return new Message();
    }

    public  enum Action {
        INIT,
        INFO,
        BACK_TO_MENU,
        UPDATE_DICE,
        UPDATE_TILES,
        UPDATE_PLAYER,
        RESPONSE,
        ACK,
        GET_PLAYER_NAME,
        WANT_PICK,
        WANT_STEAL,
        ERROR,
        CHOOSE_DICE,
        BEGIN_TURN,
        WAIT, BUST
    }
    public Message setPlayerID(int pId) {
        this.id = pId;
        return this;
    }
    public Message setOperation(Action operation) {
        this.operation = operation;
        return this;
    }
    public Message setText(String text) {
        this.text = text;
        return this;
    }
    public Message setDice(Dice dice) {
        this.dice = (dice);
        return this;
    }
    public Message setBoardTiles(BoardTiles boardTiles) {
        this.boardTiles = (boardTiles);
        return this;
    }
    public Message setCurrentPlayer(Player actualPlayer) {
        this.currentPlayer = (actualPlayer);
        return this;
    }
    public Message setPlayers(Player[] players) {
        this.players = (players);
        return this;
    }

    public Message setScore(int score){
        this.diceScore = score;
        return this;
    }
    public Message setDecision(boolean decision){
        this.decision = decision;
        return this;
    }
    public Message setAvailableTileNumber(int tileNumber){
        this.availableTileNumber = tileNumber;
        return this;
    }
    public Message setRobbedPlayer(Player player){
        this.robbedPlayer = player;
        return this;
    }

    public String toJSON(){
        return gson.toJson(this, Message.class);
    }
    public static Message fromJSON(String json){
        return gson.fromJson(json, Message.class);
    }
}
