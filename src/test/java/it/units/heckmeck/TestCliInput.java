package it.units.heckmeck;

import CLI.CliInputHandler;
import Heckmeck.Die;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class TestCliInput {

    @Test
    void user_want_to_play() {
        String userInput = "y\n";

        InputStream fakeStandardInput = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(fakeStandardInput);
        CliInputHandler inputHandler = new CliInputHandler();

        Assertions.assertTrue(inputHandler.wantToPlay());
    }

    @Test
    void user_do_not_want_to_play() {
        String userInput = "n\n";

        InputStream fakeStandardInput = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(fakeStandardInput);
        CliInputHandler inputHandler = new CliInputHandler();

        Assertions.assertFalse(inputHandler.wantToPlay());
    }

    @ParameterizedTest
    @CsvSource(ignoreLeadingAndTrailingWhitespace = false, textBlock = """
            '1\n',1
            '2\n',2
            '3\n',3
            '4\n',4
            '5\n',5
            'w\n',w
            """)
    void read_die_face_from_valid_user_input(String userInput, String faceAsString) {
        InputStream fakeStandardInput = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(fakeStandardInput);
        CliInputHandler inputHandler = new CliInputHandler();

        Die.Face expectedFace = Die.getFaceByString(faceAsString);
        Die.Face obtainedFace = inputHandler.chooseDiceFace();

        Assertions.assertEquals(expectedFace, obtainedFace);
    }

}
