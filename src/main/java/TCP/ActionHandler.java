package TCP;

import Heckmeck.IOHandler;

public class ActionHandler {
    private final Client client;
    IOHandler io;

    public ActionHandler(Client client) {
        this.client = client;
        this.io = client.getIo();
    }

    void performInit(Message rxMessage) {
        int playerID = rxMessage.id;
        client.sendMessage(
                Message.generateMessage().
                        setOperation(Message.Action.RESPONSE).
                        setPlayerID(playerID)
        );
    }

    public void perform_play_again(Message rxMessage) {
        client.sendMessage(
                Message.generateMessage().
                        setOperation(Message.Action.RESPONSE).
                        setDecision(io.wantToPlayAgain())
        );
    }

    public void perform_choose_dice(Message rxMessage) {
        client.sendMessage(
                Message.generateMessage().
                        setOperation(Message.Action.RESPONSE).
                        setText(io.chooseDie(rxMessage.currentPlayer).toString())
        );
    }

    void perform_ask_confirm(Message rxMessage) {
        io.showTurnBeginConfirm(rxMessage.currentPlayer);
        client.sendMessage(
                Message.generateMessage().
                        setOperation(Message.Action.RESPONSE).
                        setText("nothing")
        );
    }

    public void perform_get_player_name(Message rxMessage) {
        String playerName = io.choosePlayerName(rxMessage.currentPlayer);
        client.sendMessage(
                Message.generateMessage().
                        setOperation(Message.Action.RESPONSE).
                        setText(playerName)
        );
        io.printMessage("You chose " + playerName + ", wait for other players!");
    }

    public void performWantPick(Message rxMessage) {
        client.sendMessage(
                Message.generateMessage().
                        setOperation(Message.Action.RESPONSE).
                        setDecision(io.wantToPick(rxMessage.currentPlayer, rxMessage.diceScore, rxMessage.availableTileNumber))
        );

    }

    public void performWantSteal(Message rxMessage) {
        client.sendMessage(
                Message.generateMessage().
                        setOperation(Message.Action.RESPONSE).
                        setDecision(io.wantToSteal(rxMessage.currentPlayer, rxMessage.robbedPlayer))
        );
    }

    void perform_info(Message rxMessage) {
        sendAck();
        client.getIo().printMessage(rxMessage.text);

    }

    void perform_error(Message rxMessage) {
        sendAck();
        io.printError(rxMessage.text);
    }

    void perform_update_player(Message rxMessage) {
        sendAck();
        io.showPlayerData(rxMessage.currentPlayer, rxMessage.dice, rxMessage.players);
    }

    void perform_update_tiles(Message rxMessage) {
        sendAck();
        io.showBoardTiles(rxMessage.boardTiles);
    }

    void perform_update_dice(Message rxMessage) {
        sendAck();
        io.showRolledDice(rxMessage.dice);
    }

    public void sendAck() {
        client.sendMessage(
                Message.generateMessage().
                        setOperation(Message.Action.ACK)
        );
    }

    public void handleDefault(Message rxMessage) {
    }

    public void stopClient() {
    }
}
