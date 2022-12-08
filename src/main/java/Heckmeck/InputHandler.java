package Heckmeck;

import exception.IllegalInput;

import java.io.IOException;

public interface InputHandler{

    boolean wantToPlay() throws IllegalInput;
    public int chooseNumberOfPlayers() throws IllegalInput;
    public String choosePlayerName() throws IllegalInput;
    public Die.Face chooseDiceFace() throws IllegalInput ;
    public boolean wantToPick() throws IllegalInput;
    public void pressAnyKey() throws IOException;
    public boolean wantToSteal() throws IllegalInput;
}
