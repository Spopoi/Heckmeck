package TCP.Server;

import Heckmeck.*;
import TCP.Message;

import java.util.List;

public class TCPIOHandler implements IOHandler {


    private final List<SocketHandler> sockets;
    private Game game;

    public void setGame(Game game){
        this.game = game;
    }

    public TCPIOHandler(List<SocketHandler> sockets){
        this.sockets = sockets;
        //this.game = game;
    }

    private void sendBroadCast(Message msg){
        sockets.forEach(client -> client.writeMessage(msg));
    }

    @Override
    public void printMessage(String text) {
        Message msg = new Message();
        msg.setText(text);
        msg.setOperation(Message.Action.INFO);
        msg.setPlayerID(getCurrentPlayerId());
        sendBroadCast(msg);
    }
    //TODO: MODIFICARE
    @Override
    public void printError(String text){
        Message msg = new Message();
        msg.setText(text);
        msg.setOperation(Message.Action.ERROR);
        sendBroadCast(msg);
        Message respMsg = getCurrentPlayerSocket().readReceivedMessage();
        //return respMsg.text;
    }


    @Override
    public void showTurnBeginConfirm(String playerName) {
        Message msg = setGameStatus();
        informEveryOtherClient();
        msg.setOperation(Message.Action.ASK_CONFIRM);
        msg.setText(playerName + ", press enter to start your turn");
        getCurrentPlayerSocket().writeMessage(msg);
    }

    @Override
    public void showWelcomeMessage() {

    }

    // TODO: REMOVE BEFORE COMMIT!!
    @Override
    public boolean wantToPlayRemote() {
        return false;
    }

    @Override
    public int chooseNumberOfPlayers() {
        return sockets.size();
    }

    @Override
    public String choosePlayerName(int playerID) {
        Message message = new Message();
        message.setActualPlayer(game.getActualPlayer());
        message.setOperation(Message.Action.GET_PLAYER_NAME);
        message.setText("Choose player name");
        message.setPlayerID(playerID);
        sockets.get(playerID).writeMessage(message);
        return readMessage(playerID).text;
    }

    @Override
    public void showBoardTiles(BoardTiles boardTiles) {
        Message msg = setGameStatus();
        msg.setOperation(Message.Action.UPDATE_TILES);
        sendBroadCast(msg);
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
        Message msg = new Message();
        msg.setActualPlayer(game.getActualPlayer());
        msg.setOperation(Message.Action.GET_INPUT);
        msg.setPlayerID(getCurrentPlayerId());
        msg.setText("Do you want to pick tile n. " + availableTileNumber + "?");
        sendBroadCast(msg);
        Message rxMsg = readMessage(game.getActualPlayer().getPlayerID());
        return "y".equalsIgnoreCase(rxMsg.text);  // TODO verificare il metodo di check
    }
    @Override
    public boolean wantToSteal(Player robbedPlayer) {
        Message msg = new Message();
        msg.setActualPlayer(game.getActualPlayer());
        msg.setPlayerID(getCurrentPlayerId());
        msg.setOperation(Message.Action.GET_INPUT);
        msg.setText("Do you want to steal?");
        sockets.stream().forEach(client -> client.writeMessage(msg));
        Message rxMsg = readMessage(game.getActualPlayer().getPlayerID());
        return "y".equalsIgnoreCase(rxMsg.text);    }

    @Override
    public void showPlayerData(Player player, Dice dice, Player[] players) {
        Message msg = new Message();
        msg.setActualPlayer(player);
        msg.setPlayerID(getCurrentPlayerId());
        msg.setDice(dice);
        msg.setPlayers(players);
        msg.setOperation(Message.Action.UPDATE_PLAYER);
        sockets.stream().forEach(client -> client.writeMessage(msg));
    }

    @Override
    public Die.Face chooseDie(Dice dice) {
        Message msg = new Message();
        msg.setActualPlayer(game.getActualPlayer());
        msg.setPlayerID(getCurrentPlayerId());
        informEveryOtherClient();
        msg.setOperation(Message.Action.GET_INPUT);
        msg.setText("Choose a die face");
        getCurrentPlayerSocket().writeMessage(msg);

        Message rxMsg = readMessage(game.getActualPlayer().getPlayerID());
        return Die.getFaceByString(rxMsg.text);
    }

    private void informEveryOtherClient(){
        Message msg = setGameStatus();
        msg.setText("Waiting for your turn");
        msg.setOperation(Message.Action.INFO);
        msg.setText("This is " + getCurrentPlayer().getName() + "'s turn, please wait for yours");
        getOtherPlayers().forEach(s -> s.writeMessage(msg));
    }

    @Override
    public void showBustMessage() {
        printMessage("## BUST! ##");
    }

    @Override
    public String getInputString() {
        return null;
    }

    public Message readMessage(int playerId){
        return sockets.get(playerId).readReceivedMessage();
    }

    private Message setGameStatus(){
        Message msg = new Message();
        msg.setActualPlayer(game.getActualPlayer());
        msg.setPlayers(game.getPlayers());
        msg.setDice(game.getDice());
        msg.setBoardTiles(game.getBoardTiles());
        return msg;
    }

    private int getCurrentPlayerId(){
        if(game.getActualPlayer()==null)
            return 0;
        return game.getActualPlayer().getPlayerID();
    }

    private Player getCurrentPlayer(){
        return this.game.getPlayers()[(getCurrentPlayerId())];
    }

    private SocketHandler getCurrentPlayerSocket(){
        return sockets.get(getCurrentPlayerId());
    }

    public List<SocketHandler> getOtherPlayers(){
        return sockets.stream().filter(p -> p.getPlayerID() != game.getActualPlayer().getPlayerID()).toList();
    }

}
