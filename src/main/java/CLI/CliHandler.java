package CLI;
import Heckmeck.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CliHandler {

    public static void main(String args[]){
        Dice dice = Dice.generateDice();
        dice.rollDice();
        dice.addSpecificDie(Die.face.ONE);
        dice.chooseDice(Die.face.ONE);
        dice.addSpecificDie(Die.face.THREE);
        dice.chooseDice(Die.face.THREE);
        dice.addSpecificDie(Die.face.WORM);
        //dice.chooseDice(Die.face.WORM);

        List<Die> diceList = dice.getDiceList();

        String diceString = drawDice(diceList);
        //System.out.print(diceString);

        Tiles board = Tiles.init();
        List <Tile> tileList = board.getTilesList();
        String initialTiles = drawTiles(tileList);
        System.out.print(initialTiles);
        Tile tile = tileList.get(5);

        Player player = Player.generatePlayer("Pippo");
        player.pickTileFromBoard(tile, board);
        //player.pickTileFromBoard(tileList.get(9), board);
        tileList = board.getTilesList();
        String boardString = drawTiles(tileList);

        String playerData = drawPlayerData(player, dice);
        System.out.print(boardString);
        System.out.print(playerData);
    }

    private static final Map<Die.face, String> dieToFirstRow =
            Collections.unmodifiableMap(new HashMap<Die.face, String>() {{
                put(Die.face.ONE,   "┊         ┊ ");
                put(Die.face.TWO,   "┊      ◎  ┊ ");
                put(Die.face.THREE, "┊      ◎  ┊ ");
                put(Die.face.FOUR,  "┊  ◎   ◎  ┊ ");
                put(Die.face.FIVE,  "┊  ◎   ◎  ┊ ");
                put(Die.face.WORM,  "┊   \\=\\   ┊ ");
            }});
    private static final Map<Die.face, String> dieToSecondRow =
            Collections.unmodifiableMap(new HashMap<Die.face, String>() {{
                put(Die.face.ONE,   "┊    ◎    ┊ ");
                put(Die.face.TWO,   "┊         ┊ ");
                put(Die.face.THREE, "┊    ◎    ┊ ");
                put(Die.face.FOUR,  "┊         ┊ ");
                put(Die.face.FIVE,  "┊    ◎    ┊ ");
                put(Die.face.WORM,  "┊   /=/   ┊ ");
            }});
    private static final Map<Die.face, String> dieToThirdRow =
            Collections.unmodifiableMap(new HashMap<Die.face, String>() {{
                put(Die.face.ONE,   "┊         ┊ ");
                put(Die.face.TWO,   "┊  ◎      ┊ ");
                put(Die.face.THREE, "┊  ◎      ┊ ");
                put(Die.face.FOUR,  "┊  ◎   ◎  ┊ ");
                put(Die.face.FIVE,  "┊  ◎   ◎  ┊ ");
                put(Die.face.WORM,  "┊   \\=\\   ┊ ");
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


    public static String drawDice(List <Die> diceList){
        String topRow = "";
        String firstRow = "";
        String secondRow = "";
        String thirdRow = "";
        String bottomRow = "";
        for(Die die : diceList){
            topRow      += getDieTopRow();
            firstRow    += getFirstDieRow(die);
            secondRow   += getSecondDieRow(die);
            thirdRow    += getThirdDieRow(die);
            bottomRow   += getDieBottomRow();
        }
        return topRow + "\n" + firstRow + "\n" + secondRow + "\n" + thirdRow + "\n" + bottomRow + "\n";
    }

    public static String drawSingleDie(Die die){
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

        return toPrint;

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

    public static void getDiceList(){
       // List <Dice> diceList
    }


    public String drawPlayerTile(Player player) {
        Tile tile = player.getLastPickedTile();
        String displayString = "        " +player.getName();
        displayString += "'s tiles:  ";


        String topRow = String.format("%1$"+ displayString.length() + "s", displayString ) + getTopTilesRow();
        String firstRow =String.format("%1$"+ displayString.length() + "s", "" ) + getFirstTilesRow(tile);
        String secondRow =String.format("%1$"+ displayString.length() + "s", "" ) + getSecondTileRow(tile);
        String thirdRow =String.format("%1$"+ displayString.length() + "s", "" ) + getTilesThirdRow(tile);
        String bottomRow =String.format("%1$"+ displayString.length() + "s", "" ) + getBottomTilesRow();

        return topRow + "\n" + firstRow + "\n" + secondRow + "\n" + thirdRow + "\n" + bottomRow + "\n";
    }
    public static String drawPlayerData(Player player, Dice dice) {
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

        return topRow + "\n" + firstRow + "\n" + secondRow + "\n" + thirdRow + "\n" + bottomRow + "\n";

    }

    public static String drawTiles(List<Tile> tileList){
        String topRow = "";
        String firstRow = "";
        String secondRow = "";
        String thirdRow = "";
        String bottomRow = "";
        for(Tile tile : tileList){
            topRow      += getTopTilesRow();
            firstRow    += getFirstTilesRow(tile);
            secondRow   += getSecondTileRow(tile);
            thirdRow    += getTilesThirdRow(tile);
            bottomRow   += getBottomTilesRow();
        }
        return topRow + "\n" + firstRow + "\n" + secondRow + "\n" + thirdRow + "\n" + bottomRow + "\n";

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
