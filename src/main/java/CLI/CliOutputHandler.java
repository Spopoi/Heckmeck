package CLI;
import Heckmeck.*;

import java.util.*;


public class CliOutputHandler implements OutputHandler {

    private static final Map<Die.Face, String> dieToFirstRow =
            Collections.unmodifiableMap(new HashMap<Die.Face, String>() {{
                put(Die.Face.ONE,   "┊         ┊ ");
                put(Die.Face.TWO,   "┊      ◎  ┊ ");
                put(Die.Face.THREE, "┊      ◎  ┊ ");
                put(Die.Face.FOUR,  "┊  ◎   ◎  ┊ ");
                put(Die.Face.FIVE,  "┊  ◎   ◎  ┊ ");
                put(Die.Face.WORM,  "┊   \\=\\   ┊ ");
            }});
    private static final Map<Die.Face, String> dieToSecondRow =
            Collections.unmodifiableMap(new HashMap<Die.Face, String>() {{
                put(Die.Face.ONE,   "┊    ◎    ┊ ");
                put(Die.Face.TWO,   "┊         ┊ ");
                put(Die.Face.THREE, "┊    ◎    ┊ ");
                put(Die.Face.FOUR,  "┊         ┊ ");
                put(Die.Face.FIVE,  "┊    ◎    ┊ ");
                put(Die.Face.WORM,  "┊   /=/   ┊ ");
            }});
    private static final Map<Die.Face, String> dieToThirdRow =
            Collections.unmodifiableMap(new HashMap<Die.Face, String>() {{
                put(Die.Face.ONE,   "┊         ┊ ");
                put(Die.Face.TWO,   "┊  ◎      ┊ ");
                put(Die.Face.THREE, "┊  ◎      ┊ ");
                put(Die.Face.FOUR,  "┊  ◎   ◎  ┊ ");
                put(Die.Face.FIVE,  "┊  ◎   ◎  ┊ ");
                put(Die.Face.WORM,  "┊   \\=\\   ┊ ");
            }});
    private static String zero = "│      │";
    private static String one = "│  ~   │";
    private static String two = "│  ~~  │";

    private static final Map<Integer, String> secondTileRowToWorms =
            Collections.unmodifiableMap(new HashMap<Integer, String>() {{
                put(21, one);
                put(22, one);
                put(23, one);
                put(24, one);
                put(25, two);
                put(26, two);
                put(27, two);
                put(28, two);
                put(29, two);
                put(30, two);
                put(31, two);
                put(32, two);
                put(33, two);
                put(34, two);
                put(35, two);
                put(36, two);
            }});    private static final Map<Integer, String> thirdTileRowToWorms =
            Collections.unmodifiableMap(new HashMap<Integer, String>() {{
                put(21, zero);
                put(22, zero);
                put(23, zero);
                put(24, zero);
                put(25, zero);
                put(26, zero);
                put(27, zero);
                put(28, zero);
                put(29, one);
                put(30, one);
                put(31, one);
                put(32, two);
                put(33, two);
                put(34, two);
                put(35, two);
                put(36, two);
            }});

    @Override
    public void showDice(Dice dice) {
        String topRow = "";
        String firstRow = "";
        String secondRow = "";
        String thirdRow = "";
        String bottomRow = "";
        for(Die die : dice.getDiceList()){
            topRow      += getDieTopRow();
            firstRow    += getFirstDieRow(die);
            secondRow   += getSecondDieRow(die);
            thirdRow    += getThirdDieRow(die);
            bottomRow   += getDieBottomRow();
        }
        System.out.println(topRow + "\n" + firstRow + "\n" + secondRow + "\n" + thirdRow + "\n" + bottomRow + "\n");
    }

    @Override
    public void showTiles(Tiles tiles) {
        String topRow = "";
        String firstRow = "";
        String secondRow = "";
        String thirdRow = "";
        String bottomRow = "";
        for(Tile tile : tiles.getTiles()){
            topRow      += getTopTilesRow();
            firstRow    += getFirstTilesRow(tile);
            secondRow   += getSecondTileRow(tile);
            thirdRow    += getTilesThirdRow(tile);
            bottomRow   += getBottomTilesRow();
        }
        System.out.println(topRow + "\n" + firstRow + "\n" + secondRow + "\n" + thirdRow + "\n" + bottomRow + "\n");
    }

