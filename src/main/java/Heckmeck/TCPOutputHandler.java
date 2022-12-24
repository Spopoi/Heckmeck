package Heckmeck;

import com.google.gson.Gson;

public class TCPOutputHandler implements OutputHandler{

    Gson gson = new Gson();

    enum action{

    }
    GameServer gameServer;


    public TCPOutputHandler(GameServer gameServer){
        this.gameServer = gameServer;
    }

    @Override
    public void printMessage(String message) {
        gameServer.clients.stream().forEach(client -> client.writeMessage(message));
}

    @Override
    public void showDice(Dice dice) {
        String jsonString = gson.toJson(dice);
        printMessage(jsonString);

    }

    @Override
    public void showTiles(BoardTiles boardTiles) {
        String jsonString = gson.toJson(boardTiles);
        printMessage(jsonString);

    }

    @Override
    public void showPlayerData(Player player, Dice dice, Player[] players) {
        String playerString = gson.toJson(players);
        printMessage(playerString);

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
