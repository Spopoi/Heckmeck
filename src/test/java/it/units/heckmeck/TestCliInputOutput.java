package it.units.heckmeck;

import CLI.CliIOHandler;
import Heckmeck.BoardTiles;
import Heckmeck.Dice;
import Heckmeck.Die;
import Heckmeck.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.stream.Stream;

import static org.mockito.Mockito.mock;

public class TestCliInputOutput {

    public static final String PLAYER_NAME = "Luigi";

    private final Player fakePlayer = mock(Player.class);

    ByteArrayOutputStream fakeStandardOutput = new ByteArrayOutputStream();

    private final PrintStream fakeOutputStream = new PrintStream(fakeStandardOutput);

    private final CliIOHandler testInputOutput = new CliIOHandler(System.in, fakeOutputStream);

    // TODO: chooseDie does not need dice
    @ParameterizedTest
    @MethodSource("wrongUserInputForNumberOfPlayersProvider")
    void wrongNumberOfPlayersAreNotAccepted(String userInput) {
        testInputOutput.setInput(new ByteArrayInputStream(userInput.getBytes()));
        String expectedResponse = """
                Choose number of players between 2 and 7:
                Input is not correct, choose a number between 2 and 7:
                """;

        try {
            testInputOutput.chooseNumberOfPlayers();
        } catch (java.util.NoSuchElementException ex){
            String actualResponse = fakeStandardOutput.toString();
            Assertions.assertEquals(expectedResponse, standardizeLineSeparator(actualResponse));
        }
    }

    @ParameterizedTest
    @MethodSource("blankUserInputForPlayerNameProvider")
    void blankPlayerNameNotAccepted(String userInput) {
        testInputOutput.setInput(new ByteArrayInputStream(userInput.getBytes()));
        String expectedResponse = """
                    Insert the name for player1:
                    Name of a player can not be blank.
                    Insert the name for player1:
                    """;

        try {
            testInputOutput.choosePlayerName(1);
        } catch (java.util.NoSuchElementException ex){
            String actualResponse = fakeStandardOutput.toString();
            Assertions.assertEquals(expectedResponse, standardizeLineSeparator(actualResponse));
        }
    }

    @ParameterizedTest
    @MethodSource("correctUserInputForNumberOfPlayersProvider")
    void correctNumberOfPlayersAreAccepted(String userInput, int expectedReadNumber) {
        testInputOutput.setInput(new ByteArrayInputStream(userInput.getBytes()));

        int numberActuallyRead = testInputOutput.chooseNumberOfPlayers();

        Assertions.assertEquals(expectedReadNumber, numberActuallyRead);
    }

    @ParameterizedTest
    @MethodSource("correctUserInputForPlayerNameProvider")
    void correctPlayerNameAreAccepted(String userInput, String expectedReadPlayerName) {
        testInputOutput.setInput(new ByteArrayInputStream(userInput.getBytes()));

        String playerNameActuallyRead = testInputOutput.choosePlayerName(1);

        Assertions.assertEquals(expectedReadPlayerName, playerNameActuallyRead);

    }

    @Test
    void printInitialBoardConfiguration() {
        BoardTiles boardTiles = BoardTiles.init();
        String expectedPrintedBoard = """
                The available tiles on the board now are:
                 .------. .------. .------. .------. .------. .------. .------. .------. .------. .------. .------. .------. .------. .------. .------. .------.
                 |  21  | |  22  | |  23  | |  24  | |  25  | |  26  | |  27  | |  28  | |  29  | |  30  | |  31  | |  32  | |  33  | |  34  | |  35  | |  36  |
                 |  ~   | |  ~   | |  ~   | |  ~   | |  ~~  | |  ~~  | |  ~~  | |  ~~  | |  ~~  | |  ~~  | |  ~~  | |  ~~  | |  ~~  | |  ~~  | |  ~~  | |  ~~  |
                 |      | |      | |      | |      | |      | |      | |      | |      | |  ~   | |  ~   | |  ~   | |  ~   | |  ~~  | |  ~~  | |  ~~  | |  ~~  |
                 '------' '------' '------' '------' '------' '------' '------' '------' '------' '------' '------' '------' '------' '------' '------' '------'
                  
                """;

        testInputOutput.showBoardTiles(boardTiles);

        Assertions.assertEquals(expectedPrintedBoard, standardizeLineSeparator(fakeStandardOutput.toString()));
    }

