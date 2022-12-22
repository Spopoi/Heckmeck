package Heckmeck;

public class TCPOutputHandler implements OutputHandler{

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

    }

    @Override
    public void showTiles(BoardTiles boardTiles) {

    }

    @Override
    public void showPlayerData(Player player, Dice dice, Player[] players) {

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
