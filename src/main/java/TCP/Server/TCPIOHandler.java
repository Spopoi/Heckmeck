package TCP.Server;

import Heckmeck.*;
import TCP.Message;

import java.util.List;

public class TCPIOHandler implements IOHandler {


    private final List<SocketHandler> sockets;

    public TCPIOHandler(List<SocketHandler> sockets){
        this.sockets = sockets;
    }

    private void sendBroadCast(Message msg){
        sockets.forEach(client -> client.writeMessage(msg));
    }

    @Override
    public void printMessage(String text) {
        sendBroadCast(
                Message.generateMessage().
                        setOperation(Message.Action.INFO).
                        setText(text)
        );
    }

    @Override
    public void printError(String text){
        sendBroadCast(
                Message.generateMessage().
                        setOperation(Message.Action.ERROR).
                        setText(text)
        );
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
        informPlayer(
                currentPlayer,
                Message.generateMessage().
                        setOperation(Message.Action.ASK_CONFIRM).
                        setText(currentPlayer.getName() + ", press enter to start your turn")
        );
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
    public String choosePlayerName(Player player) {
        informPlayer(
                player.getPlayerID(),
                Message.generateMessage().
                        setOperation(Message.Action.GET_PLAYER_NAME).
                        setText("Choose player name").
                        setPlayerID(player.getPlayerID())
        );
        return readMessage(player.getPlayerID()).text;
    }

    @Override
    public void showBoardTiles(BoardTiles boardTiles) {
        sendBroadCast(
                Message.generateMessage().
                        setOperation(Message.Action.UPDATE_TILES).
                        setBoardTiles(boardTiles)
        );
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
        sendBroadCast(
                Message.generateMessage().
                        setOperation(Message.Action.GET_INPUT).
                        setText("Do you want to pick tile n. " + availableTileNumber + "?").setPlayerID(currentPlayer.getPlayerID())
        );
        return "y".equalsIgnoreCase(readMessage(currentPlayer).text);
    }
    @Override
    public boolean wantToSteal(Player currentPlayer, Player robbedPlayer) {
        sendBroadCast(
                Message.generateMessage().
                        setOperation(Message.Action.GET_INPUT).
                        setText("Do you want to steal?")
        );
        return "y".equalsIgnoreCase(readMessage(currentPlayer).text);
    }

    @Override
    public void showPlayerData(Player currentPlayer, Dice dice, Player[] players) {
        sendBroadCast(
                Message.generateMessage().
                        setOperation(Message.Action.UPDATE_PLAYER).
                        setDice(dice).
                        setPlayers(players).
                        setActualPlayer(currentPlayer)
        );
    }

    @Override
    public Die.Face chooseDie(Player currentPlayer, Dice dice) {
        informEveryOtherClient(currentPlayer);
        informPlayer(
                currentPlayer,
                Message.generateMessage().
                        setOperation(Message.Action.GET_INPUT).
                        setText("Choose a die face").
                        setActualPlayer(currentPlayer)
        );
        return Die.getFaceByString(readMessage(currentPlayer).text);
    }

    private void informEveryOtherClient(Player currentPlayer){
        getOtherPlayersSockets(currentPlayer).
                forEach(s -> s.writeMessage(
                                Message.generateMessage().
                                        setOperation(Message.Action.INFO).
                                        setText("This is " + currentPlayer.getName() + "'s turn, please wait for yours").
                                        setActualPlayer(currentPlayer)
                        )
                );
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
    public Message readMessage(Player player){
        return readMessage(player.getPlayerID());
    }

    private SocketHandler getPlayerSocket(Player currentPlayer){
        return sockets.get(currentPlayer.getPlayerID());
    }

    public List<SocketHandler> getOtherPlayersSockets(Player currentPlayer){
        return sockets.stream().filter(p -> p.getPlayerID() != currentPlayer.getPlayerID()).toList();
    }

    private void informPlayer(Player player, Message msg) {
        getPlayerSocket(player).writeMessage(msg.setPlayerID(player.getPlayerID()));
    }
    private Message informPlayer(int playerID, Message msg){
        return sockets.get(playerID).writeMessage(msg);
    }
}