package CLI;
import Heckmeck.*;

import java.util.*;
import java.util.stream.Stream;


public class CliOutputHandler implements OutputHandler {

    private static final String[] faceOne = {   "|         | ",
                                                "|    o    | ",
                                                "|         | "  };

    private static final String[] faceTwo = {   "|      o  | ",
                                                "|         | ",
                                                "|  o      | "  };

    private static final String[] faceThree ={  "|      o  | ",
                                                "|    o    | ",
                                                "|  o      | "  };

    private static final String[] faceFour = {  "|  o   o  | ",
                                                "|         | ",
                                                "|  o   o  | "  };

    private static final String[] faceFive = {  "|  o   o  | ",
                                                "|    o    | ",
                                                "|  o   o  | "  };

    private static final String[] faceWorm = {      "|   \\=\\   | ",
                                                    "|   /=/   | ",
                                                    "|   \\=\\   | "  };

    private static final Map<Die.Face, String[]> dieToString =
            Collections.unmodifiableMap(new HashMap<Die.Face, String[]>() {{
                put(Die.Face.ONE,   faceOne);
                put(Die.Face.TWO,   faceTwo);
                put(Die.Face.THREE, faceThree);
                put(Die.Face.FOUR,  faceFour);
                put(Die.Face.FIVE,  faceFive);
                put(Die.Face.WORM,  faceWorm);
            }});

    private static final String[] oneWorm = {"|  ~   |", "|      |"};
    private static final String[] twoWorms = {"|  ~~  |", "|      |"};
    private static final String[] threeWorms = {"|  ~~  |", "|  ~   |"};
    private static final String[] fourWorms = {"|  ~~  |", "|  ~~  |"};

    private static final String newLine = System.lineSeparator();
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

    private static void print(String message){
        System.out.println(message);
    }

    @Override
    public void showDice(Dice dice){    //TODO Provare a fondere in un unico StringBuilder
        StringBuilder topRow = new StringBuilder();
        StringBuilder firstRow = new StringBuilder();
        StringBuilder secondRow = new StringBuilder();
        StringBuilder thirdRow = new StringBuilder();
        StringBuilder bottomRow = new StringBuilder();

        dice.getDiceList().forEach(die->{
            topRow.append(getTopDieRow());
            firstRow.append(getFirstDieRow(die));
            secondRow.append(getSecondDieRow(die));
            thirdRow.append(getThirdDieRow(die));
            bottomRow.append(getDieBottomRow());
        });
        print(topRow + newLine + firstRow + newLine + secondRow + newLine + thirdRow + newLine + bottomRow);
    }

    @Override
    public void showTiles(BoardTiles boardTiles){
        StringBuilder topRow = new StringBuilder();
        StringBuilder firstRow = new StringBuilder();
        StringBuilder secondRow = new StringBuilder();
        StringBuilder thirdRow = new StringBuilder();
        StringBuilder bottomRow = new StringBuilder();

        boardTiles.getTiles().forEach(tile->{
            topRow.append(getTopTilesRow());
            firstRow.append(getFirstTilesRow(tile));
            secondRow.append(getSecondTileRow(tile));
            thirdRow.append(getTilesThirdRow(tile));
            bottomRow.append(getBottomTilesRow());
        });
        print("The available tiles on the board now are:" + newLine + topRow + newLine +
                firstRow + newLine + secondRow + newLine + thirdRow + newLine + bottomRow);
    }

