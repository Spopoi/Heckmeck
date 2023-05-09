package TCP.Server;

import Heckmeck.*;
import TCP.Message;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

public class TCPIOHandler implements IOHandler {

    Gson gson = new Gson();


    GameServer gameServer;

    public TCPIOHandler(GameServer gameServer){
        this.gameServer = gameServer;
    }

    @Override
    public void printMessage(String text) {
        Message msg = new Message();
        msg.setText(text);
        msg.setOperation(Message.Action.INFO);
        msg.setPlayerID(getCurrentPlayerId());
        gameServer.sockets.stream().forEach(client -> client.writeMessage(msg));
    }
    @Override
    public String printError(String text){
        Message msg = new Message();
        msg.setText(text);
        msg.setOperation(Message.Action.ERROR);
        gameServer.sockets.stream().forEach(client -> client.writeMessage(msg));
        Message respMsg = getCurrentPlayerSocket().readReceivedMessage();
        return respMsg.text;
    }

    @Override
    public void showTurnBeginConfirm(String playerName) {
        System.out.println("ASKING PLAYER INPUT FOR: " + playerName);
        Message message = new Message();
        message.setOperation(Message.Action.GET_INPUT);
        message.setActualPlayer(gameServer.game.getActualPlayer());
        message.setText(playerName + ", press enter to start your turn");
        gameServer.sockets.stream().forEach(client -> client.writeMessage(message));
        //waitOneSec();
        Message msg = readMessage(gameServer.game.getActualPlayer().getPlayerID());

    }

    @Override
    public void showWelcomeMessage() {

    }

    // TODO: REMOVE BEFORE COMMIT!!
    @Override
    public boolean wantToPlayRemote() {
        return false;
    }

    void waitOneSec(){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int chooseNumberOfPlayers() {
        return gameServer.getNumOfPlayers();
    }

    @Override
    public String choosePlayerName(int playerNumber) {

        Message message = new Message();
        //message.setActualPlayer(gameServer.cu);
        message.setOperation(Message.Action.GET_PLAYER_NAME);
        message.setText("Choose player name");
        message.setPlayerID(playerNumber);
        //gameServer.clients.stream().forEach(client -> client.writeMessage(message));
        gameServer.sockets.get(playerNumber).writeMessage(message);
        //waitOneSec();
        Message msg = readMessage(playerNumber);
        return msg.text;
    }

    @Override
    public void showBoardTiles(BoardTiles boardTiles) {
        Message message = new Message();
        //message.setActualPlayer(gameServer.game.getActualPlayer());
        message.setPlayerID(getCurrentPlayerId());
        message.setOperation(Message.Action.UPDATE_TILES);
        message.setBoardTiles(boardTiles);
        gameServer.sockets.stream().forEach(client -> client.writeMessage(message));
        /*gameServer.clients.get(0).writeMessage(message);
        waitOneSec();
        //gameServer.clients.get(1).writeMessage(message);
        waitOneSec();
        //gameServer.clients.get(2).writeMessage(message);
        waitOneSec();*/


    }

    @Override
    public void askRollDiceConfirmation(String playerName) {
        return;
    }

    @Override
    public void showRolledDice(Dice dice) {
        return;
    }

    // TODO: move method to the new signature
    @Override
    public boolean wantToPick(int actualDiceScore, int availableTileNumber) {
        Message message = new Message();
        message.setActualPlayer(gameServer.game.getActualPlayer());
        message.setOperation(Message.Action.GET_INPUT);
        message.setPlayerID(getCurrentPlayerId());

        message.setText("Do you want to pick tile n. " + gameServer.game.getDice().getScore() + "?");
        gameServer.sockets.stream().forEach(client -> client.writeMessage(message));
        //waitOneSec();
        Message msg = readMessage(gameServer.game.getActualPlayer().getPlayerID());
        //if(isYesOrNoChar(decision)) else
        return "y".equalsIgnoreCase(msg.text);

    }

    @Override
    public boolean wantToSteal(Player robbedPlayer) {

        Message message = new Message();
        message.setActualPlayer(gameServer.game.getActualPlayer());
        message.setPlayerID(getCurrentPlayerId());

        message.setOperation(Message.Action.GET_INPUT);
        message.setText("Do you want to pick?");
        gameServer.sockets.stream().forEach(client -> client.writeMessage(message));
        //waitOneSec();
        Message msg = readMessage(gameServer.game.getActualPlayer().getPlayerID());
        //if(isYesOrNoChar(decision)) else
        return "y".equalsIgnoreCase(msg.text);    }

    @Override
    public void showPlayerData(Player player, Dice dice, Player[] players) {
        Message message = new Message();
        message.setActualPlayer(player);
        message.setPlayerID(getCurrentPlayerId());
        message.setDice(dice);
        message.setPlayers(players);
        message.setOperation(Message.Action.UPDATE_PLAYER);
        gameServer.sockets.stream().forEach(client -> client.writeMessage(message));

    }

    @Override
    public Die.Face chooseDie(Dice dice) {
        Message message = new Message();
        message.setActualPlayer(gameServer.game.getActualPlayer());
        message.setOperation(Message.Action.GET_INPUT);
        message.setPlayerID(getCurrentPlayerId());
        message.setText("Choose a die face");
        gameServer.sockets.stream().forEach(client -> client.writeMessage(message));
        //waitOneSec();
        Message msg = readMessage(gameServer.game.getActualPlayer().getPlayerID());
        return Die.getFaceByString(msg.text);
    }

    @Override
    public void showBustMessage() {
        printMessage("## BUST! ##");

    }

    @Override
    public String getInputString() {
        return null;
    }

    public String readRxBuffer(int playerId){
        return (gameServer.sockets.get(playerId).readLine());
    }

    public Message readMessage(int playerId){
        return gameServer.sockets.get(playerId).readReceivedMessage();
    }


    public void showDice(Dice dice){
        Message message = new Message();

        message.boardTiles = null;
        message.setOperation(Message.Action.UPDATE_PLAYER);
        message.setDice(dice);
        gameServer.sockets.stream().forEach(client -> client.writeMessage(message));
    }

    private int getCurrentPlayerId(){
        return this.gameServer.game.getActualPlayer().getPlayerID();
    }

    private Player getCurrentPlayer(){
        return this.gameServer.game.getPlayers()[(getCurrentPlayerId())];
    }

    private SocketHandler getCurrentPlayerSocket(){
        return this.gameServer.sockets.get(getCurrentPlayerId());
    }

}
