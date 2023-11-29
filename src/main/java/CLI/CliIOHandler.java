package CLI;

import Heckmeck.*;

import java.util.*;

public class CliIOHandler implements IOHandler {

    private static final String newLine = System.lineSeparator();
    private final Scanner scan;

    public CliIOHandler() {
        scan = new Scanner(System.in);
    }

    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void showWelcomeMessage() {
        printMessage(FileReader.readTextFile(Utils.getLogoPath()));
        printMessage("                      Welcome in Heckmeck");
    }

    @Override
    public boolean wantToPlayRemote() {
        printMessage("Do you want to play remotely? Press 'y' or 'n' to select:");
        return getYesOrNoAnswer("Incorrect decision, please select 'y' to play remotely 'n' to play locally");
    }

    public boolean wantToHost() {
        printMessage("Do you want to host the game? Press 'y' or 'n' to select:");
        return getYesOrNoAnswer("Incorrect decision, please select 'y' to play remotely 'n' to play locally");
    }

    public boolean wantToPlayAgain() {
        printMessage("Do you want to return to main menu and start another game? Press 'y' or 'n' to select");
        return getYesOrNoAnswer("Incorrect decision, please select 'y' to play remotely 'n' to play locally");
    }

    public String askIPToConnect() {
        printMessage("Insert IP address of the host");
        return getInputString();
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
        printMessage(Utils.collectionToText(boardTiles.getTiles()) + newLine);
    }

    @Override
    public void showPlayerData(Player actualPlayer, Dice dice, Player[] players) {

        List<Player> otherPlayers = Arrays.stream(players)
                .filter(p -> !Objects.equals(p, actualPlayer))
                .toList();
        SummaryTable summaryTable = new SummaryTable(otherPlayers)
                .createHeader()
                .fillWithPlayersData();

        String actualPlayerInfo = fillActualPlayerInfoTemplate(FileReader.readTextFile(Utils.getActualPlayerInfoTemplate()), actualPlayer, dice);

        printMessage(new TextBlockConcatenator(actualPlayerInfo, summaryTable.toString(), 12).concatenate() + newLine);
    }

    private String fillActualPlayerInfoTemplate(String actualPlayerInfoTemplate, Player actualPlayer, Dice dice) {
        return actualPlayerInfoTemplate.replace("$ACTUAL_PLAYER", actualPlayer.getName())
                .replace("$CURRENT_TILES", Utils.collectionToText(actualPlayer.getPlayerTiles()))
                .replace("$CHOSEN_DICE", dice.getChosenDiceString())
                .replace("$CURRENT_DICE_SCORE", String.valueOf(dice.getScore()))
                .replace("$IS_WARM_SELECTED", String.valueOf(dice.isFaceChosen(Die.Face.WORM)));
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
                " from " + robbedPlayer.getName() +
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
        printMessage(Utils.collectionToText(dice.getDiceList()));
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
        printMessage("#####################" + newLine + "# BUUUUUSSSTTTTTT!! #" + newLine + "#####################" + newLine);
    }

    @Override
    public String printError(String text) {
        return null;
    }

    public String getInputString() {
        return scan.nextLine();
    }

    private int getInputNumber() {
        return Integer.parseInt(getInputString());
    }

    private boolean getYesOrNoAnswer(String invalidInputMessage) {
        while (true) {
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

}
