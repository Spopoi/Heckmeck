package it.units.heckmeck;

import CLI.CliOutputHandler;
import Heckmeck.BoardTiles;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class TestCliOutput {

    private static final String INITIAL_BOARD = getInitialBoard();


    @Test
    void printInitialBoardConfiguration() throws IOException {
        ByteArrayOutputStream fakeStandardOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(fakeStandardOutput));
        CliOutputHandler output = new CliOutputHandler();
        BoardTiles boardTiles = BoardTiles.init();

        output.showTiles(boardTiles);

        Assertions.assertEquals(INITIAL_BOARD, fakeStandardOutput.toString().replaceAll("\u001B\\[[;\\d]*m", ""));
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

}