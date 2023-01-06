package Heckmeck;

import com.google.gson.Gson;

public class Message {

    Gson gson = new Gson();

    public  Dice dice;
    public  BoardTiles boardTiles;
    public  Player actualPlayer;
    public  Player[] players;
    public  Action operation;
    public  String text;

    public Message(){

    }

    public  enum Action {
        GET_INPUT,
        UPDATE,
        ERROR
    }


    @Override
    public String toString() {

        return gson.toJson(this);

    }

    public void setOperation(Action operation) {
        this.operation = operation;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

    public void setBoardTiles(BoardTiles boardTiles) {
        this.boardTiles = boardTiles;
    }

    public void setActualPlayer(Player actualPlayer) {
        this.actualPlayer = actualPlayer;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }
}
