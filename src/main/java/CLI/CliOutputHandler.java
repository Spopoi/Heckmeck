package CLI;
import Heckmeck.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;


public class CliOutputHandler implements OutputHandler {

    private static final Map<Die.Face, String> dieToFirstRow =
            Collections.unmodifiableMap(new HashMap<Die.Face, String>() {{
                put(Die.Face.ONE,   "┊         ┊ ");
                put(Die.Face.TWO,   "┊      ◎  ┊ ");
                put(Die.Face.THREE, "┊      ◎  ┊ ");
                put(Die.Face.FOUR,  "┊  ◎   ◎  ┊ ");
                put(Die.Face.FIVE,  "┊  ◎   ◎  ┊ ");
                put(Die.Face.WORM,  "┊   \033[0;31m\\=\\\033[0m   ┊ ");
            }});
    private static final Map<Die.Face, String> dieToSecondRow =
            Collections.unmodifiableMap(new HashMap<Die.Face, String>() {{
                put(Die.Face.ONE,   "┊    ◎    ┊ ");
                put(Die.Face.TWO,   "┊         ┊ ");
                put(Die.Face.THREE, "┊    ◎    ┊ ");
                put(Die.Face.FOUR,  "┊         ┊ ");
                put(Die.Face.FIVE,  "┊    ◎    ┊ ");
                put(Die.Face.WORM,  "┊   \033[0;31m/=/\033[0m   ┊ ");
            }});
    private static final Map<Die.Face, String> dieToThirdRow =
            Collections.unmodifiableMap(new HashMap<Die.Face, String>() {{
                put(Die.Face.ONE,   "┊         ┊ ");
                put(Die.Face.TWO,   "┊  ◎      ┊ ");
                put(Die.Face.THREE, "┊  ◎      ┊ ");
                put(Die.Face.FOUR,  "┊  ◎   ◎  ┊ ");
                put(Die.Face.FIVE,  "┊  ◎   ◎  ┊ ");
                put(Die.Face.WORM,  "┊   \033[0;31m\\=\\\033[0m   ┊ ");
            }});
    private static String zero = "\033[0;93m│      │\033[0m";
    private static String one = "\033[0;93m│\033[0m  \033[0;31m~\033[0m   \033[0;93m│\033[0m";
    private static String two = "\033[0;93m│\033[0m  \033[0;31m~~\033[0m  \033[0;93m│\033[0m";

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
    public void showDice(Dice dice) throws IOException {
        String topRow = "";
        String firstRow = "";
        String secondRow = "";
        String thirdRow = "";
        String bottomRow = "";
        for(Die die : dice.getDiceList()){
            topRow      += decodeText(getTopDieRow(), "UTF-8");
            firstRow    += decodeText(getFirstDieRow(die), "UTF-8");
            secondRow   += decodeText(getSecondDieRow(die), "UTF-8");
            thirdRow    += decodeText(getThirdDieRow(die), "UTF-8");
            bottomRow   += decodeText(getDieBottomRow(), "UTF-8");
        }
        System.out.println(topRow + "\n" + firstRow + "\n" + secondRow + "\n" + thirdRow + "\n" + bottomRow + "\n");
    }

    @Override
    public void showTiles(Tiles tiles) throws IOException {
        String topRow = "";
        String firstRow = "";
        String secondRow = "";
        String thirdRow = "";
        String bottomRow = "";
        for(Tile tile : tiles.getTiles()){
            topRow      += decodeText(getTopTilesRow(), "UTF-8");
            firstRow    += decodeText(getFirstTilesRow(tile), "UTF-8");
            secondRow   += decodeText(getSecondTileRow(tile), "UTF-8");
            thirdRow    += decodeText(getTilesThirdRow(tile), "UTF-8");
            bottomRow   += decodeText(getBottomTilesRow(), "UTF-8");
        }
        System.out.println(topRow + "\n" + firstRow + "\n" + secondRow + "\n" + thirdRow + "\n" + bottomRow + "\n");
    }

    @Override
    public void showPlayerData(Player player, Dice dice) throws IOException {
        Tile tile = player.getLastPickedTile();

        String displayString = decodeText( "        " + player.getName() + "'s tiles:  ", "UTF-8");
        String chosenDiceString = decodeText( "     Chosen dice: " + dice.getChosenDiceString(), "UTF-8");
        String chosenDiceScore = decodeText( "     Current dice score: " + dice.getScore(), "UTF-8");
        String wormPresent = decodeText( "     WORM is chosen: " + dice.isWormChosen(), "UTF-8");

        String topRow = decodeText(String.format("%1$"+ displayString.length() + "s", displayString ) + getTopTilesRow() + chosenDiceString , "UTF-8");
        String firstRow =decodeText(String.format("%1$"+ displayString.length() + "s", "" ) + getFirstTilesRow(tile) + chosenDiceScore, "UTF-8");
        String secondRow =decodeText(String.format("%1$"+ displayString.length() + "s", "" ) + getSecondTileRow(tile) + wormPresent, "UTF-8");
        String thirdRow =decodeText(String.format("%1$"+ displayString.length() + "s", "" ) + getTilesThirdRow(tile), "UTF-8");
        String bottomRow =decodeText(String.format("%1$"+ displayString.length() + "s", "" ) + getBottomTilesRow(), "UTF-8");

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
        System.out.print(getLogo());
        System.out.println("                    Benvenuto in Heckmeck");
        System.out.println("            Scegli il numero di giocatori tra 1 e 6:");
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

    @Override
    public void showBustMessage() {
        System.out.println("#####################");
        System.out.println("# BUUUUUSSSTTTTTT!! #");
        System.out.println("#####################");

    }

    @Override
    public void showPlayerScore(Player actualPlayer, Dice dice) {
        System.out.println("Actual player '" + actualPlayer.getName() + "' score = " + dice.getScore());
    }

    @Override
    public void showTurnBeginConfirm(){
        System.out.println("################################");
        System.out.println("# Hit enter to start your turn #");
        System.out.println("################################");
    }

    public static void drawSingleDie(Die die){
        String toPrint = getTopDieRow();
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

    public static String getTopDieRow(){
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
        return "\033[0;93m└──────┘\033[0m";
    }

    private static String getTopTilesRow() {
        return "\033[0;93m┌──────┓\033[0m";
    }

    private static String getFirstTilesRow(Tile tile){
        if (tile != null){
            return "\033[0;93m│\033[0m  " + String.valueOf(tile.getNumber()) + "  \033[0;93m│\033[0m";
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

    static String getLogo(){
        String topRow =     ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n";
        String firstRow =   ",--.  ,--.             ,--.                           ,--.    \n";
        String secondRow =  "|  '--'  | ,---.  ,---.|  |,-. ,--,--,--. ,---.  ,---.|  |,-. \n";
        String thirdRow =   "|  .--.  || .-. :| .--'|     / |        || .-. :| .--'|     / \n";
        String fourthRow =  "|  |  |  |\\   --.\\ `--.|  \\  \\ |  |  |  \\|\\   --.\\ `--.|  \\  \\ \n";
        String fifthRow =   "`--'  `--' `----' `---'`--'`--'`--`--`--' `----' `---'`--'`--'\n";
        String bottomRow = "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<\n";
        return topRow + firstRow+secondRow+thirdRow+fourthRow+fifthRow+ bottomRow;
    }

    static String decodeText(String input, String encoding) throws IOException {
        return
                new BufferedReader(
                        new InputStreamReader(
                                new ByteArrayInputStream(input.getBytes()),
                                Charset.forName(encoding)))
                        .readLine();
    }
}
