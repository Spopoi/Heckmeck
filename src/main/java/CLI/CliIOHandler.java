package CLI;

import Utils.PropertiesManager;
import Utils.CLI.SummaryTable;
import Utils.CLI.TextBlock;
import Utils.CLI.Utils;
import Heckmeck.*;
import Heckmeck.Components.BoardTiles;
import Heckmeck.Components.Dice;
import Heckmeck.Components.Die;
import Heckmeck.Components.Player;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Stream;

import static Heckmeck.Launcher.getPropertiesManager;

public class CliIOHandler implements IOHandler {

    private static final String newLine = System.lineSeparator();
    private Scanner scan;
    private PrintStream outputWhereToPrint;
    private final Rules rules;
    private final PropertiesManager propertiesManager;

    public CliIOHandler(InputStream inputStream, PrintStream printStream) {
        setOutput(printStream);
        rules = new HeckmeckRules();
        scan = new Scanner(inputStream);
        propertiesManager = getPropertiesManager();
    }

    public void setInput(InputStream inputStream) {
        scan.close();
        scan = new Scanner(inputStream);
    }

    public void setOutput(PrintStream printStream) {
        outputWhereToPrint = printStream;
    }

    @Override
    public void printMessage(String message) {
        outputWhereToPrint.println(message);
    }


    public void showWelcomeMessage() {
        printMessage(Utils.getLogo());
        printMessage(propertiesManager.getMessage("welcomeMessage"));
    }

    boolean wantToHost() {
        printMessage(propertiesManager.getMessage("wantToHost") + " (y/n)");
        return getYesOrNoAnswer(propertiesManager.getMessage("invalidYN"));
    }

    public void backToMenu() {
        printMessage(propertiesManager.getMessage("backToMenu"));
        getInputString();
    }

    public String askIPToConnect() {
        printMessage(propertiesManager.getMessage("askIP"));
        return getInputString();
    }

    @Override
    public int chooseNumberOfPlayers() {
        printMessage(propertiesManager.getMessage("chooseNumberPlayer"));
        while (true) {
            String userInput = getInputString();
            if (!userInput.isBlank()) {
                try {
                    int numberOfPlayer = Integer.parseInt(userInput);
                    if (rules.validNumberOfPlayer(numberOfPlayer)) {
                        return numberOfPlayer;
                    } else {
                        printMessage(propertiesManager.getMessage("inavlidPlayerNum"));
                    }
                } catch (NumberFormatException ex) {
                    printMessage(propertiesManager.getMessage("inavlidPlayerNum"));
                }
            }
        }
    }

    @Override
    public String choosePlayerName(Player player) {
        while (true) {
            String message = propertiesManager.getMessage("choosePlayerName").
                    replace("$PLAYER_ID", Integer.toString(player.getPlayerID()));
            printMessage(message);
            String playerName = getInputString();
            if (playerName.isBlank()) {
                printMessage("Name of a player can not be blank.");
            } else {
                return playerName;
            }
        }
    }

    @Override
    public void showTurnBeginConfirm(Player player) {
        String mainMessage = propertiesManager.getMessage("turnBeginConfirm").
                replace("$PLAYER_NAME", player.getName());
        String separator = "#".repeat(mainMessage.length());
        printMessage(newLine + separator + newLine +
                mainMessage + newLine +
                separator);
        getInputString();
    }

    @Override
    public void showBoardTiles(BoardTiles boardTiles) {
        printMessage(propertiesManager.getMessage("availableTiles"));
        printMessage(Utils.collectionToString(boardTiles.tiles()));
    }

    @Override
    public void showPlayerData(Player actualPlayer, Dice dice, Player[] players) {

        List<Player> otherPlayers = Arrays.stream(players)
                .filter(p -> !Objects.equals(p, actualPlayer))
                .toList();
        SummaryTable summaryTable = new SummaryTable(otherPlayers)
                .createHeader()
                .fillWithPlayersData();

        String actualPlayerInfo = fillActualPlayerInfoTemplate(Utils.getActualPlayerInfoTemplate(), actualPlayer, dice);

        TextBlock playerData = new TextBlock(actualPlayerInfo).concatenateWith(new TextBlock(summaryTable.toString()), 9);
        printMessage(playerData.toString() + newLine);
    }

