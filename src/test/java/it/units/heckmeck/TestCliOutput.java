package it.units.heckmeck;

import CLI.CliOutputHandler;
import Heckmeck.BoardTiles;
import Heckmeck.Dice;
import Heckmeck.Die;
import Heckmeck.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class TestCliOutput {

    private static final String INITIAL_BOARD = getInitialBoard();

    public static final String EIGHT_DICE_WITH_ONE_FACES = getEightDiceWithOneFaces();

    public static final String ALL_DIE_FACES = getSixDiceWithAllFaces();

    public static final String PLAYER_NAME = "Luigi";

    public static final String INITIAL_PLAYER_STATUS = getInitialPlayerStatus();

    ByteArrayOutputStream fakeStandardOutput = new ByteArrayOutputStream();

    private final PrintStream outStream = new PrintStream(fakeStandardOutput);

    private final CliOutputHandler output = new CliOutputHandler();


    @Test
    void printInitialBoardConfiguration() throws IOException {
        BoardTiles boardTiles = BoardTiles.init();

        setSystemOut(outStream);
        output.showTiles(boardTiles);

        Assertions.assertEquals(INITIAL_BOARD, fakeStandardOutput.toString().replaceAll("\u001B\\[[;\\d]*m", ""));
    }

    @Test
    void printEightOnesAsDiceResult() throws IOException {
        Dice dice = Dice.init();

        dice.eraseDice();
        for (int i = 0; i < 8; i++) {
            dice.addSpecificDie(Die.Face.ONE);
        }
        setSystemOut(outStream);
        output.showDice(dice);

        Assertions.assertEquals(EIGHT_DICE_WITH_ONE_FACES, fakeStandardOutput.toString());
    }

    @Test
    void printAllFacesAsDiceResult() throws IOException {
        Dice dice = Dice.init();

        dice.eraseDice();
        dice.addSpecificDie(Die.Face.ONE);
        dice.addSpecificDie(Die.Face.TWO);
        dice.addSpecificDie(Die.Face.THREE);
        dice.addSpecificDie(Die.Face.FOUR);
        dice.addSpecificDie(Die.Face.FIVE);
        dice.addSpecificDie(Die.Face.WORM);
        setSystemOut(outStream);
        output.showDice(dice);

        Assertions.assertEquals(ALL_DIE_FACES, fakeStandardOutput.toString().replaceAll("\u001B\\[[;\\d]*m", ""));
    }

    @Test
    void printInitialPlayerStatus() throws IOException {
        setSystemOut(outStream);
        Dice dice = Dice.init();
        Player player = Player.generatePlayer(PLAYER_NAME);

        output.showPlayerData(player, dice);

        Assertions.assertEquals(INITIAL_PLAYER_STATUS, fakeStandardOutput.toString().replaceAll("\u001B\\[[;\\d]*m", ""));
    }


    private static void setSystemOut(PrintStream outStream) {
        System.setOut(outStream);
    }

    private static String getInitialBoard() {
        return """
                The available tiles on the board now are:
                .------..------..------..------..------..------..------..------..------..------..------..------..------..------..------..------.
                |  21  ||  22  ||  23  ||  24  ||  25  ||  26  ||  27  ||  28  ||  29  ||  30  ||  31  ||  32  ||  33  ||  34  ||  35  ||  36  |
                |  ~   ||  ~   ||  ~   ||  ~   ||  ~~  ||  ~~  ||  ~~  ||  ~~  ||  ~~  ||  ~~  ||  ~~  ||  ~~  ||  ~~  ||  ~~  ||  ~~  ||  ~~  |
                |      ||      ||      ||      ||      ||      ||      ||      ||  ~   ||  ~   ||  ~   ||  ~   ||  ~~  ||  ~~  ||  ~~  ||  ~~  |
                '------''------''------''------''------''------''------''------''------''------''------''------''------''------''------''------'
                """;
    }

    private static String getEightDiceWithOneFaces() {
        return """
                .---------. .---------. .---------. .---------. .---------. .---------. .---------. .---------.\s
                |         | |         | |         | |         | |         | |         | |         | |         |\s
                |    o    | |    o    | |    o    | |    o    | |    o    | |    o    | |    o    | |    o    |\s
                |         | |         | |         | |         | |         | |         | |         | |         |\s
                '---------' '---------' '---------' '---------' '---------' '---------' '---------' '---------'\s
                """;
    }

    private static String getSixDiceWithAllFaces() {
        return """
                .---------. .---------. .---------. .---------. .---------. .---------.\s
                |         | |      o  | |      o  | |  o   o  | |  o   o  | |   \\=\\   |\s
                |    o    | |         | |    o    | |         | |    o    | |   /=/   |\s
                |         | |  o      | |  o      | |  o   o  | |  o   o  | |   \\=\\   |\s
                '---------' '---------' '---------' '---------' '---------' '---------'\s
                """;
    }

    private static String getInitialPlayerStatus() {
        String intro = "        " + PLAYER_NAME + "'s tiles:  ";
        String indent = String.format("%1$" + intro.length() + "s", "");
        return intro + ".------.     Chosen dice: []\n" +
                indent + "|  no  |     Current dice score: 0\n" +
                indent + "| tile |     WORM is chosen: false\n" +
                indent + "|      |\n" +
                indent + "'------'\n";
    }

}