package TCP;

import Heckmeck.BoardTiles;
import Heckmeck.Dice;
import Heckmeck.Player;
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
        UPDATE_TILES,
        UPDATE_PLAYER,
        RESPONSE,
        ACK,
        GET_PLAYER_NAME,
        ERROR,
        ASK_CONFIRM;
    }


    public String toString(Object obj) {

        return ""; //gson.toJson(obj);

    }

    public void setPlayerID(int pId) {
        playerID = pId;
    }

    public void setOperation(Action operation) {
        this.operation = operation;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDice(Dice dice) {
        this.dice = (dice);
    }

    public void setBoardTiles(BoardTiles boardTiles) {
        this.boardTiles = (boardTiles);
    }

    public void setActualPlayer(Player actualPlayer) {
        this.actualPlayer = (actualPlayer);
    }

    public void setPlayers(Player[] players) {
        this.players = (players);
    }

    public String toJSON(){
        return gson.toJson(this, Message.class);
    }
    public static Message fromJSON(String json){
        return gson.fromJson(json, Message.class);
    }
}
