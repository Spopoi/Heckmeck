package it.units.heckmeck;

import Heckmeck.GameResult;
import Heckmeck.GameState;
import Heckmeck.Components.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static it.units.heckmeck.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestGameResult {

    private GameState state;

    @BeforeEach
    public void setUp() {
        Player[] players = createTestPlayers(2);
        state = createFreshGameState(players);
    }

    @Test
    public void testSuccessfulResultCreation() {
        GameResult result = new GameResult(state);
        
        assertEquals(state, result.newState());
        assertTrue(result.errors().isEmpty());
        assertFalse(result.hasErrors());
        assertTrue(result.isSuccess());
    }

    @Test
    public void testResultWithErrors() {
        List<String> errors = List.of("Error 1", "Error 2");
        GameResult result = new GameResult(state, errors);
        
        assertEquals(state, result.newState());
        assertEquals(2, result.errors().size());
        assertTrue(result.hasErrors());
        assertFalse(result.isSuccess());
    }

    @Test
    public void testResultWithNullStateThrowsException() {
        assertThrows(NullPointerException.class, () -> new GameResult(null));
        assertThrows(NullPointerException.class, () -> new GameResult(null, List.of()));
    }

    @Test
    public void testResultWithNullErrorsThrowsException() {
        assertThrows(NullPointerException.class, () -> new GameResult(state, null));
    }

    @Test
    public void testIsSuccessLogic() {
        // Success: no errors
        assertTrue(new GameResult(state).isSuccess());
        assertFalse(new GameResult(state).hasErrors());
        
        // Failure: with errors
        GameResult errorResult = new GameResult(state, List.of("Error"));
        assertFalse(errorResult.isSuccess());
        assertTrue(errorResult.hasErrors());
    }
}
