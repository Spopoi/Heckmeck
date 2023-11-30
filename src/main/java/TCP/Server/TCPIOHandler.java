package TCP.Server;

import Heckmeck.*;
import TCP.Message;

import java.util.List;

public class TCPIOHandler implements IOHandler {

    GameServer gameServer;

    public TCPIOHandler(GameServer gameServer){
        this.gameServer = gameServer;
    }

    private void sendBroadCast(Message msg){
        gameServer.sockets.forEach(client -> client.writeMessage(msg));
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
        msg.setText("Waiting for your turn");
        msg.setOperation(Message.Action.INFO);
        informEveryOtherClient(msg);
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
        return gameServer.getNumOfPlayers();
    }

    @Override
    public String choosePlayerName(int playerNumber) {

        Message message = new Message();
        message.setActualPlayer(gameServer.game.getActualPlayer());
        message.setOperation(Message.Action.GET_PLAYER_NAME);
        message.setText("Choose player name");
        message.setPlayerID(playerNumber);
        gameServer.sockets.get(playerNumber).writeMessage(message);
        return readMessage(playerNumber).text;
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
        msg.setActualPlayer(gameServer.game.getActualPlayer());
        msg.setOperation(Message.Action.GET_INPUT);
        msg.setPlayerID(getCurrentPlayerId());
        msg.setText("Do you want to pick tile n. " + gameServer.game.getDice().getScore() + "?");
        sendBroadCast(msg);
        Message rxMsg = readMessage(gameServer.game.getActualPlayer().getPlayerID());
        return "y".equalsIgnoreCase(rxMsg.text);  // TODO verificare il metodo di check
    }
    @Override
    public boolean wantToSteal(Player robbedPlayer) {
        Message msg = new Message();
        msg.setActualPlayer(gameServer.game.getActualPlayer());
        msg.setPlayerID(getCurrentPlayerId());
        msg.setOperation(Message.Action.GET_INPUT);
        msg.setText("Do you want to steal?");
        gameServer.sockets.stream().forEach(client -> client.writeMessage(msg));
        Message rxMsg = readMessage(gameServer.game.getActualPlayer().getPlayerID());
        return "y".equalsIgnoreCase(rxMsg.text);    }

    @Override
    public void showPlayerData(Player player, Dice dice, Player[] players) {
        Message msg = new Message();
        msg.setActualPlayer(player);
        msg.setPlayerID(getCurrentPlayerId());
        msg.setDice(dice);
        msg.setPlayers(players);
        msg.setOperation(Message.Action.UPDATE_PLAYER);
        gameServer.sockets.stream().forEach(client -> client.writeMessage(msg));

    }

    @Override
    public Die.Face chooseDie(Dice dice) {
        Message msg = new Message();
        msg.setActualPlayer(gameServer.game.getActualPlayer());
        msg.setPlayerID(getCurrentPlayerId());
        informEveryOtherClient(msg);
        msg.setOperation(Message.Action.GET_INPUT);
        msg.setText("Choose a die face");
        getCurrentPlayerSocket().writeMessage(msg);

        Message rxMsg = readMessage(gameServer.game.getActualPlayer().getPlayerID());
        return Die.getFaceByString(rxMsg.text);
    }

    private void informEveryOtherClient(Message msg){
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
        return gameServer.sockets.get(playerId).readReceivedMessage();
    }

    private Message setGameStatus(){
        Message msg = new Message();
        msg.setActualPlayer(gameServer.game.getActualPlayer());
        msg.setPlayers(gameServer.game.getPlayers());
        msg.setDice(gameServer.game.getDice());
        msg.setBoardTiles(gameServer.game.getBoardTiles());
        return msg;
    }

    private int getCurrentPlayerId(){
        if(gameServer.game.getActualPlayer()==null)
            return 0;
        return gameServer.game.getActualPlayer().getPlayerID();
    }

    private Player getCurrentPlayer(){
        return this.gameServer.game.getPlayers()[(getCurrentPlayerId())];
    }

    private SocketHandler getCurrentPlayerSocket(){
        return gameServer.sockets.get(getCurrentPlayerId());
    }

    private List<SocketHandler> getOtherPlayers(){
        return gameServer.sockets.stream().filter(p -> p.playerId != gameServer.game.getActualPlayer().getPlayerID()).toList();
    }

}