    private String fillActualPlayerInfoTemplate(String actualPlayerInfoTemplate, Player actualPlayer, Dice dice) {
        String choseDiceString = dice.getChosenDice().stream().map(e -> e.getDieFace().toString()).toList().toString();

        return actualPlayerInfoTemplate.replace("$ACTUAL_PLAYER", actualPlayer.getName())
                .replace("$TOP_TILE", actualPlayer.hasTile() ? actualPlayer.getLastPickedTile().toString() : "")
                .replace("$CHOSEN_DICE", choseDiceString)
                .replace("$CURRENT_DICE_SCORE", String.valueOf(dice.getScore()))
                .replace("$IS_WARM_SELECTED", String.valueOf(dice.isFaceChosen(Die.Face.WORM)))
                .replace("$TOTAL_WORMS", String.valueOf(actualPlayer.getWormScore()));
    }

    @Override
    public boolean wantToPick(Player currentPlayer, int actualDiceScore, int availableTileNumber) {
        printMessage(
                propertiesManager.getMessage("actualScore").
                        replace("$DICE_SCORE", Integer.toString(actualDiceScore))
        );
        printMessage(propertiesManager.getMessage("wantToPick").replace("$TILE_NUMBER", Integer.toString(availableTileNumber)));
        printMessage(propertiesManager.getMessage("innformHowToPick"));
        return getYesOrNoAnswer(propertiesManager.getMessage("invalidYN"));
    }

    @Override
    public boolean wantToSteal(Player currentPlayer, Player robbedPlayer) {
        printMessage(propertiesManager.getMessage("wantToSteal").
                replace("$TILE_NUMBER", Integer.toString(robbedPlayer.getLastPickedTile().number())).
                replace("$ROBBED", robbedPlayer.getName())
                );
        printMessage(propertiesManager.getMessage("informHowToSteal"));
        return getYesOrNoAnswer(propertiesManager.getMessage("invalidYN"));
    }

    @Override
    public void showRolledDice(Dice dice) {
        String mainMessage = propertiesManager.getMessage("promptRollDice");
        String separator = "#".repeat(mainMessage.length());
        printMessage(newLine + separator + newLine +
                mainMessage + newLine +
                separator);
        getInputString();
        printMessage(Utils.collectionToString(dice.getDiceList()));
    }

    @Override
    public Die.Face chooseDie(Player currentPlayer) {
        printMessage(
                propertiesManager.getMessage("choseDice").
                        replace("$PLAYER_NAME", currentPlayer.getName())
        );
        while (true) {
            String chosenDice = getInputString();
            if (!chosenDice.isBlank()) {
                if (Die.isFaceLegit(chosenDice)) {
                    return Die.getFaceByString(chosenDice);
                } else {
                    printMessage(getPropertiesManager().getMessage("choseDieIncorrect"));
                }
            }
        }
    }

    @Override
    public void showBustMessage() {
        printMessage("#####################" + newLine + propertiesManager.getMessage("bust") + newLine + "#####################");
    }

    @Override
    public void printError(String text) {
        printMessage(text);
    }

    public String getInputString() {
        return scan.nextLine();
    }

    private boolean getYesOrNoAnswer(String invalidInputMessage) {
        while (true) {
            String decision = getInputString();
            if (!decision.isBlank()) {
                if(Objects.equals(decision, "y")) {
                    return true;
                } else if (Objects.equals(decision, "n")) {
                    return false;
                } else {
                    printMessage(invalidInputMessage);
                }
            }
        }
    }

    String getInitialChoice(){
        while (true) {
            String userChoice = getInputString();
            Optional<String> checkedUserChoice = Stream.of("1", "2", "3", "4")
                    .filter(x -> Objects.equals(x, userChoice))
                    .findFirst();
            if (checkedUserChoice.isPresent()) {
                return checkedUserChoice.get();
            } else if (!userChoice.isBlank()) {
                printMessage(propertiesManager.getMessage("invalidInitialChoice"));
            }
        }
    }

}
