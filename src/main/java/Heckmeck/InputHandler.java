package Heckmeck;

import java.io.IOException;

public interface InputHandler{

    boolean wantToPlay();
    public int chooseNumberOfPlayers();
    public String choosePlayerName();
    public Die.Face chooseDiceNumber();
    public boolean wantToPick();
    public void pressAnyKey() throws IOException;
    public boolean wantToSteal();
}
