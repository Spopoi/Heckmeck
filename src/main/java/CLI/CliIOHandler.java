package CLI;

import Heckmeck.*;
import exception.IllegalInput;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class CliIOHandler implements IOHandler {

    public static final String LOGO_FILE = "LOGO";
    private static final String[] oneWorm = {"|  ~   |", "|      |"};
    private static final String[] twoWorms = {"|  ~~  |", "|      |"};
    private static final String[] threeWorms = {"|  ~~  |", "|  ~   |"};
    private static final String[] fourWorms = {"|  ~~  |", "|  ~~  |"};

    private static final String newLine = System.lineSeparator();
    private final Scanner scan;
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


    public CliIOHandler(){
        scan = new Scanner(System.in);
    }

    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void showTurnBeginConfirm(String playerName) {
        String message = ": hit enter to start your turn #";
        String separator = "#".repeat(playerName.length()).concat(
                "#".repeat(message.length()+2));

        printMessage(separator);
        printMessage("# " + playerName + message );
        printMessage(separator);
        getInputString();
    }

    @Override
    public void showWelcomeMessage() {
        printMessage(FileReader.readLogoFromTextFile(getLogoPath()));
        printMessage("                      Welcome in Heckmeck");
    }

    @Override
    public int chooseNumberOfPlayers() {
        printMessage("Choose number of players between 2 and 7:");
        while (true) {
            try {
                int numberOfPlayer = getInputNumber();
                if (Rules.validNumberOfPlayer(numberOfPlayer)) {
                    return numberOfPlayer;
                } else {
                    printMessage("Input is not correct, choose a number between 2 and 7:");
                }
            } catch (NumberFormatException ex) {
                printMessage("Input is not correct, choose a number between 2 and 7:");
            }
        }
    }


    @Override
    public String choosePlayerName(int playerNumber) {
        printMessage("Insert the name for player" + playerNumber);
        while(true) {
            try {
                String playerName = getInputString();
                if (playerName.isBlank()) throw new IllegalInput("Blank name, choose a valid a one");
                else return playerName;
            } catch (IllegalInput e) {
                printMessage(e.getMessage());
            }
        }
    }

    @Override
    public void showBoardTiles(BoardTiles boardTiles) {
        if (!boardTiles.allTilesHaveSameHeight()) {
            printMessage("WARNING: In the Tiles representation you've selected, tiles have different height!!!");
        }
        printMessage("The available tiles on the board now are:" + newLine + boardTiles);
    }

    @Override
    public boolean wantToPick(int diceScore) {

        printMessage("Actual score: " + diceScore);
        printMessage("Do you want to pick the tile?" + newLine + "Press 'y' for picking the tile or 'n' for rolling the remaining dice");
        while(true) {
            try {
                String decision  = getInputString();
                if(isYesOrNoChar(decision)) throw new IllegalInput("Incorrect decision, please select 'y' for picking or 'n' for rolling your remaining dice");
                else return "y".equalsIgnoreCase(decision);
            } catch (IllegalInput e) {
                printMessage(e.getMessage());
            }
        }
    }

    @Override
    public boolean wantToSteal(Player robbedPlayer) {
        printMessage("Do you want to steal tile number "+ robbedPlayer.getLastPickedTile().getNumber()+ " from "+ robbedPlayer.getName() + "? Press 'y' for stealing or 'n' for continuing your turn:");
        while(true) {
            try {
                String decision  = getInputString();
                if(isYesOrNoChar(decision)) throw new IllegalInput("Incorrect decision, please select 'y' to steal or 'n' to continue your turn");
                else return "y".equalsIgnoreCase(decision);
            } catch (IllegalInput e) {
                printMessage(e.getMessage());
            }
        }
    }

    @Override
    public void showPlayerData(Player player, Dice dice, Player[] players) {
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

        printMessage(topRow);

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
           printMessage(r);
        }
    }

    //TODO: ha ancora senso mantenere le eccezioni?
    @Override
    public Die.Face chooseDie(Dice dice) {
        printMessage(Dice.diceToString(dice.getDiceList()));
        printMessage("Pick one unselected face");
        while (true) {
            try {
                String chosenDice = getInputString();
                if (Die.stringToFaceMap.containsKey(chosenDice)) {
                    return Die.stringToFaceMap.get(chosenDice);
                } else throw new IllegalInput("Incorrect input, choose between {1, 2, 3, 4, 5, w}: ");
            } catch (IllegalInput e) {
                printMessage(e.getMessage());
            }
        }
    }

    @Override
    public void showBustMessage() {
        printMessage("#####################" +newLine+ "# BUUUUUSSSTTTTTT!! #" +newLine + "#####################" +newLine);
    }

    @Override
    public String printError(String text) {
        return null;
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

    private static Path getLogoPath() {
        URL tilesResource = Tile.class.getClassLoader().getResource(LOGO_FILE);
        Path resourcePath = null;
        try {
            resourcePath = Path.of(tilesResource.toURI());
        } catch (URISyntaxException ex) {
            System.out.println(ex);
        }
        return resourcePath;
    }


    private boolean isYesOrNoChar(String decision){
        return(!"y".equalsIgnoreCase(decision) && !"n".equalsIgnoreCase(decision));
    }

    public String getInputString(){
        return scan.nextLine();
    }

    private int getInputNumber(){
        return Integer.parseInt(getInputString());
    }



}
