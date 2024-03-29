package TCP;


import Heckmeck.*;
import Heckmeck.Components.BoardTiles;
import Heckmeck.Components.Dice;
import Heckmeck.Components.Die;
import Heckmeck.Components.Player;
import TCP.Server.ClientHandler;


import java.util.ArrayList;
import java.util.List;



public class TCPIOHandler implements IOHandler {
    private final List<ClientHandler> clients;
    private final List<Thread> threads;
    public TCPIOHandler(List<ClientHandler> clients){
        this.clients = clients;
        threads = new ArrayList<>(clients.size());
        initClients();
    }
    private void sendBroadCast(Message msg){
        clients.forEach(client -> client.writeMessage(msg));
    }
    public void initClients() {
        clients.forEach(socket -> {
            Thread t = new Thread(socket);
            threads.add(t);
            t.start();
        });
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
    public void backToMenu() {
        sendBroadCast(  Message.generateMessage().
                        setOperation(Message.Action.BACK_TO_MENU)

        );

    }
    @Override
    public void showTurnBeginConfirm(Player currentPlayer) {
        informEveryOtherClient(currentPlayer);
        informPlayer(
                currentPlayer,
                Message.generateMessage().
                        setOperation(Message.Action.BEGIN_TURN)
        );
    }
    @Override
    public int chooseNumberOfPlayers() {
        return clients.size();
    }
    @Override
    public String choosePlayerName(Player player) {
        informEveryOtherClient(player);
        Message resp = informPlayer(
                        player,
                        Message.generateMessage().
                                setOperation(Message.Action.GET_PLAYER_NAME).
                                setCurrentPlayer(player)
                );
        return resp.text;
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
    public void showRolledDice(Dice dice) {
        sendBroadCast(
                Message.generateMessage().
                        setOperation(Message.Action.UPDATE_DICE).
                        setDice(dice)
        );
    }
    @Override
    public boolean wantToPick(Player currentPlayer, int actualDiceScore, int availableTileNumber) {
        return informPlayer(currentPlayer,
                    Message.generateMessage().
                    setOperation(Message.Action.WANT_PICK).
                    setCurrentPlayer(currentPlayer).
                    setScore(actualDiceScore).
                    setAvailableTileNumber(availableTileNumber)
                ).decision;
    }
    @Override
    public boolean wantToSteal(Player currentPlayer, Player robbedPlayer) {
        return informPlayer(currentPlayer,
                    Message.generateMessage().
                        setOperation(Message.Action.WANT_STEAL).
                        setCurrentPlayer(currentPlayer).
                        setRobbedPlayer(robbedPlayer)
                ).decision;
    }
    @Override
    public void showPlayerData(Player currentPlayer, Dice dice, Player[] players) {
        sendBroadCast(
                Message.generateMessage().
                        setOperation(Message.Action.UPDATE_PLAYER).
                        setDice(dice).
                        setPlayers(players).
                        setCurrentPlayer(currentPlayer)
        );
    }
    @Override
    public Die.Face chooseDie(Player currentPlayer) {
        Message msg = informPlayer(
                currentPlayer,
                Message.generateMessage().
                        setOperation(Message.Action.CHOOSE_DICE).
                        setCurrentPlayer(currentPlayer)
        );
        return Die.Face.valueOf(msg.text);
    }
    private void informEveryOtherClient(Player currentPlayer){
        getOtherPlayersSockets(currentPlayer).
                forEach(s -> s.writeMessage(
                                Message.generateMessage().
                                        setOperation(Message.Action.WAIT).
                                        setCurrentPlayer(currentPlayer)
                        )
                );
    }
    @Override
    public void showBustMessage() {
        sendBroadCast(
                Message.generateMessage().
                        setOperation(Message.Action.BUST)
        );
    }
    public List<ClientHandler> getOtherPlayersSockets(Player currentPlayer){
        return clients.stream().filter(p -> p.getPlayerID() != currentPlayer.getPlayerID()).toList();
    }
    private Message informPlayer(Player player, Message msg){
        int playerID = player.getPlayerID();
        try {
            threads.get(playerID).join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return clients.get(playerID).writeMessage(msg.setCurrentPlayer(player));
    }
}