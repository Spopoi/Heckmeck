package CLI;
import Heckmeck.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class CliOutputHandler implements OutputHandler {

    private static final String[] faceOne = {   "┊         ┊ ",
                                                "┊    ◎    ┊ ",
                                                "┊         ┊ "  };

    private static final String[] faceTwo = {   "┊      ◎  ┊ ",
                                                "┊         ┊ ",
                                                "┊  ◎      ┊ "  };

    private static final String[] faceThree ={  "┊      ◎  ┊ ",
                                                "┊    ◎    ┊ ",
                                                "┊  ◎      ┊ "  };

    private static final String[] faceFour = {  "┊  ◎   ◎  ┊ ",
                                                "┊         ┊ ",
                                                "┊  ◎   ◎  ┊ "  };

    private static final String[] faceFive = {  "┊  ◎   ◎  ┊ ",
                                                "┊    ◎    ┊ ",
                                                "┊  ◎   ◎  ┊ "  };

    private static final String[] faceWorm = {      "┊   \033[0;31m\\=\\\033[0m   ┊ ",
                                                    "┊   \033[0;31m/=/\033[0m   ┊ ",
                                                    "┊   \033[0;31m\\=\\\033[0m   ┊ "  };

    private static final Map<Die.Face, String[]> dieToString =
            Collections.unmodifiableMap(new HashMap<Die.Face, String[]>() {{
                put(Die.Face.ONE,   faceOne);
                put(Die.Face.TWO,   faceTwo);
                put(Die.Face.THREE, faceThree);
                put(Die.Face.FOUR,  faceFour);
                put(Die.Face.FIVE,  faceFive);
                put(Die.Face.WORM,  faceWorm);
            }});

    private static final String[] oneWorm = {"\033[0;93m│\033[0m  \033[0;31m~\033[0m   \033[0;93m│\033[0m", "\033[0;93m│      │\033[0m"};
    private static final String[] twoWorms = {"\033[0;93m│\033[0m  \033[0;31m~~\033[0m  \033[0;93m│\033[0m", "\033[0;93m│      │\033[0m"};
    private static final String[] threeWorms = {"\033[0;93m│\033[0m  \033[0;31m~~\033[0m  \033[0;93m│\033[0m", "\033[0;93m│\033[0m  \033[0;31m~\033[0m   \033[0;93m│\033[0m"};
    private static final String[] fourWorms = {"\033[0;93m│\033[0m  \033[0;31m~~\033[0m  \033[0;93m│\033[0m", "\033[0;93m│\033[0m  \033[0;31m~~\033[0m  \033[0;93m│\033[0m"};

    private static final Map<Integer, String[]> tileToString =
            Collections.unmodifiableMap(new HashMap<Integer, String[]>() {{
                put(21, oneWorm);
                put(22, oneWorm);
                put(23, oneWorm);
                put(24, oneWorm);
                put(25, twoWorms);
                put(26, twoWorms);
                put(27, twoWorms);
                put(28, twoWorms);
                put(29, threeWorms);
                put(30, threeWorms);
                put(31, threeWorms);
                put(32, threeWorms);
                put(33, fourWorms);
                put(34, fourWorms);
                put(35, fourWorms);
                put(36, fourWorms);
            }});
    private static final String newLine = System.lineSeparator();

    private static void print(String message){
        System.out.println(message);
    }

    @Override
    public void showDice(Dice dice) throws IOException {    //TODO Cambiare concatenaz. con stringBuilder
        String topRow = "";
        String firstRow = "";
        String secondRow = "";
        String thirdRow = "";
        String bottomRow = "";
        for(Die die : dice.getDiceList()){
            topRow      += decodeText(getTopDieRow());
            firstRow    += decodeText(getFirstDieRow(die));
            secondRow   += decodeText(getSecondDieRow(die));
            thirdRow    += decodeText(getThirdDieRow(die));
            bottomRow   += decodeText(getDieBottomRow());
        }
        print(topRow + newLine + firstRow + newLine + secondRow + newLine + thirdRow + newLine + bottomRow+ newLine);
    }

    @Override
    public void showTiles(BoardTiles boardTiles) throws IOException{ //TODO Cambiare concatenaz. con stringBuilder
        String topRow = "";
        String firstRow = "";
        String secondRow = "";
        String thirdRow = "";
        String bottomRow = "";
        for(Tile tile : boardTiles.getTiles()){
            topRow      += decodeText(getTopTilesRow());
            firstRow    += decodeText(getFirstTilesRow(tile));
            secondRow   += decodeText(getSecondTileRow(tile));
            thirdRow    += decodeText(getTilesThirdRow(tile));
            bottomRow   += decodeText(getBottomTilesRow());
        }
        print("The available tiles on the board now are:" + newLine + topRow + newLine +
                firstRow + newLine + secondRow + newLine + thirdRow + newLine + bottomRow + newLine);
    }

    @Override
    public void showPlayerData(Player player, Dice dice) throws IOException {
        Tile tile = player.getLastPickedTile();

        String displayString = decodeText( "        " + player.getName() + "'s tiles:  ");
        String chosenDiceString = decodeText( "     Chosen dice: " + dice.getChosenDiceString());
        String chosenDiceScore = decodeText( "     Current dice score: " + dice.getScore());
        String wormPresent = decodeText( "     WORM is chosen: " + dice.isWormChosen());

        String topRow = decodeText(String.format("%1$"+ displayString.length() + "s", displayString ) + getTopTilesRow() + chosenDiceString);
        String format = String.format("%1$" + displayString.length() + "s", "");
        String firstRow =decodeText(format + getFirstTilesRow(tile) + chosenDiceScore);
        String secondRow =decodeText(format + getSecondTileRow(tile) + wormPresent);
        String thirdRow =decodeText(format + getTilesThirdRow(tile));
        String bottomRow =decodeText(format + getBottomTilesRow());

        print(topRow + newLine + firstRow + newLine + secondRow + newLine + thirdRow + newLine + bottomRow + newLine);
    }

//    @Override
//    public void showPlayerTile(Player player){
//        Tile tile = player.getLastPickedTile();
//        String displayString = "        " +player.getName();
//        displayString += "'s tiles:  ";
//
//        String topRow = String.format("%1$"+ displayString.length() + "s", displayString ) + getTopTilesRow();
//        String format = String.format("%1$" + displayString.length() + "s", "");
//
//        String firstRow = format + getFirstTilesRow(tile);
//        String secondRow = format + getSecondTileRow(tile);
//        String thirdRow = format + getTilesThirdRow(tile);
//        String bottomRow = format + getBottomTilesRow();
//
//        print(topRow + newLine + firstRow + newLine + secondRow + newLine + thirdRow + newLine + bottomRow + newLine);
//    }

    @Override
    public void showWelcomeMessage(){
        print(getLogo());
        print("                      Welcome in Heckmeck");
    }

    @Override
    public void askForNumberOfPlayers(){
        print("Choose number of players between 2 and 7:");
    }

    @Override
    public void showSetPlayerName(int playerNumber){
        print("Insert the name for player" + playerNumber);
    }

    @Override
    public void showDiceChoice() {
        print("Pick one unselected face");
    }

    @Override
    public void showExceptionMessage(Exception ex) {
        print(ex.getMessage());
    }

    @Override
    public void showWantToPick() {
        print("Do you want to pick the tile?" + newLine + "Press 'y' for picking the tile or 'n' for rolling dice");
    }

    @Override
    public void showBustMessage() {
        print("#####################" +newLine+ "# BUUUUUSSSTTTTTT!! #" +newLine + "#####################" +newLine);
    }

    @Override
    public void showPlayerScore(Player actualPlayer, Dice dice) {
        print("Actual player '" + actualPlayer.getName() + "' score = " + dice.getScore());
    }

    //@Override
    public void showTurnBeginConfirm(Player actualPlayer){
        String message = " hit enter to start your turn #";
        String separator = "#".repeat(actualPlayer.getName().length()).concat(
                "#".repeat(message.length()+2));

        print(separator);
        print("# " + actualPlayer.getName() + message );
        print(separator);
    }

    @Override
    public void showWantToSteal() {
        print("Do you want to steal? Press 'y' for stealing or 'n' for continuing your turn:");
    }

    @Override
    public void showWinnerPlayerMessage(Player winnerPlayer) {
        print("Congratulation to "+winnerPlayer.getName() + "!" + newLine+ "You are the WINNER!!");
    }

    @Override
    public void showAlreadyPickedDice() {
        print("You have already chose this face, pick another one!");
    }

    @Override
    public void showFaceNotPresentMessage() {
        print("This face is not present.. Pick another one!");
    }

    @Override
    public void showAlreadyPickedName() {
        print("Already picked name.. Please choose another one");
    }

    @Override
    public void showWantToPlay() {
        print("Do you want to start a new game? y/n");
    }

    public static void drawSingleDie(Die die){
        String dieString = getTopDieRow();
        dieString += System.lineSeparator();
        dieString+= getFirstDieRow(die);
        dieString += System.lineSeparator();
        dieString+= getSecondDieRow(die);
        dieString += System.lineSeparator();
        dieString += getThirdDieRow(die);
        dieString += System.lineSeparator();
        dieString += getDieBottomRow();
        dieString += System.lineSeparator();

        print(dieString);
    }

    public static String getTopDieRow(){
        return "┌---------┐ ";
    }
    public static String getFirstDieRow(Die die){
        return dieToString.get(die.getDieFace())[0];
    }
    public static String getSecondDieRow(Die die){
        return dieToString.get(die.getDieFace())[1];
    }
    public static String getThirdDieRow(Die die){
        return dieToString.get(die.getDieFace())[2];
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
            return "\033[0;93m│\033[0m  " + tile.getNumber() + "  \033[0;93m│\033[0m";
        }
        else{
            return "│  no  │";
        }
    }
    private static String getSecondTileRow(Tile tile){
        if (tile != null){
            return tileToString.get(tile.getNumber())[0];
        }
        else{
            return "│ tile │";
        }
    }
    private static  String getTilesThirdRow(Tile tile){
        if (tile != null){
            return tileToString.get(tile.getNumber())[1];
        }
        else{
            return "│      │";
        }
    }

static String getLogo(){
        return """
                        >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                        ,--.  ,--.             ,--.                           ,--.
                        |  '--'  | ,---.  ,---.|  |,-. ,--,--,--. ,---.  ,---.|  |,-.
                        |  .--.  || .-. :| .--'|     / |        || .-. :| .--'|     /
                        |  |  |  |\\   --.\\ `--.|  \\  \\ |  |  |  |\\   --.\\ `--.|  \\  \\
                        `--'  `--' `----' `---'`--'`--'`--`--`--' `----' `---'`--'`--'
                        <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                        """;
    }

    static String decodeText(String input) throws IOException {
        return
                new BufferedReader(
                        new InputStreamReader(
                                new ByteArrayInputStream(input.getBytes()),
                                StandardCharsets.UTF_8))
                        .readLine();
    }
}