    @Override
    public void showPlayerData(Player player, Dice dice, Player[] players){
        Tile tile = player.getLastPickedTile();
        List<Player> otherPlayers = Arrays.asList(players).stream().filter(e -> !e.equals(player)).toList();

        String displayString = "        " + player.getName() + "'s tiles:  ";
        String chosenDiceString = "     Chosen dice: " + dice.getChosenDiceString();
        String chosenDiceScore = "     Current dice score: " + dice.getScore();
        String wormPresent =  "     WORM is chosen: " + dice.isFaceChosen(Die.Face.WORM);

        List <String> rows = new ArrayList<>();

        rows.add(String.format("%1$"+ displayString.length() + "s", displayString ) + ".------." + chosenDiceString);
        String format = String.format("%1$" + displayString.length() + "s", "");
        rows.add(format + getFirstTilesRow(tile) + chosenDiceScore);
        rows.add(format + getSecondTileRow(tile) + wormPresent);
        rows.add(format + getTilesThirdRow(tile));
        rows.add(format + "'------'");
        rows.add("");
        int len = 5 + rows.stream().max(Comparator.comparing(e-> e.length())).get().length();
        int size = Stream.of(players).max(Comparator.comparing(e-> e.getName().length())).get().getName().length();
        String topRow = String.format("%1$-" + len  + "s", "") + String.format("%1$-" + (size + 2) + "s", "")
                + String.format("%1$-" + 8 + "s", "Top Tile");

        print(topRow);

        for(int i = 0; i < otherPlayers.size(); i++){
            String othersTile;
            if(otherPlayers.get(i).hasTile()){
                Tile lastPickedTile = otherPlayers.get(i).getLastPickedTile();
                int number = lastPickedTile.getNumber();
                String worms = lastPickedTile.getWormString();
                String tileNumber = String.format("%1$" + 2 + "s", number);
                //String wormNumber = String.format("%1$-" + 4 + "s", worms);
                othersTile = tileNumber + " - " + worms;
            }else othersTile = "No tiles";
            String playersName = String.format("%1$" + size  + "s", otherPlayers.get(i).getName());
            String newString = String.format("%1$-" + len + "s", rows.get(i)).concat(playersName) + " | " + othersTile;
            rows.set(i, newString);

        }
        for (String r : rows){
            System.out.println(r);
        }
    }

    public void printMessage(String message){
        print(message);
    }

    @Override
    public void showWelcomeMessage(){
        print(getLogo());
        print("                      Welcome in Heckmeck");
    }

    @Override
    public void showWantToPick() {
        print("Do you want to pick the tile?" + newLine + "Press 'y' for picking the tile or 'n' for rolling the remaining dice");
    }

    @Override
    public void showBustMessage() {
        print("#####################" +newLine+ "# BUUUUUSSSTTTTTT!! #" +newLine + "#####################" +newLine);
    }

    @Override
    public void showTurnBeginConfirm(String playerName){
        String message = ": hit enter to start your turn #";
        String separator = "#".repeat(playerName.length()).concat(
                "#".repeat(message.length()+2));

        print(separator);
        print("# " + playerName + message );
        print(separator);
    }

    @Override
    public void showWantToSteal(Player robbedPlayer) {
        print("Do you want to steal tile number "+ robbedPlayer.getLastPickedTile().getNumber()+ " from "+ robbedPlayer.getName() + "? Press 'y' for stealing or 'n' for continuing your turn:");
    }

    @Override
    public void showWantToPlay() {
        print("Do you want to start a new game? Press 'y' for playing or 'n' for quitting");
    }

    public static String getTopDieRow(){
        return ".---------. ";
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
        return "'---------' ";
    }

    private static String getBottomTilesRow() {
        return "'------'";
    }

    private static String getTopTilesRow() {
        return ".------.";
    }

    private static String getFirstTilesRow(Tile tile){
        if (tile != null){
            return "|  " + tile.getNumber() + "  |";
        }
        else{
            return "|  no  |";
        }
    }
    private static String getSecondTileRow(Tile tile){
        if (tile != null){
            return tileToString.get(tile.getNumber())[0];
        }
        else{
            return "| tile |";
        }
    }
    private static String getTilesThirdRow(Tile tile){
        if (tile != null){
            return tileToString.get(tile.getNumber())[1];
        }
        else{
            return "|      |";
        }
    }

static String getLogo(){
        return """
                       
                      _/'')
                     / />>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>         _______                                                                                   
                    ( ( ,--.  ,--.             ,--.                           ,--.            /\\ o o o\\                                                                                    
                    \\ ) |  '--'  | ,---.  ,---.|  |,-. ,--,--,--. ,---.  ,---.|  |,-.        /o \\ o o o\\_______                                                                                    
                        |  .--.  || .-. :| .--'|     / |        || .-. :| .--'|     /       <    >------>   o /|                                                                                    
                        |  |  |  |\\   --.\\ `--.|  \\  \\ |  |  |  |\\   --.\\ `--.|  \\  \\        \\ o/  o   /_____/o|                                                                                            
                        `--'  `--' `----' `---'`--'`--'`--`--`--' `----' `---'`--'`--'        \\/______/     |oo|                                                                                    
                       <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<             |   o   |o/
                                                                                                   |_______|/                                                                                                                             
                """;
    }

}
