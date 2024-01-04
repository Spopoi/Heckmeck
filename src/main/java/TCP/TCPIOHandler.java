package TCP;

import Utils.CLI.Utils;
import Heckmeck.*;
import Heckmeck.Components.BoardTiles;
import Heckmeck.Components.Dice;
import Heckmeck.Components.Die;
import Heckmeck.Components.Player;
import TCP.Server.ClientHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TCPIOHandler implements IOHandler {


    private final List<ClientHandler> sockets;
    private final List<Thread> threads;

    public TCPIOHandler(List<ClientHandler> sockets){
        this.sockets = sockets;
        threads = new ArrayList<>(sockets.size());
        initClients();
    }
    private void sendBroadCast(Message msg){
        sockets.forEach(client -> client.writeMessage(msg));
    }
    public void initClients() {
        sockets.forEach(socket -> {
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
    public String askIPToConnect() {
        return null;
    }

    @Override
    public boolean wantToPlayAgain() {
        return getYesOrNoAnswer(
                getHostClient().playerId,
                "Do you want to play again? (y/n)"
                );
    }
    @Override
    public void showTurnBeginConfirm(Player currentPlayer) {
        informEveryOtherClient(currentPlayer);
        informPlayer(
                currentPlayer,
                Message.generateMessage().
                        setOperation(Message.Action.BEGIN_TURN).
                        setText("Press enter to start your turn").
                        setActualPlayer(currentPlayer)
        );
    }
    @Override
    public void showWelcomeMessage() {
        printMessage(Utils.getMultyplayerPath());
    }
    @Override
    public int chooseNumberOfPlayers() {
        return sockets.size();
    }
    @Override
    public String choosePlayerName(Player player) {
        informEveryOtherClient(player);
        Message resp = informPlayer(
                        player,
                        Message.generateMessage().
                                setOperation(Message.Action.GET_PLAYER_NAME).
                                setText("Choose player name").
                                setPlayerID(player.getPlayerID())
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
    public void askRollDiceConfirmation(Player currentPlayer) {
        informEveryOtherClient(currentPlayer);
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
        return getYesOrNoAnswer(currentPlayer.getPlayerID(), "Do you want to pick tile n. " + availableTileNumber + "?");
    }
    @Override
    public boolean wantToSteal(Player currentPlayer, Player robbedPlayer) {
        return getYesOrNoAnswer(currentPlayer.getPlayerID(), "Do you want to steal " + robbedPlayer.getName() + "' s tile?");
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
    public Die.Face chooseDie(Player currentPlayer) {
        informEveryOtherClient(currentPlayer);
        Message msg = informPlayer(
                currentPlayer,
                Message.generateMessage().
                        setOperation(Message.Action.GET_INPUT).
                        setText("Choose a die face").
                        setActualPlayer(currentPlayer)
        );
        return Die.getFaceByString(msg.text);
    }
    private void informEveryOtherClient(Player currentPlayer){
        getOtherPlayersSockets(currentPlayer).
                forEach(s -> s.writeMessage(
                                Message.generateMessage().
                                        setOperation(Message.Action.INFO).
                                        setText("This is not your turn, please wait").
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

    public List<ClientHandler> getOtherPlayersSockets(Player currentPlayer){
        return sockets.stream().filter(p -> p.getPlayerID() != currentPlayer.getPlayerID()).toList();
    }
    private Message informPlayer(Player player, Message msg) {
        return informPlayer(player.getPlayerID(), msg);
    }
    private Message informPlayer(int playerID, Message msg){
        try {
            threads.get(playerID).join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return sockets.get(playerID).writeMessage(msg.setPlayerID(playerID));
    }

    private ClientHandler getHostClient(){
        return sockets.get(0);
    }
    public boolean getYesOrNoAnswer(int playerID, String textToDisplay){
        while(true){
            String decision = informPlayer(playerID,
                    Message.generateMessage().
                            setOperation(Message.Action.GET_INPUT).
                            setText(textToDisplay)
            ).text;

            if (Objects.equals(decision, "y")) {
                return true;
            } else if (Objects.equals(decision, "n")) {
                return false;
            } else if (decision.isBlank()) {
                continue;
            } else {
                printError("Invalid input, choose between y or n");
            }
        }
    }
}