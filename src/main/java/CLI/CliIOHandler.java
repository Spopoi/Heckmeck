package CLI;

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

public class CliIOHandler implements IOHandler {

    private static final String newLine = System.lineSeparator();
    private Scanner scan;
    private PrintStream outputWhereToPrint;
    //todo: unificare
    public CliIOHandler() {
        this(System.in, System.out);
    }

    public CliIOHandler(InputStream inputStream, PrintStream printStream) {
        setOutput(printStream);
        scan = new Scanner(inputStream);
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

    @Override
    public void showWelcomeMessage() {
        printMessage(Utils.getLogo());
        printMessage("                      Welcome in Heckmeck");
    }


    public boolean wantToHost() {
        printMessage("Do you want to host the game? Press 'y' or 'n' to select:");
        return getYesOrNoAnswer("Incorrect decision, please select 'y' to play remotely 'n' to play locally");
    }

    public boolean wantToPlayAgain() {
        printMessage("Do you want to play again (y) or go back to main Menu (n)?");
        return getYesOrNoAnswer("Incorrect decision, please select 'y' to play again 'n' to return to menu");
    }

    public String askIPToConnect() {
        printMessage("Insert IP address of the host");
        return getInputString();
    }

    @Override
    public int chooseNumberOfPlayers() {
        printMessage("Choose number of players between 2 and 7:");
        while (true) {
            String userInput = getInputString();
            if (userInput.isBlank()) {
                continue;
            } else {
                try {
                    int numberOfPlayer = Integer.parseInt(userInput);
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
    }

    @Override
    public String choosePlayerName(Player player) {
        while (true) {
            printMessage("Insert the name for player" + player.getPlayerID() + ":");
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
        String mainMessage = "# " + player.getName() + ": hit enter to start your turn #";
        String separator = "#".repeat(mainMessage.length());
        printMessage(newLine + separator + newLine +
                mainMessage + newLine +
                separator);
        getInputString();
    }

    @Override
    public void showBoardTiles(BoardTiles boardTiles) {
        printMessage("The available tiles on the board now are:");
        printMessage(Utils.collectionToString(boardTiles.getTiles()));
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

        TextBlock playerData = new TextBlock(actualPlayerInfo).concatenateWith(new TextBlock(summaryTable.toString()), 12);
        printMessage(playerData.toString());
    }

    private String fillActualPlayerInfoTemplate(String actualPlayerInfoTemplate, Player actualPlayer, Dice dice) {
        return actualPlayerInfoTemplate.replace("$ACTUAL_PLAYER", actualPlayer.getName())
                .replace("$CURRENT_TILES", Utils.collectionToString(actualPlayer.getPlayerTiles()))
                .replace("$CHOSEN_DICE", dice.getChosenDiceString())
                .replace("$CURRENT_DICE_SCORE", String.valueOf(dice.getScore()))
                .replace("$IS_WARM_SELECTED", String.valueOf(dice.isFaceChosen(Die.Face.WORM)));
    }

    @Override
    public boolean wantToPick(Player currentPlayer, int actualDiceScore, int availableTileNumber) {
        printMessage("Your actual score is: " + actualDiceScore);
        printMessage("Do you want to pick tile number " + availableTileNumber + " from board?" + newLine +
                "Press 'y' for picking the tile or 'n' for rolling the remaining dice");
        return getYesOrNoAnswer("Incorrect decision, please select 'y' for picking or 'n' for rolling your remaining dice");
    }

    @Override
    public boolean wantToSteal(Player currentPlayer, Player robbedPlayer) {
        printMessage("Do you want to steal tile number " + robbedPlayer.getLastPickedTile().getNumber() +
                " from " + robbedPlayer.getName() +
                "? Press 'y' for stealing or 'n' for continuing your turn:");
        return getYesOrNoAnswer("Incorrect decision, please select 'y' to steal or 'n' to continue your turn");
    }

    @Override
    public void askRollDiceConfirmation(Player player) {
        String mainMessage = "# " + player.getName() + ": hit enter to roll dice #";
        String separator = "#".repeat(mainMessage.length());
        printMessage(newLine + separator + newLine +
                mainMessage + newLine +
                separator);
        getInputString();
    }

    @Override
    public void showRolledDice(Dice dice) {
        printMessage(Utils.collectionToString(dice.getDiceList()));
    }

    @Override
    public Die.Face chooseDie(Player currentPlayer) {
        // TODO: bug input infinite loop
        printMessage("Pick one unselected face:");
        while (true) {
            String chosenDice = getInputString();
            // TODO: refactor Hide-delegate?
            if (Die.stringToFaceMap.containsKey(chosenDice)) {
                return Die.stringToFaceMap.get(chosenDice);
            } else if (chosenDice.isBlank()) {
                continue;
            } else {
                printMessage("Incorrect input, choose between {1, 2, 3, 4, 5, w}:");
            }
        }
    }

    @Override
    public void showBustMessage() {
        printMessage("#####################" + newLine + "# BUUUUUSSSTTTTTT!! #" + newLine + "#####################");
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
            if (Objects.equals(decision, "y")) {
                return true;
            } else if (Objects.equals(decision, "n")) {
                return false;
            } else if (decision.isBlank()) {
                continue;
            } else {
                printMessage(invalidInputMessage);
            }
        }
    }

//TODO: refactoring with stream
    public String getInitialChoice(){
        while (true) {
            String decision = getInputString();
            if (Objects.equals(decision, "1")) {
                return decision;
            } else if (Objects.equals(decision, "2")) {
                return decision;
            } else if (Objects.equals(decision, "3")) {
                return decision;
            } else if (Objects.equals(decision, "4")) {
                return decision;
            } else if (decision.isBlank()) {
                continue;
            } else {
                printMessage("Incorrect input, insert one possible choice (1, 2, 3, 4)");
            }
        }
    }

}
