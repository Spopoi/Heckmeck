package TCP;

import Heckmeck.BoardTiles;
import Heckmeck.Dice;
import Heckmeck.Player;
import com.google.gson.Gson;

import java.io.Serializable;

public class Message implements Serializable {


    public Dice dice;
    public BoardTiles boardTiles;
    public Player actualPlayer;
    public  Player[] players;
    public  Action operation;
    public  String text;
    public int playerID;


    public Message(){

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
}