    @Override
    public void showPlayerData(Player player, Dice dice) {
        Tile tile = player.getLastPickedTile();

        String displayString = "        " + player.getName() + "'s tiles:  ";
        String chosenDiceString = "     Chosen dice: " + dice.getChosenDiceString();;
        String chosenDiceScore = "     Current dice score: " + dice.getScore();
        String wormPresent = "     WORM is chosen: " + dice.isWormChosen();

        String topRow = String.format("%1$"+ displayString.length() + "s", displayString ) + getTopTilesRow() + chosenDiceString ;
        String firstRow =String.format("%1$"+ displayString.length() + "s", "" ) + getFirstTilesRow(tile) + chosenDiceScore;
        String secondRow =String.format("%1$"+ displayString.length() + "s", "" ) + getSecondTileRow(tile) + wormPresent;
        String thirdRow =String.format("%1$"+ displayString.length() + "s", "" ) + getTilesThirdRow(tile);
        String bottomRow =String.format("%1$"+ displayString.length() + "s", "" ) + getBottomTilesRow();

        System.out.println(topRow + "\n" + firstRow + "\n" + secondRow + "\n" + thirdRow + "\n" + bottomRow + "\n");
    }

    @Override
    public void showPlayerTile(Player player){
        Tile tile = player.getLastPickedTile();
        String displayString = "        " +player.getName();
        displayString += "'s tiles:  ";

        String topRow = String.format("%1$"+ displayString.length() + "s", displayString ) + getTopTilesRow();
        String firstRow =String.format("%1$"+ displayString.length() + "s", "" ) + getFirstTilesRow(tile);
        String secondRow =String.format("%1$"+ displayString.length() + "s", "" ) + getSecondTileRow(tile);
        String thirdRow =String.format("%1$"+ displayString.length() + "s", "" ) + getTilesThirdRow(tile);
        String bottomRow =String.format("%1$"+ displayString.length() + "s", "" ) + getBottomTilesRow();

        System.out.println(topRow + "\n" + firstRow + "\n" + secondRow + "\n" + thirdRow + "\n" + bottomRow + "\n");
    }

    @Override
    public void showMenu(){
        System.out.println("Benvenuto in Heckmeck");
        System.out.println("Scegli il numero di giocatori tra 1 e 6");
    }

    @Override
    public void showSetPlayerName(){
        System.out.println("Inserisci il nome del giocatore");
    }

    @Override
    public void showDiceChoice() {
        System.out.println("Pick one unselected face");
    }

    @Override
    public void showWantToPick() {
        System.out.println("Do you want to pick the tile?" + '\n' + "Press 'y' for picking the tile or 'n' for rolling dice");
    }

    public static void drawSingleDie(Die die){
        String toPrint = getDieTopRow();
        toPrint += "\n";
        toPrint+= getFirstDieRow(die);
        toPrint += "\n";
        toPrint+= getSecondDieRow(die);
        toPrint += "\n";
        toPrint += getThirdDieRow(die);
        toPrint += "\n";
        toPrint += getDieBottomRow();
        toPrint += "\n";

        System.out.println(toPrint);
    }

    public static String getDieTopRow(){
        return "┌---------┐ ";
    }
    public static String getFirstDieRow(Die die){
        return dieToFirstRow.get(die.getDieFace());
    }
    public static String getSecondDieRow(Die die){
        return dieToSecondRow.get(die.getDieFace());
    }
    public static String getThirdDieRow(Die die){
        return dieToThirdRow.get(die.getDieFace());
    }
    public static String getDieBottomRow(){
        return "└---------┘ ";
    }

    private static String getBottomTilesRow() {
        return "└──────┘";
    }

    private static String getTopTilesRow() {
        return "┌──────┓";
    }

    private static String getFirstTilesRow(Tile tile){
        if (tile != null){
            return "│  " + String.valueOf(tile.getNumber()) + "  │";
        }
        else{
            return "│  no  │";
        }
    }
    private static String getSecondTileRow(Tile tile){
        if (tile != null){
            return secondTileRowToWorms.get(tile.getNumber());
        }
        else{
            return "│ tile │";
        }
    }
    private static  String getTilesThirdRow(Tile tile){
        if (tile != null){
            return thirdTileRowToWorms.get(tile.getNumber());
        }
        else{
            return "│      │";
        }
    }
}
