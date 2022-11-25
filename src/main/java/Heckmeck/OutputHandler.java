package Heckmeck;

public interface OutputHandler {

    public void showDice(Dice dice);
    public void showTiles(Tiles tiles);
    public void showPlayerTile(Player player);
    public void showPlayerData(Player player, Dice dice);
    public void showMenu();
    public void showSetPlayerName();
}
