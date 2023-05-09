package CLI;

import Heckmeck.*;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CliIOHandler implements IOHandler {

    public static final String LOGO_FILE = "LOGO";
    public static final String ACTUAL_PLAYER_INFO_TEMPLATE_FILE = "PLAYER_INFO_TEMPLATE";

    private static final String newLine = System.lineSeparator();
    private final Scanner scan;

    public CliIOHandler(){
        scan = new Scanner(System.in);
    }

    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void showWelcomeMessage() {
        printMessage(FileReader.readTextFile(getLogoPath()));
        printMessage("                      Welcome in Heckmeck");
    }

    @Override
    public boolean wantToPlayRemote() {
        printMessage("Do you want to play remotely? Press 'y' or 'n' to select:");
        return getYesOrNoAnswer("Incorrect decision, please select 'y' to play remotely 'n' to play locally");
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
        printMessage(collectionToText(boardTiles.getTiles()) + newLine);
    }

    @Override
    public void showPlayerData(Player actualPlayer, Dice dice, Player[] players) {
        String actualPlayerInfoTemplate = FileReader.readTextFile(getActualPlayerInfoTemplate());

        List<Player> otherPlayers = Arrays.stream(players)
                .filter(p -> !Objects.equals(p, actualPlayer))
                .toList();
        String summaryTable = buildSummaryTableAsText(otherPlayers);

        String actualPlayerInfo = actualPlayerInfoTemplate.replace("$ACTUAL_PLAYER", actualPlayer.getName())
                .replace("$CURRENT_TILES", collectionToText(actualPlayer.getPlayerTiles()))
                .replace("$CHOSEN_DICE", dice.getChosenDiceString())
                .replace("$CURRENT_DICE_SCORE", String.valueOf(dice.getScore()))
                .replace("$IS_WARM_SELECTED", String.valueOf(dice.isFaceChosen(Die.Face.WORM)));

        printMessage(concatenateTextBlocks(actualPlayerInfo, summaryTable, 12) + newLine);
    }

    private String buildSummaryTableAsText(List<Player> otherPlayers) {

        int maxLengthOfPlayerName = otherPlayers.stream()
                .mapToInt(p -> p.getName().length())
                .max()
                .orElse(0);
        int playerNameColumnWidth = Math.max("Player".length(), maxLengthOfPlayerName) + 2;
        String playerColumnHeader = alignToColumn("Player", playerNameColumnWidth);

        String topTileColumnHeader = alignToColumn("Top tile", 11);

        String wormsColumnHeader = alignToColumn("Worms", 7);

        StringBuilder table = new StringBuilder();
        String header = playerColumnHeader + "|" + topTileColumnHeader + "|" + wormsColumnHeader;
        String separator = "-".repeat(header.length());
        table.append(header).append(newLine).
                append(separator).append(newLine);

        for (var player : otherPlayers) {
            table.append(summaryRow(player, playerNameColumnWidth))
                    .append(newLine);
        }

        return table.toString();
    }

    @Override
    public boolean wantToPick(int actualDiceScore, int availableTileNumber) {
        printMessage("Your actual score is: " + actualDiceScore);
        printMessage("Do you want to pick tile number " + availableTileNumber + " from board?" + newLine +
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
    public void askRollDiceConfirmation(String playerName) {
        String mainMessage = "# " + playerName + ": hit enter to roll dice #";
        String separator = "#".repeat(mainMessage.length());
        printMessage(separator + newLine +
                mainMessage + newLine +
                separator);
        getInputString();
    }

    @Override
    public void showRolledDice(Dice dice) {
        printMessage(collectionToText(dice.getDiceList()));
    }

    @Override
    public Die.Face chooseDie(Dice dice) {
        // TODO: bug input infinite loop
        printMessage("Pick one unselected face:");
        while (true) {
            String chosenDice = getInputString();
            // TODO: refactor Hide-delegate?
            if (Die.stringToFaceMap.containsKey(chosenDice)) {
                return Die.stringToFaceMap.get(chosenDice);
            } else {
                printMessage("Incorrect input, choose between {1, 2, 3, 4, 5, w}:");
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

    public static String alignToColumn(String entry, int columnWidth) {
        int padding = (columnWidth - entry.length())/2;
        int paddingToAlignColumnDivider = columnWidth - 2*padding - entry.length();
        return String.format("%" + padding + "s%s%" + (padding+paddingToAlignColumnDivider) + "s",
                "", entry, "");
    }

    private static Path getLogoPath() {
        URL tilesResource = CliIOHandler.class.getClassLoader().getResource(LOGO_FILE);
        Path resourcePath = null;
        try {
            resourcePath = Path.of(tilesResource.toURI());
        } catch (URISyntaxException ex) {
            System.out.println(ex);
        }
        return resourcePath;
    }

    private Path getActualPlayerInfoTemplate() {
        URL tilesResource = CliIOHandler.class.getClassLoader().getResource(ACTUAL_PLAYER_INFO_TEMPLATE_FILE);
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

    private static String concatenateTextBlocks(String textBlock1, String textBlock2, Integer spaceBetweenBlocks) {
        final int finalSpaceBetweenBlocks = spaceBetweenBlocks != null ? spaceBetweenBlocks : 1;
        int textBlock1Height = (int) textBlock1.lines().count();
        int textBlock1Width = textBlock1.lines()
                .mapToInt(String::length)
                .max().orElse(0);
        textBlock1 = padRightTextBlockWithSpaces(textBlock1, textBlock1Width);

        int textBlock2Height = (int) textBlock2.lines().count();
        int resultingHeight = Math.max(textBlock1Height, textBlock2Height);

        String pad = " ".repeat(textBlock1Width + finalSpaceBetweenBlocks);

        List<String> lines1 = textBlock1.lines().toList();
        List<String> lines2 = textBlock2.lines().toList();

        return IntStream.range(0, resultingHeight)
                .mapToObj(i -> {
                    String leftLine = i < textBlock1Height ? lines1.get(i) : pad;
                    String rightLine = i < textBlock2Height ? lines2.get(i) : "";
                    return leftLine + " ".repeat(finalSpaceBetweenBlocks) + rightLine;
                })
                .collect(Collectors.joining(newLine));
    }

    private static String padRightTextBlockWithSpaces(String textBlock, int width) {
        return textBlock.lines()
                .map(line -> String.format("%1$-" + width + "s", line))
                .collect(Collectors.joining(newLine));
    }

    private String summaryRow(Player player, int playerNameColumnWidth) {
        String alignedPlayerName = alignToColumn(player.getName(), playerNameColumnWidth);
        String alignedTopTileInfo = alignToColumn(player.getTopTileInfo(), 11);
        String alignedWormsInfo = alignToColumn(String.valueOf(player.getWormScore()), 5);
        return alignedPlayerName + "|" + alignedTopTileInfo + "|" + alignedWormsInfo;
    }

    private String collectionToText(Collection<?> collection) {
        String collectionAsText = "";
        for (var item : collection) {
            // at first iteration collectionAsText will have height=0 --> 2 spaces
            collectionAsText = concatenateTextBlocks(collectionAsText, item.toString(), null);
        }
        return collectionAsText;
    }

}
