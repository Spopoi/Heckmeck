package Heckmeck;

import com.google.gson.Gson;

public class TCPIOHandler implements IOHandler{

    Gson gson = new Gson();


    GameServer gameServer;
    private Message message = new Message();

    public TCPIOHandler(GameServer gameServer){
        this.gameServer = gameServer;
    }

    @Override
    public void printMessage(String message) {
        gameServer.clients.stream().forEach(client -> client.writeMessage(message));
    }

    @Override
    public void showTurnBeginConfirm(String playerName) {

    }

    @Override
    public void showWelcomeMessage() {

    }

    @Override
    public int chooseNumberOfPlayers() {
        return gameServer.getNumOfPlayers();
    }

    @Override
    public String choosePlayerName(int playerNumber) {
        gameServer.clients.stream().forEach(client -> client.writeMessage("GET PLAYER_NAME"));
        return readMessage(playerNumber);
    }

    @Override
    public void showBoardTiles(BoardTiles boardTiles) {
        message.setOperation(Message.Action.UPDATE);
        message.setBoardTiles(boardTiles);
        printMessage(gson.toJson(boardTiles));
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

    }

    @Override
    public Die.Face chooseDie(Dice dice) {
        return null;
    }

    @Override
    public void showBustMessage() {

    }

    public String readMessage(int playerId){
        return gameServer.clients.get(playerId).readReceivedMessage();
    }

    public void showDice(Dice dice){
        message.setOperation(Message.Action.UPDATE);
        message.setDice(dice);
        printMessage(gson.toJson(message));
    }

}
