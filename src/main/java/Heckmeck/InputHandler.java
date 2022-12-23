package Heckmeck;

import exception.IllegalInput;

public interface InputHandler{
    boolean wantToPlay() throws IllegalInput;
    public int chooseNumberOfPlayers() throws IllegalInput;
    public String choosePlayerName(int playerNumber) throws IllegalInput;
    public Die.Face chooseDiceFace() throws IllegalInput ;
    public boolean wantToPick() throws IllegalInput;
    public void pressEnter();
    public boolean wantToSteal() throws IllegalInput;
}