    @Test
    void correctlyPrintRolledDice() {
        Dice dice = Dice.init();
        dice.eraseDice();
        for (var dieFace : Die.Face.values()) {
            dice.addSpecificDie(dieFace);
        }
        String expectedPrintedDice = """
                .---------. .---------. .---------. .---------. .---------. .---------.
                |         | |      o  | |      o  | |  o   o  | |  o   o  | |   \\=\\   |
                |    o    | |         | |    o    | |         | |    o    | |   /=/   |
                |         | |  o      | |  o      | |  o   o  | |  o   o  | |   \\=\\   |
                '---------' '---------' '---------' '---------' '---------' '---------'
                """;

        testInputOutput.showRolledDice(dice);

        Assertions.assertEquals(expectedPrintedDice, standardizeLineSeparator(fakeStandardOutput.toString()));
    }

    @ParameterizedTest
    @MethodSource("userInputForSelectingDiceProvider")
    void rejectWrongInputsWhenUserChooseDie(String userInput) {
        testInputOutput.setInput(new ByteArrayInputStream(userInput.getBytes()));
        Dice dice = Dice.init();
        String expectedResponse = """
                    Pick one unselected face:
                    Incorrect input, choose between {1, 2, 3, 4, 5, w}:
                    """;

        try {
            testInputOutput.chooseDie(fakePlayer, dice);
        } catch (java.util.NoSuchElementException ex) {
            String actualResponse = fakeStandardOutput.toString();
            Assertions.assertEquals(expectedResponse, standardizeLineSeparator(actualResponse));
        }
    }

    @ParameterizedTest
    @MethodSource("blankUserInputForSelectingDiceProvider")
    void skipBlankInputsWhenUserChooseDie(String userInput) {
        testInputOutput.setInput(new ByteArrayInputStream(userInput.getBytes()));
        Dice dice = Dice.init();
        String expectedResponse = """
                    Pick one unselected face:
                    """;

        try {
            testInputOutput.chooseDie(fakePlayer, dice);
        } catch (java.util.NoSuchElementException ex) {
            String actualResponse = fakeStandardOutput.toString();
            Assertions.assertEquals(expectedResponse, standardizeLineSeparator(actualResponse));
        }
    }

    @ParameterizedTest
    @MethodSource("correctUserInputForSelectingDiceProvider")
    void readDieFaceFromValidUserInput(String userInput, String faceAsString) {
        testInputOutput.setInput(new ByteArrayInputStream(userInput.getBytes()));

        Die.Face expectedFace = Die.getFaceByString(faceAsString);
        Die.Face obtainedFace = testInputOutput.chooseDie(fakePlayer, null);

        Assertions.assertEquals(expectedFace, obtainedFace);
    }

    @ParameterizedTest
    @MethodSource("wrongUserInputWhenPickingTilesProvider")
    void printWarningForWrongAnswersWhenPickingTiles(String userInput) {
        testInputOutput.setInput(new ByteArrayInputStream(userInput.getBytes()));
        String expectedResponse = """
                    Your actual score is: 1
                    Do you want to pick tile number 1 from board?
                    Press 'y' for picking the tile or 'n' for rolling the remaining dice
                    Incorrect decision, please select 'y' for picking or 'n' for rolling your remaining dice
                    """;

        try {
            testInputOutput.wantToPick(fakePlayer, 1, 1);
        } catch (java.util.NoSuchElementException ex) {
            String actualResponse = fakeStandardOutput.toString();
            Assertions.assertEquals(expectedResponse, standardizeLineSeparator(actualResponse));
        }
    }

