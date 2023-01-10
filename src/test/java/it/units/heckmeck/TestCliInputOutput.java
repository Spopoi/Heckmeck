package it.units.heckmeck;

import CLI.CliIOHandler;
import CLI.CliInputHandler;
import Heckmeck.BoardTiles;
import Heckmeck.Dice;
import Heckmeck.Die;
import Heckmeck.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.stream.Stream;

public class TestCliInputOutput {

    private static final String INITIAL_BOARD = getInitialBoard();

    public static final String EIGHT_DICE_WITH_ONE_FACES = getEightDiceWithOneFaces();

    public static final String ALL_DIE_FACES = getSixDiceWithAllFaces();

    public static final String PLAYER_NAME = "Luigi";

    public static final String INITIAL_PLAYER_STATUS = getInitialPlayerStatus();

    ByteArrayOutputStream fakeStandardOutput = new ByteArrayOutputStream();

    private final PrintStream outStream = new PrintStream(fakeStandardOutput);

    private final CliIOHandler inputOutput = new CliIOHandler();

    private static final String newLine = System.lineSeparator();


    // TODO: Should we ask if user wants to play?
    @Test
    @Disabled
    void user_want_to_play() {
        String userInput = "y\n";

        InputStream fakeStandardInput = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(fakeStandardInput);
        CliInputHandler inputHandler = new CliInputHandler();

        Assertions.assertTrue(inputHandler.wantToPlay());
    }

    @Test
    @Disabled
    void user_do_not_want_to_play() {
        String userInput = "n\n";

        InputStream fakeStandardInput = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(fakeStandardInput);
        CliInputHandler inputHandler = new CliInputHandler();

        Assertions.assertFalse(inputHandler.wantToPlay());
    }

    // TODO: chooseDie does not need dice
    @ParameterizedTest
    @CsvSource(ignoreLeadingAndTrailingWhitespace = false, textBlock = """
            '1\n',1
            '2\n',2
            '3\n',3
            '4\n',4
            '5\n',5
            'w\n',w
            """)
    @Disabled
    void readDieFaceFromValidUserInput(String userInput, String faceAsString) {
        InputStream fakeStandardInput = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(fakeStandardInput);
        CliIOHandler inputHandler = new CliIOHandler();

        Die.Face expectedFace = Die.getFaceByString(faceAsString);
        Die.Face obtainedFace = inputHandler.chooseDie(null);

        Assertions.assertEquals(expectedFace, obtainedFace);
    }

    @ParameterizedTest
    @MethodSource("wrongUserInputForPlayerNumberProvider")
    @Disabled
    void wrongNumberOfPlayersAreNotAccepted(String userInput) {
        // TODO: infinite loop breaks the test
        System.out.println(userInput);
        InputStream fakeStandardInput = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(fakeStandardInput);
        ByteArrayOutputStream fakeStandardOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(fakeStandardOutput));
        CliIOHandler inputOutputHandler = new CliIOHandler();

        inputOutputHandler.chooseNumberOfPlayers();

        String expectedResponse = "Input is not correct, choose a number between 2 and 7:";
        String actualResponse = fakeStandardOutput.toString();
        System.out.println(actualResponse);
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @ParameterizedTest
    @MethodSource("correctUserInputForPlayerNumberProvider")
    void correctNumberOfPlayersAreAccepted(String userInput, int expectedReadNumber) {
        InputStream fakeStandardInput = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(fakeStandardInput);
        ByteArrayOutputStream fakeStandardOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(fakeStandardOutput));
        CliIOHandler inputOutputHandler = new CliIOHandler();

