package TCP.Server;

import Heckmeck.*;
import TCP.Message;

import java.util.List;

public class TCPIOHandler implements IOHandler {


    private final List<SocketHandler> sockets;
    private final Message msg = Message.generateMessage();


    public TCPIOHandler(List<SocketHandler> sockets){
        this.sockets = sockets;
    }

    private void sendBroadCast(Message msg){
        sockets.forEach(client -> client.writeMessage(msg));
    }

    @Override
    public void printMessage(String text) {
        msg.setText(text);
        msg.setOperation(Message.Action.INFO);
        sendBroadCast(msg);
    }

    @Override
    public void printError(String text){
        msg.setText(text);
        msg.setOperation(Message.Action.ERROR);
        sendBroadCast(msg);
    }

    @Override
    public String askIPToConnect() {
        return null;
    }

    @Override
    public boolean wantToPlayAgain() {
        return false;
    }



    @Override
    public void showTurnBeginConfirm(Player currentPlayer) {
        
        informEveryOtherClient(currentPlayer);
        msg.setOperation(Message.Action.ASK_CONFIRM);
        msg.setText(currentPlayer.getName() + ", press enter to start your turn");
        informPlayer(currentPlayer, msg);
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
        
        msg.setOperation(Message.Action.GET_PLAYER_NAME);
        msg.setText("Choose player name");
        msg.setPlayerID(playerID);
        sockets.get(playerID).writeMessage(msg);
        return readMessage(playerID).text;
    }

    @Override
    public void showBoardTiles(BoardTiles boardTiles) {
        
        msg.setBoardTiles(boardTiles);
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
    public boolean wantToPick(Player currentPlayer, int actualDiceScore, int availableTileNumber) {
        
        msg.setOperation(Message.Action.GET_INPUT);
        msg.setText("Do you want to pick tile n. " + availableTileNumber + "?");
        sendBroadCast(msg);
        Message rxMsg = readMessage(currentPlayer.getPlayerID());
        return "y".equalsIgnoreCase(rxMsg.text);  // TODO verificare il metodo di check
    }
    @Override
    public boolean wantToSteal(Player currentPlayer, Player robbedPlayer) {
        msg.setOperation(Message.Action.GET_INPUT);
        msg.setText("Do you want to steal?");
        sockets.stream().forEach(client -> client.writeMessage(msg));
        Message rxMsg = readMessage(robbedPlayer.getPlayerID());
        return "y".equalsIgnoreCase(rxMsg.text);
    }

    @Override
    public void showPlayerData(Player currentPlayer, Dice dice, Player[] players) {
        msg.setDice(dice);
        msg.setPlayers(players);
        msg.setActualPlayer(currentPlayer);
        msg.setOperation(Message.Action.UPDATE_PLAYER);
        sockets.stream().forEach(client -> client.writeMessage(msg));
    }

    @Override
    public Die.Face chooseDie(Player currentPlayer, Dice dice) {
        informEveryOtherClient(currentPlayer);
        msg.setOperation(Message.Action.GET_INPUT);
        msg.setText("Choose a die face");
        msg.setActualPlayer(currentPlayer);
        informPlayer(currentPlayer, msg);
        Message rxMsg = readMessage(currentPlayer.getPlayerID());
        return Die.getFaceByString(rxMsg.text);
    }

    private void informEveryOtherClient(Player currentPlayer){
        Message msg = Message.generateMessage();
        msg.setOperation(Message.Action.INFO);
        msg.setText("This is " + currentPlayer.getName() + "'s turn, please wait for yours");
        getOtherPlayersSockets(currentPlayer).forEach(s -> s.writeMessage(msg));
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
        return Message.generateMessage();
    }

    private SocketHandler getPlayerSocket(Player currentPlayer){
        return sockets.get(currentPlayer.getPlayerID());
    }

    public List<SocketHandler> getOtherPlayersSockets(Player currentPlayer){
        return sockets.stream().filter(p -> p.getPlayerID() != currentPlayer.getPlayerID()).toList();
    }

    private void informPlayer(Player player, Message msg) {
        getPlayerSocket(player).writeMessage(msg);
    }
}
