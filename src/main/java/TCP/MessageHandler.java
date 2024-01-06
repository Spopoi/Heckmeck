package TCP;

import CLI.HeckmeckCLI;
import Heckmeck.IOHandler;

public class MessageHandler {
    IOHandler io;

    public MessageHandler(IOHandler io){
        this.io = io;
    }
    public Message ack(){
        return Message.generateMessage().
                        setOperation(Message.Action.ACK);
    }
        public  Message performInit(Message rxMessage) {
            return Message.generateMessage().
                            setOperation(Message.Action.RESPONSE);
    }

    public Message perform_get_player_name(Message rxMessage) {
        String playerName = io.choosePlayerName(rxMessage.currentPlayer);
        io.printMessage("You chose " + playerName + ", wait for other players!");
        return Message.generateMessage().
                        setOperation(Message.Action.RESPONSE).
                        setText(playerName);
    }

    public Message perform_ask_confirm(Message rxMessage) {
        io.showTurnBeginConfirm(rxMessage.currentPlayer);
        return Message.generateMessage().
                        setOperation(Message.Action.RESPONSE).
                        setText("nothing");
    }
    public Message perform_play_again(Message rxMessage) {
        return Message.generateMessage().
                        setOperation(Message.Action.RESPONSE).
                        setDecision(io.wantToPlayAgain());
    }

    public Message perform_update_tiles(Message rxMessage) {
        io.showBoardTiles(rxMessage.boardTiles);
        return ack();
    }
    public Message perform_update_dice(Message rxMessage) {
        io.showRolledDice(rxMessage.dice);
        return ack();
    }
    public Message perform_update_player(Message rxMessage) {
        io.showPlayerData(rxMessage.currentPlayer, rxMessage.dice, rxMessage.players);
        return ack();
    }
    public Message perform_error(Message rxMessage) {
        io.printError(rxMessage.text);
        return ack();
    }
    public Message perform_info(Message rxMessage) {
        io.printMessage(rxMessage.text);
        return ack();
    }
    public Message perform_choose_dice(Message rxMessage) {
        return Message.generateMessage().
                        setOperation(Message.Action.RESPONSE).
                        setText(io.chooseDie(rxMessage.currentPlayer).toString()
                        );
    }
    public Message performWantPick(Message rxMessage){
        return Message.generateMessage().
                        setOperation(Message.Action.RESPONSE).
                        setDecision(io.wantToPick(rxMessage.currentPlayer, rxMessage.diceScore, rxMessage.availableTileNumber)
                        );
    }

    public Message performWantSteal(Message rxMessage){
        return Message.generateMessage().
                        setOperation(Message.Action.RESPONSE).
                        setDecision(io.wantToSteal(rxMessage.currentPlayer, rxMessage.robbedPlayer)
                        );
    }

    public void handleDefault(Message rxMessage) {
        io.printError("Unexpected problem, operation was " + rxMessage.operation.toString());
    }

    public void stopClient(){
        io.printError("Error in receiving data. Stopping client");
        if(io.wantToPlayAgain()) HeckmeckCLI.startMenu();
        else System.exit(0);
    }
}