        Assertions.assertEquals(expectedReadNumber, inputOutputHandler.chooseNumberOfPlayers());
    }

    @ParameterizedTest
    @MethodSource("blankUserInputForPlayerNameProvider")
    @Disabled
    void blankPlayerNameNotAccepted(String userInput) {
        InputStream fakeStandardInput = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(fakeStandardInput);
        ByteArrayOutputStream fakeStandardOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(fakeStandardOutput));
        CliIOHandler inputOutputHandler = new CliIOHandler();

        inputOutputHandler.choosePlayerName(1);

        String expectedResponse = "Name of a player can not be blank.";
        String actualResponse = fakeStandardOutput.toString();
        Assertions.assertEquals(expectedResponse, actualResponse);
    }

    @ParameterizedTest
    @MethodSource("correctUserInputForPlayerNameProvider")
    void correctPlayerNameAreAccepted(String userInput, String expectedReadPlayerName) {
        InputStream fakeStandardInput = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(fakeStandardInput);
        ByteArrayOutputStream fakeStandardOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(fakeStandardOutput));
        CliIOHandler inputOutputHandler = new CliIOHandler();

        Assertions.assertEquals(expectedReadPlayerName, inputOutputHandler.choosePlayerName(1));
    }

    @Test
    void printInitialBoardConfiguration() {
        BoardTiles boardTiles = BoardTiles.init();

        setSystemOut(outStream);
        inputOutput.showBoardTiles(boardTiles);

        Assertions.assertEquals(INITIAL_BOARD, fakeStandardOutput.toString().replaceAll("\u001B\\[[;\\d]*m", ""));
    }

    @Test
    @Disabled
    void printEightOnesAsDiceResult() {
        Dice dice = Dice.init();

        dice.eraseDice();
        for (int i = 0; i < 8; i++) {
            dice.addSpecificDie(Die.Face.ONE);
        }
        setSystemOut(outStream);
        inputOutput.chooseDie(dice);

        Assertions.assertEquals(EIGHT_DICE_WITH_ONE_FACES, fakeStandardOutput.toString());
    }

    @Test
    @Disabled
    void printAllFacesAsDiceResult() {
        Dice dice = Dice.init();

        dice.eraseDice();
        dice.addSpecificDie(Die.Face.ONE);
        dice.addSpecificDie(Die.Face.TWO);
        dice.addSpecificDie(Die.Face.THREE);
        dice.addSpecificDie(Die.Face.FOUR);
        dice.addSpecificDie(Die.Face.FIVE);
        dice.addSpecificDie(Die.Face.WORM);
        setSystemOut(outStream);
        inputOutput.chooseDie(dice);

        Assertions.assertEquals(ALL_DIE_FACES, fakeStandardOutput.toString().replaceAll("\u001B\\[[;\\d]*m", ""));
    }

    @Test
    void printInitialPlayerStatus() {
        setSystemOut(outStream);
        Dice dice = Dice.init();
        Player player1 = Player.generatePlayer(PLAYER_NAME);
        Player player2 = Player.generatePlayer("player2");
        Player player3 = Player.generatePlayer("player3");
        Player[] players = {player1, player2, player3};


        inputOutput.showPlayerData(player1, dice, players);

        Assertions.assertEquals(INITIAL_PLAYER_STATUS, fakeStandardOutput.toString().replaceAll("\u001B\\[[;\\d]*m", ""));
    }


    private static void setSystemOut(PrintStream outStream) {
        System.setOut(outStream);
    }

    private static String getInitialBoard() {
        return """
                The available tiles on the board now are:
                .------. .------. .------. .------. .------. .------. .------. .------. .------. .------. .------. .------. .------. .------. .------. .------.\s
                |  21  | |  22  | |  23  | |  24  | |  25  | |  26  | |  27  | |  28  | |  29  | |  30  | |  31  | |  32  | |  33  | |  34  | |  35  | |  36  |\s
                |  ~   | |  ~   | |  ~   | |  ~   | |  ~~  | |  ~~  | |  ~~  | |  ~~  | |  ~~  | |  ~~  | |  ~~  | |  ~~  | |  ~~  | |  ~~  | |  ~~  | |  ~~  |\s
                |      | |      | |      | |      | |      | |      | |      | |      | |  ~   | |  ~   | |  ~   | |  ~   | |  ~~  | |  ~~  | |  ~~  | |  ~~  |\s
                '------' '------' '------' '------' '------' '------' '------' '------' '------' '------' '------' '------' '------' '------' '------' '------'\s
                
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
       /* String intro = "        " + PLAYER_NAME + "'s tiles:  ";
        String indent = String.format("%1$" + intro.length() + "s", "");
        return intro + ".------.     Chosen dice: []\n" +
                indent + "|  no  |     Current dice score: 0\n" +
                indent + "| tile |     WORM is chosen: false\n" +
                indent + "|      |\n" +
                indent + "'------'\n";*/
        return """
                                                                                        Top Tile
                        Luigi's tiles:  .------.     Chosen dice: []           player2 | No tiles
                                        |  no  |     Current dice score: 0     player3 | No tiles
                                        | tile |     WORM is chosen: false
                                        |      |
                                        '------'
                                
                """;
    }

    static String stringToUserInput(String text) {
        return text + newLine;
    }

    static Stream<String> wrongUserInputForPlayerNumberProvider(){
        return Stream.of("1", "8", "ciao")
                .map(TestCliInputOutput::stringToUserInput);
    }

    static Stream<Arguments> correctUserInputForPlayerNumberProvider() {
        return Stream.of(
                Arguments.arguments(stringToUserInput("2"), 2),
                Arguments.arguments(stringToUserInput("7"), 7)
        );
    }

    static Stream<String> blankUserInputForPlayerNameProvider() {
        return Stream.of("", "\t")
                .map(TestCliInputOutput::stringToUserInput);
    }

    static  Stream<Arguments> correctUserInputForPlayerNameProvider() {
        return Stream.of(
                Arguments.arguments(stringToUserInput("Mario"), "Mario"),
                Arguments.arguments(stringToUserInput("Luigi"), "Luigi"),
                Arguments.arguments(stringToUserInput("Sara"), "Sara")
        );
    }


}