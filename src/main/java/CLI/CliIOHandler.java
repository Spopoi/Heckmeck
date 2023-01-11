package CLI;

import Heckmeck.*;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
        // TODO: manage tabs in names, breaks everything
        while (true) {
            printMessage("Insert the name for player" + playerNumber + ":");
            String playerName = getInputString();
            if (playerName.isBlank()) {
                printMessage("Name of a player can not be blank.");
            } else {
                return playerName;
            }
        }
    }

    @Override
    public void showTurnBeginConfirm(String playerName) {
        // TODO: remove code duplication in the ###### msg
        String mainMessage = "# " + playerName + ": hit enter to start your turn #";
        String separator = "#".repeat(mainMessage.length());
        printMessage(separator + newLine +
                mainMessage + newLine +
                separator);
        getInputString();
    }

    @Override
    public void showBoardTiles(BoardTiles boardTiles) {
        printMessage("The available tiles on the board now are:");
        convertToTextAndPrettyPrintCollection(boardTiles.getTiles());
    }

    @Override
    public boolean wantToPick(int diceScore) {
        printMessage("Your actual score is: " + diceScore);
        printMessage("Do you want to pick the tile?" + newLine +
                "Press 'y' for picking the tile or 'n' for rolling the remaining dice");
        return getYesOrNoAnswer("Incorrect decision, please select 'y' for picking or 'n' for rolling your remaining dice");
    }

    @Override
    public boolean wantToSteal(Player robbedPlayer) {
        printMessage("Do you want to steal tile number " + robbedPlayer.getLastPickedTile().getNumber() +
                " from "+ robbedPlayer.getName() +
                "? Press 'y' for stealing or 'n' for continuing your turn:");
        return getYesOrNoAnswer("Incorrect decision, please select 'y' to steal or 'n' to continue your turn");
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
        // TODO: bug input infinite loop
        String mainMessage = "# Hit enter to roll dice #";
        String separator = "#".repeat(mainMessage.length());
        printMessage(separator + newLine +
                mainMessage + newLine +
                separator);
        getInputString();
        convertToTextAndPrettyPrintCollection(dice.getDiceList());
        printMessage("Pick one unselected face:");
        while (true) {
            String chosenDice = getInputString();
            // TODO: refactor Hide-delegate?
            if (Die.stringToFaceMap.containsKey(chosenDice)) {
                return Die.stringToFaceMap.get(chosenDice);
            } else {
                printMessage("Incorrect input, choose between {1, 2, 3, 4, 5, w}: ");
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

    private String getInputString(){
        return scan.nextLine();
    }

    private int getInputNumber(){
        return Integer.parseInt(getInputString());
    }

    private boolean getYesOrNoAnswer(String invalidInputMessage) {
        while(true){
            String decision = getInputString();
            if (Objects.equals(decision, "y")) {
                return true;
            } else if (Objects.equals(decision, "n")) {
                return false;
            } else {
                printMessage(invalidInputMessage);
            }
        }
    }

    private static String concatenateTextBlocks(String textBlock1, String textBlock2) {
        int numberOfLines1 = (int) textBlock1.lines().count();
        int numberOfLines2 = (int) textBlock2.lines().count();
        int maxNumberOfLines = Math.max(numberOfLines1, numberOfLines2);

        // Tabs are counted as 1??
        int paddingSize = textBlock1.lines()
                .mapToInt(String::length)
                .max()
                .orElse(0);
        String pad = new String(new char[paddingSize]).replace('\0', ' ');

        List<String> lines1 = textBlock1.lines().toList();
        List<String> lines2 = textBlock2.lines().toList();

        return IntStream.range(0, maxNumberOfLines)
                .mapToObj(i -> {
                    String leftLine = i < numberOfLines1 ? lines1.get(i) : pad;
                    String rightLine = i < numberOfLines2 ? lines2.get(i) : "";
                    return leftLine + " " + rightLine;
                })
                .collect(Collectors.joining(newLine));
    }

    private void convertToTextAndPrettyPrintCollection(Collection<?> collection) {
        String collectionAsText = "";
        for (var item : collection) {
            collectionAsText = concatenateTextBlocks(collectionAsText, item.toString());
        }
        printMessage(collectionAsText);
    }

}
