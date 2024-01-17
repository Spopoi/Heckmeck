package TCP.Client;

import Heckmeck.IOHandler;
import TCP.Message;

public class MessageHandler {
    final IOHandler io;

    public MessageHandler(IOHandler io) {
        this.io = io;
    }

    Message performInit(Message rxMessage) {
        int playerID = rxMessage.id;
        return Message.generateMessage()
                .setOperation(Message.Action.RESPONSE)
                .setPlayerID(playerID);
    }

    public Message perform_choose_dice(Message rxMessage) {
        io.chooseDie(rxMessage.currentPlayer);
        return Message.generateMessage()
                .setOperation(Message.Action.RESPONSE)
                .setText(io.chooseDie(rxMessage.currentPlayer).toString());
    }

    public Message perform_ask_confirm(Message rxMessage) {
        io.showTurnBeginConfirm(rxMessage.currentPlayer);
        return Message.generateMessage()
                .setOperation(Message.Action.RESPONSE)
                .setText("nothing");
    }

    public Message perform_get_player_name(Message rxMessage) {
        String playerName = io.choosePlayerName(rxMessage.currentPlayer);
        io.printMessage("You chose " + playerName + ", wait for other players!");
        return Message.generateMessage()
                .setOperation(Message.Action.RESPONSE)
                .setText(playerName);
    }

    public Message performWantPick(Message rxMessage) {
        return Message.generateMessage()
                .setOperation(Message.Action.RESPONSE)
                .setDecision(io.wantToPick(rxMessage.currentPlayer, rxMessage.diceScore, rxMessage.availableTileNumber));
    }

    public Message performWantSteal(Message rxMessage) {
        return Message.generateMessage()
                .setOperation(Message.Action.RESPONSE)
                .setDecision(io.wantToSteal(rxMessage.currentPlayer, rxMessage.robbedPlayer));
    }

    Message perform_info(Message rxMessage) {
        io.printMessage(rxMessage.text);
        return  ack();
    }

    public Message perform_error(Message rxMessage) {
        io.printError(rxMessage.text);
        return ack();
    }

    public Message perform_update_player(Message rxMessage) {
        io.showPlayerData(rxMessage.currentPlayer, rxMessage.dice, rxMessage.players);
        return ack();
    }

    public Message perform_update_tiles(Message rxMessage) {
        io.showBoardTiles(rxMessage.boardTiles);
        return ack();
    }

    public Message perform_update_dice(Message rxMessage) {
        io.showRolledDice(rxMessage.dice);
        return ack();
    }

    public Message ack() {
        return Message.generateMessage().setOperation(Message.Action.ACK);
    }

    public Message performBust(Message rxMessage) {
        io.showBustMessage();
        return ack();
    }

    public Message performWaitYourTurn(Message rxMessage) {
        io.printMessage("This is " + rxMessage.currentPlayer.getName() + "'s Turn, please wait");
        return ack();
    }
}
