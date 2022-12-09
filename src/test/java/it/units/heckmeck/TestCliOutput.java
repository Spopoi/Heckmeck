package it.units.heckmeck;

import CLI.CliOutputHandler;
import Heckmeck.BoardTiles;
import Heckmeck.Dice;
import Heckmeck.Die;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class TestCliOutput {

    private static final String INITIAL_BOARD = getInitialBoard();

    public static final String EIGHT_DICE_WITH_ONE_FACES = getEightDiceWithOneFaces();

    public static final String ALL_DIE_FACES = getSixDiceWithAllFaces();


    @Test
    void printInitialBoardConfiguration() throws IOException {
        ByteArrayOutputStream fakeStandardOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(fakeStandardOutput));
        CliOutputHandler output = new CliOutputHandler();
        BoardTiles boardTiles = BoardTiles.init();

        output.showTiles(boardTiles);

        Assertions.assertEquals(INITIAL_BOARD, fakeStandardOutput.toString().replaceAll("\u001B\\[[;\\d]*m", ""));
    }

    @Test
    void printEightOnesAsDiceResult() throws IOException {
        ByteArrayOutputStream fakeStandardOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(fakeStandardOutput));
        CliOutputHandler output = new CliOutputHandler();
        Dice dice = Dice.generateDice();

        dice.eraseDice();
        for (int i = 0; i < 8; i++) {
            dice.addSpecificDie(Die.Face.ONE);
        }
        output.showDice(dice);

        Assertions.assertEquals(EIGHT_DICE_WITH_ONE_FACES, fakeStandardOutput.toString());
    }

    @Test
    void printAllFacesAsDiceResult() throws IOException {
        ByteArrayOutputStream fakeStandardOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(fakeStandardOutput));
        CliOutputHandler output = new CliOutputHandler();
        Dice dice = Dice.generateDice();

        dice.eraseDice();
        dice.addSpecificDie(Die.Face.ONE);
        dice.addSpecificDie(Die.Face.TWO);
        dice.addSpecificDie(Die.Face.THREE);
        dice.addSpecificDie(Die.Face.FOUR);
        dice.addSpecificDie(Die.Face.FIVE);
        dice.addSpecificDie(Die.Face.WORM);
        output.showDice(dice);

        Assertions.assertEquals(ALL_DIE_FACES, fakeStandardOutput.toString().replaceAll("\u001B\\[[;\\d]*m", ""));
    }


    private static String getInitialBoard() {
        return """
                The available tiles on the board now are:
                ┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓
                │  21  ││  22  ││  23  ││  24  ││  25  ││  26  ││  27  ││  28  ││  29  ││  30  ││  31  ││  32  ││  33  ││  34  ││  35  ││  36  │
                │  ~   ││  ~   ││  ~   ││  ~   ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  │
                │      ││      ││      ││      ││      ││      ││      ││      ││  ~   ││  ~   ││  ~   ││  ~   ││  ~~  ││  ~~  ││  ~~  ││  ~~  │
                └──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘
                """;
    }

    private static String getEightDiceWithOneFaces() {
        return """
                ┌---------┐ ┌---------┐ ┌---------┐ ┌---------┐ ┌---------┐ ┌---------┐ ┌---------┐ ┌---------┐\s
                ┊         ┊ ┊         ┊ ┊         ┊ ┊         ┊ ┊         ┊ ┊         ┊ ┊         ┊ ┊         ┊\s
                ┊    ◎    ┊ ┊    ◎    ┊ ┊    ◎    ┊ ┊    ◎    ┊ ┊    ◎    ┊ ┊    ◎    ┊ ┊    ◎    ┊ ┊    ◎    ┊\s
                ┊         ┊ ┊         ┊ ┊         ┊ ┊         ┊ ┊         ┊ ┊         ┊ ┊         ┊ ┊         ┊\s
                └---------┘ └---------┘ └---------┘ └---------┘ └---------┘ └---------┘ └---------┘ └---------┘\s
                """;
    }

    private static String getSixDiceWithAllFaces() {
        return """
                ┌---------┐ ┌---------┐ ┌---------┐ ┌---------┐ ┌---------┐ ┌---------┐\s
                ┊         ┊ ┊      ◎  ┊ ┊      ◎  ┊ ┊  ◎   ◎  ┊ ┊  ◎   ◎  ┊ ┊   \\=\\   ┊\s
                ┊    ◎    ┊ ┊         ┊ ┊    ◎    ┊ ┊         ┊ ┊    ◎    ┊ ┊   /=/   ┊\s
                ┊         ┊ ┊  ◎      ┊ ┊  ◎      ┊ ┊  ◎   ◎  ┊ ┊  ◎   ◎  ┊ ┊   \\=\\   ┊\s
                └---------┘ └---------┘ └---------┘ └---------┘ └---------┘ └---------┘\s
                """;
    }



}