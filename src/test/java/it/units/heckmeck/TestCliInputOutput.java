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

    public static final String CHOOSING_DIE_OUTPUT = getExpectedOutputWhenChoosingDie();

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
    @MethodSource("wrongUserInputForNumberOfPlayersProvider")
    void wrongNumberOfPlayersAreNotAccepted(String userInput) {
        System.out.println(userInput);
        InputStream fakeStandardInput = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(fakeStandardInput);
        ByteArrayOutputStream fakeStandardOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(fakeStandardOutput));
        CliIOHandler inputOutputHandler = new CliIOHandler();

        try {
            inputOutputHandler.chooseNumberOfPlayers();
        } catch (java.util.NoSuchElementException ex){
            String expectedResponse = """
                    Choose number of players between 2 and 7:
                    Input is not correct, choose a number between 2 and 7:
                    """;
            String actualResponse = fakeStandardOutput.toString();
            Assertions.assertEquals(expectedResponse, actualResponse);
        }
    }

    @ParameterizedTest
    @MethodSource("correctUserInputForNumberOfPlayersProvider")
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
    void blankPlayerNameNotAccepted(String userInput) {
        InputStream fakeStandardInput = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(fakeStandardInput);
        ByteArrayOutputStream fakeStandardOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(fakeStandardOutput));
        CliIOHandler inputOutputHandler = new CliIOHandler();

        try {
            inputOutputHandler.choosePlayerName(1);
        } catch (java.util.NoSuchElementException ex){
            String expectedResponse = """
                    Insert the name for player1:
                    Name of a player can not be blank.
                    Insert the name for player1:
                    """;
            String actualResponse = fakeStandardOutput.toString();
            Assertions.assertEquals(expectedResponse, actualResponse);
        }

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

    @ParameterizedTest
    @MethodSource("userInputForSelectingDiceProvider")
    void correctlyPrintDiceFromUserInteraction(String userInput) {
        InputStream fakeStandardInput = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(fakeStandardInput);
        ByteArrayOutputStream fakeStandardOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(fakeStandardOutput));
        CliIOHandler inputOutputHandler = new CliIOHandler();
        Dice dice = Dice.init();

        dice.eraseDice();
        for (var dieFace : Die.Face.values()) {
            dice.addSpecificDie(dieFace);
        }

        try {
            inputOutputHandler.chooseDie(dice);
        } catch (java.util.NoSuchElementException ex) {
            Assertions.assertEquals(CHOOSING_DIE_OUTPUT, fakeStandardOutput.toString());
        }
    }

    @ParameterizedTest
    @MethodSource("wrongUserInputWhenPickingTilesProvider")
    void printWarningForWrongAnswersWhenPickingTiles(String userInput) {
        InputStream fakeStandardInput = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(fakeStandardInput);
        ByteArrayOutputStream fakeStandardOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(fakeStandardOutput));
        CliIOHandler inputOutputHandler = new CliIOHandler();

        try {
            inputOutputHandler.wantToPick(1);
        } catch (java.util.NoSuchElementException ex) {
            String expectedResponse = """
                    Your actual score is: 1
                    Do you want to pick the tile?
                    Press 'y' for picking the tile or 'n' for rolling the remaining dice
                    Incorrect decision, please select 'y' for picking or 'n' for rolling your remaining dice
                    """;
            String actualResponse = fakeStandardOutput.toString();
            Assertions.assertEquals(expectedResponse, actualResponse);
        }
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

        Assertions.assertEquals(INITIAL_PLAYER_STATUS, fakeStandardOutput.toString());
    }


    private static void setSystemOut(PrintStream outStream) {
        System.setOut(outStream);
    }

    private static String getInitialBoard() {
        return """
                The available tiles on the board now are:
                  .------. .------. .------. .------. .------. .------. .------. .------. .------. .------. .------. .------. .------. .------. .------. .------.
                  |  21  | |  22  | |  23  | |  24  | |  25  | |  26  | |  27  | |  28  | |  29  | |  30  | |  31  | |  32  | |  33  | |  34  | |  35  | |  36  |
                  |  ~   | |  ~   | |  ~   | |  ~   | |  ~~  | |  ~~  | |  ~~  | |  ~~  | |  ~~  | |  ~~  | |  ~~  | |  ~~  | |  ~~  | |  ~~  | |  ~~  | |  ~~  |
                  |      | |      | |      | |      | |      | |      | |      | |      | |  ~   | |  ~   | |  ~   | |  ~   | |  ~~  | |  ~~  | |  ~~  | |  ~~  |
                  '------' '------' '------' '------' '------' '------' '------' '------' '------' '------' '------' '------' '------' '------' '------' '------'
                  
                """;
    }

    private static String getExpectedOutputWhenChoosingDie() {
        return """
                ##########################
                # Hit enter to roll dice #
                ##########################
                  .---------. .---------. .---------. .---------. .---------. .---------.
                  |         | |      o  | |      o  | |  o   o  | |  o   o  | |   \\=\\   |
                  |    o    | |         | |    o    | |         | |    o    | |   /=/   |
                  |         | |  o      | |  o      | |  o   o  | |  o   o  | |   \\=\\   |
                  '---------' '---------' '---------' '---------' '---------' '---------'
                Pick one unselected face:
                """;
    }

    private static String getInitialPlayerStatus() {
        return """
                Luigi's tiles:                   Player  |   Top tile
                                                 --------------------
                                                 player2 |   No tiles
                Chosen dice: []                  player3 |   No tiles
                Current dice score: 0           \s
                WORM is chosen: false           \s
                                
                """;
    }

    static String stringToUserInput(String text) {
        return text + newLine;
    }

    static Stream<String> wrongUserInputForNumberOfPlayersProvider(){
        return Stream.of("1", "8", "ciao")
                .map(TestCliInputOutput::stringToUserInput);
    }

    static Stream<Arguments> correctUserInputForNumberOfPlayersProvider() {
        return Stream.of(
                Arguments.arguments(stringToUserInput("2"), 2),
                Arguments.arguments(stringToUserInput("7"), 7)
        );
    }

    static Stream<String> blankUserInputForPlayerNameProvider() {
        // TODO: should we make it work also with newLine?
        return Stream.of("", "\t", "  ")
                .map(TestCliInputOutput::stringToUserInput);
    }

    static Stream<Arguments> correctUserInputForPlayerNameProvider() {
        return Stream.of(
                Arguments.arguments(stringToUserInput("Mario"), "Mario"),
                Arguments.arguments(stringToUserInput("Luigi"), "Luigi"),
                Arguments.arguments(stringToUserInput("Sara"), "Sara")
        );
    }

    static Stream<String> wrongUserInputWhenPickingTilesProvider() {
        return Stream.of("ciao", "", "mondo")
                .map(TestCliInputOutput::stringToUserInput);
    }

    static Stream<String> userInputForSelectingDiceProvider() {
        return Stream.of("")
                .map(TestCliInputOutput::stringToUserInput);
    }

}
