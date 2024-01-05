package TCP;

import Heckmeck.Components.BoardTiles;
import Heckmeck.Components.Dice;
import Heckmeck.Components.Player;
import com.google.gson.Gson;

import java.io.Serializable;

public class Message implements Serializable {

    static Gson gson = new Gson();
    public Dice dice;
    public BoardTiles boardTiles;
    public Player actualPlayer;
    public  Player[] players;
    public  Action operation;
    public  String text;
    public int playerID;


    private Message(){

    }

    public static Message generateMessage(){
        return new Message();
    }

    public  enum Action {
        INIT,
        INFO,
        GET_INPUT,
        UPDATE_DICE,
        UPDATE_TILES,
        UPDATE_PLAYER,
        RESPONSE,
        ACK,
        GET_PLAYER_NAME,
        ERROR,
        CHOOSE_DICE,
        BEGIN_TURN
    }
    public Message setPlayerID(int pId) {
        playerID = pId;
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
        this.actualPlayer = (actualPlayer);
        return this;
    }
    public Message setPlayers(Player[] players) {
        this.players = (players);
        return this;
    }
    public String toJSON(){
        return gson.toJson(this, Message.class);
    }
    public static Message fromJSON(String json){
        return gson.fromJson(json, Message.class);
    }
}