    @ParameterizedTest
    @MethodSource("blankUserInputWhenPickingTilesProvider")
    void skipBlankAnswersWhenPickingTiles(String userInput) {
        testInputOutput.setInput(new ByteArrayInputStream(userInput.getBytes()));
        String expectedResponse = """
                    Your actual score is: 1
                    Do you want to pick tile number 1 from board?
                    Press 'y' for picking the tile or 'n' for rolling the remaining dice
                    """;

        try {
            testInputOutput.wantToPick(fakePlayer, 1, 1);
        } catch (java.util.NoSuchElementException ex) {
            String actualResponse = fakeStandardOutput.toString();
            Assertions.assertEquals(expectedResponse, standardizeLineSeparator(actualResponse));
        }
    }

    @Test
    void printInitialPlayerStatus() {
        Dice dice = Dice.init();
        Player[] listOfAllPlayers = initialiazeMaxNumberOfPlayer();
        String expectedPrintedPlayerStatus = """
                Luigi's tiles:                    Player  | Top tile | Worms\s
                                                 ----------------------------
                                                  player2 | No tiles |   0  \s
                Chosen dice: []                   player3 | No tiles |   0  \s
                Current dice score: 0             player4 | No tiles |   0  \s
                WORM is chosen: false             player5 | No tiles |   0  \s
                                                  player6 | No tiles |   0  \s
                                                  player7 | No tiles |   0  \s

                """;

        testInputOutput.showPlayerData(listOfAllPlayers[0], dice, listOfAllPlayers);

        Assertions.assertEquals(expectedPrintedPlayerStatus, standardizeLineSeparator(fakeStandardOutput.toString()));
    }

    private Player[] initialiazeMaxNumberOfPlayer() {
        Player player1 = Player.generatePlayer(0);
        player1.setPlayerName(PLAYER_NAME);
        Player player2 = Player.generatePlayer(1);
        player2.setPlayerName("player2");
        Player player3 = Player.generatePlayer(2);
        player3.setPlayerName("player3");
        Player player4 = Player.generatePlayer(3);
        player4.setPlayerName("player4");
        Player player5 = Player.generatePlayer(4);
        player5.setPlayerName("player5");
        Player player6 = Player.generatePlayer(5);
        player6.setPlayerName("player6");
        Player player7 = Player.generatePlayer(6);
        player7.setPlayerName("player7");
        return new Player[]{player1, player2, player3, player4, player5, player6, player7};
    }

    private static String standardizeLineSeparator(String actualResponse) {
        return actualResponse.replaceAll("\\r\\n?", "\n");
    }

    static String stringToUserInput(String text) {
        return text + System.lineSeparator();
    }

    static Stream<Arguments> correctUserInputForSelectingDiceProvider() {
        return Stream.of(
                Arguments.arguments(stringToUserInput("1"), "1"),
                Arguments.arguments(stringToUserInput("2"), "2"),
                Arguments.arguments(stringToUserInput("3"), "3"),
                Arguments.arguments(stringToUserInput("4"), "4"),
                Arguments.arguments(stringToUserInput("5"), "5"),
                Arguments.arguments(stringToUserInput("w"), "w")
        );
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
        return Stream.of("ciao", "mondo")
                .map(TestCliInputOutput::stringToUserInput);
    }

    static Stream<String> blankUserInputWhenPickingTilesProvider() {
        return Stream.of("", "\t", "  ", " \t ")
                .map(TestCliInputOutput::stringToUserInput);
    }

    static Stream<String> userInputForSelectingDiceProvider() {
        return Stream.of("ciao", "mondo")
                .map(TestCliInputOutput::stringToUserInput);
    }

    static Stream<String> blankUserInputForSelectingDiceProvider() {
        return Stream.of("", "\t", "  ", " \t ")
                .map(TestCliInputOutput::stringToUserInput);
    }

}
