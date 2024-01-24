package Heckmeck;

import Heckmeck.Components.Player;

public interface Rules{
    boolean validNumberOfPlayer(int numberOfPlayer);
    Player whoIsTheWinner(Player[] players);
    boolean isNameAlreadyPicked(String name, Player[] playersList);
}
