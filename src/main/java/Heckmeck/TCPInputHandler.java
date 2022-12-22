package Heckmeck;

import exception.IllegalInput;
import Heckmeck.GameServer;

public class TCPInputHandler implements InputHandler{
    //private GameServer gameServer = new GameServer();
    GameServer gameServer;



    public TCPInputHandler(GameServer gameserver){
        this.gameServer = gameserver;
    }

    @Override
    public boolean wantToPlay() throws IllegalInput {
        return false;
    }

    @Override
    public int chooseNumberOfPlayers(){
        try {
            return getInputNumber();
        } catch (NumberFormatException e){
            throw new IllegalInput("Invalid input, please choose a number between 1 and 7");
        }
    }

    private int getInputNumber() {
        return 0;
    }

    @Override
    public String choosePlayerName() throws IllegalInput {
        return null;
    }

    @Override
    public Die.Face chooseDiceFace() throws IllegalInput {
        return null;
    }

    @Override
    public boolean wantToPick() throws IllegalInput {
        return false;
    }

    @Override
    public void pressEnter() {

    }

    @Override
    public boolean wantToSteal() throws IllegalInput {
        return false;
    }

    public String readMessage(int playerId){
        return gameServer.clients.get(playerId).readReceivedMessage();
    }
}
