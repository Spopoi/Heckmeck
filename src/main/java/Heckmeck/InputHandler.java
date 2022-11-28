package Heckmeck;

import java.io.IOException;

public interface InputHandler{

    public int chooseNumberOfPlayers();
    public String choosePlayerName();

    public int chooseDiceNumber();

    public boolean wantToPick();

    public void pressAnyKey() throws IOException;
}
