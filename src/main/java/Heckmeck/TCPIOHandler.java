package Heckmeck;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

public class TCPIOHandler implements IOHandler{

    Gson gson = new Gson();


    GameServer gameServer;
    private Message message = new Message();

    public TCPIOHandler(GameServer gameServer){
        this.gameServer = gameServer;
    }

    @Override
    public void printMessage(String text) {
        gameServer.clients.stream().forEach(client -> client.writeLine(text));
    }

    @Override
    public void showTurnBeginConfirm(String playerName) {

    }

    @Override
    public void showWelcomeMessage() {

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
        message.setOperation(Message.Action.GET_INPUT);
        message.setText("Choose player name");
        printMessage(gson.toJson(message));
        waitOneSec();
        Message msg = readMessage(playerNumber);
        return msg.text;
    }

    @Override
    public void showBoardTiles(BoardTiles boardTiles) {

        message.setOperation(Message.Action.UPDATE_TILES);
        message.setBoardTiles(boardTiles);
        printMessage(gson.toJson(message));

    }

    @Override
    public boolean wantToPick(int diceScore) {
        return false;
    }

    @Override
    public boolean wantToSteal(Player robbedPlayer) {
        return false;
    }

    @Override
    public void showPlayerData(Player player, Dice dice, Player[] players) {
        message.setActualPlayer(player);
        message.setDice(dice);
        message.setPlayers(players);
        message.setOperation(Message.Action.UPDATE_PLAYER);
        printMessage(gson.toJson(message));
    }

    @Override
    public Die.Face chooseDie(Dice dice) {
        return null;
    }

    @Override
    public void showBustMessage() {

    }

    public String readRxBuffer(int playerId){
        return (gameServer.clients.get(playerId).readReceivedMessage());
    }

    public Message readMessage(int playerId){
        return gson.fromJson(gameServer.clients.get(playerId).readReceivedMessage(), Message.class);
    }

    public void showDice(Dice dice){
        message.boardTiles = null;
        message.setOperation(Message.Action.UPDATE_PLAYER);
        message.setDice(dice);
        printMessage(gson.toJson(message));
    }

}
