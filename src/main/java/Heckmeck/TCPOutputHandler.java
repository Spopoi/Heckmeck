/*
package Heckmeck;

import com.google.gson.Gson;
public class TCPOutputHandler implements OutputHandler{


Gson gson = new Gson();

enum action{

}
GameServer gameServer;
Message message = new Message();


public TCPOutputHandler(GameServer gameServer){
    this.gameServer = gameServer;
}

@Override
public void printMessage(String message) {
    gameServer.clients.stream().forEach(client -> client.writeMessage(message));
}

@Override
public void showDice(Dice dice) {
    message.setOperation(Message.Action.UPDATE);
    message.setDice(dice);
    printMessage(message.toString());

}

@Override
public void showTiles(BoardTiles boardTiles) {
    message.setOperation(Message.Action.UPDATE);
    message.setBoardTiles(boardTiles);
    printMessage(message.toString());

}

@Override
public void showPlayerData(Player player, Dice dice, Player[] players) {
    message.setOperation(Message.Action.UPDATE);
    message.setPlayers(players);
    message.setDice(dice);
    message.setActualPlayer(player);
    printMessage(message.toString());

}

@Override
public void showWelcomeMessage() {

}

@Override
public void showWantToPick() {

}

@Override
public void showBustMessage() {

}

@Override
public void showTurnBeginConfirm(String playerName) {

}

@Override
public void showWantToSteal(Player robbedPlayer) {

}

@Override
public void showWantToPlay() {

}
}
*/