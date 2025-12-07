package it.units.heckmeck;

import Heckmeck.GameState;
import Heckmeck.Components.BoardTiles;
import Heckmeck.Components.Dice;
import Heckmeck.Components.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestGameState {

    private Player[] players;

    @BeforeEach
    public void setUp() {
        players = new Player[]{
                Player.generatePlayer(0),
                Player.generatePlayer(1),
                Player.generatePlayer(2)
        };
        players[0].setPlayerName("Alice");
        players[1].setPlayerName("Bob");
        players[2].setPlayerName("Charlie");
    }

    // Helper method to create fresh Dice and BoardTiles for each test
    private GameState createFreshGameState() {
        return GameState.initial(players, Dice.init(), BoardTiles.init());
    }

    @Test
    public void testInitialStateCreation() {
        GameState state = createFreshGameState();

        assertNotNull(state);
        assertEquals(3, state.getNumberOfPlayers());
        assertEquals(0, state.getCurrentPlayerIndex());
        assertEquals(GameState.Phase.WAITING_TURN_START, state.getPhase());
        assertFalse(state.isGameEnded());
        assertNull(state.getWinner());
        assertFalse(state.hasWinner());
    }

    @Test
    public void testNotInitializedState() {
        GameState state = GameState.notInitialized();

        assertEquals(0, state.getNumberOfPlayers());
        assertEquals(-1, state.getCurrentPlayerIndex());
        assertNull(state.getCurrentPlayer());
        assertEquals(GameState.Phase.NOT_INITIALIZED, state.getPhase());
        assertFalse(state.isGameEnded());
    }

    @Test
    public void testInitialStateWithNullPlayersThrowsException() {
        assertThrows(NullPointerException.class, () -> {
            GameState.initial(null, Dice.init(), BoardTiles.init());
        });
    }

    @Test
    public void testInitialStateWithEmptyPlayersThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            GameState.initial(new Player[0], Dice.init(), BoardTiles.init());
        });
    }

    @Test
    public void testGetCurrentPlayer() {
        GameState state = createFreshGameState();

        Player current = state.getCurrentPlayer();
        assertNotNull(current);
        assertEquals("Alice", current.getName());
    }

    @Test
    public void testGetPlayersReturnsClone() {
        GameState state = createFreshGameState();

        Player[] retrievedPlayers = state.getPlayers();
        retrievedPlayers[0] = null; // modifica la copia

        // Lo stato interno non deve essere modificato
        assertNotNull(state.getPlayers()[0]);
        assertEquals("Alice", state.getPlayers()[0].getName());
    }

    @Test
    public void testWithPhase() {
        GameState state = createFreshGameState();
        assertEquals(GameState.Phase.WAITING_TURN_START, state.getPhase());

        GameState newState = state.withPhase(GameState.Phase.ROLL_OR_ACTION);
        
        // Stato originale non cambia
        assertEquals(GameState.Phase.WAITING_TURN_START, state.getPhase());
        // Nuovo stato ha la fase aggiornata
        assertEquals(GameState.Phase.ROLL_OR_ACTION, newState.getPhase());
    }

    @Test
    public void testWithCurrentPlayerIndex() {
        GameState state = createFreshGameState();
        assertEquals(0, state.getCurrentPlayerIndex());
        assertEquals("Alice", state.getCurrentPlayer().getName());

        GameState newState = state.withCurrentPlayerIndex(1);
        
        assertEquals(0, state.getCurrentPlayerIndex());
        assertEquals(1, newState.getCurrentPlayerIndex());
        assertEquals("Bob", newState.getCurrentPlayer().getName());
    }

    @Test
    public void testNextPlayer() {
        GameState state = createFreshGameState();

        GameState state1 = state.nextPlayer();
        assertEquals(1, state1.getCurrentPlayerIndex());
        assertEquals("Bob", state1.getCurrentPlayer().getName());

        GameState state2 = state1.nextPlayer();
        assertEquals(2, state2.getCurrentPlayerIndex());
        assertEquals("Charlie", state2.getCurrentPlayer().getName());

        GameState state3 = state2.nextPlayer();
        assertEquals(0, state3.getCurrentPlayerIndex());
        assertEquals("Alice", state3.getCurrentPlayer().getName());
    }

    @Test
    public void testNextPlayerWithEmptyPlayers() {
        GameState state = GameState.notInitialized();
        GameState newState = state.nextPlayer();

        // Non deve crashare, ritorna lo stesso stato
        assertEquals(state, newState);
    }

    @Test
    public void testWithGameEnded() {
        GameState state = createFreshGameState();
        assertFalse(state.isGameEnded());
        assertNull(state.getWinner());

        Player winner = players[1];
        GameState endedState = state.withGameEnded(true, winner);

        assertTrue(endedState.isGameEnded());
        assertEquals(winner, endedState.getWinner());
        assertTrue(endedState.hasWinner());
        assertEquals(GameState.Phase.GAME_OVER, endedState.getPhase());
    }

    @Test
    public void testWithGameEndedWithoutWinner() {
        GameState state = createFreshGameState();

        GameState endedState = state.withGameEnded(true, null);

        assertTrue(endedState.isGameEnded());
        assertNull(endedState.getWinner());
        assertFalse(endedState.hasWinner());
    }

    @Test
    public void testWithPlayers() {
        GameState state = createFreshGameState();

        Player[] newPlayers = new Player[]{
                Player.generatePlayer(0),
                Player.generatePlayer(1)
        };
        newPlayers[0].setPlayerName("David");
        newPlayers[1].setPlayerName("Eve");

        GameState newState = state.withPlayers(newPlayers);

        assertEquals(3, state.getNumberOfPlayers());
        assertEquals(2, newState.getNumberOfPlayers());
        assertEquals("David", newState.getPlayers()[0].getName());
    }

    @Test
    public void testWithPlayersAdjustsCurrentPlayerIndex() {
        GameState state = createFreshGameState();
        state = state.withCurrentPlayerIndex(2); // Charlie (indice 2)

        // Riduci i giocatori a 2
        Player[] newPlayers = new Player[]{players[0], players[1]};
        GameState newState = state.withPlayers(newPlayers);

        // currentPlayerIndex dovrebbe essere clampato a 1 (ultimo valido)
        assertEquals(1, newState.getCurrentPlayerIndex());
    }

    @Test
    public void testWithDice() {
        GameState state = createFreshGameState();
        
        Dice newDice = Dice.init();
        newDice.rollDice();
        
        GameState newState = state.withDice(newDice);

        assertNotEquals(state.getDice(), newState.getDice());
        assertEquals(newDice, newState.getDice());
    }

    @Test
    public void testWithBoardTiles() {
        GameState state = createFreshGameState();
        
        BoardTiles newBoardTiles = BoardTiles.init();
        newBoardTiles.remove(newBoardTiles.tiles().first());
        
        GameState newState = state.withBoardTiles(newBoardTiles);

        assertNotEquals(state.getBoardTiles(), newState.getBoardTiles());
        assertEquals(newBoardTiles, newState.getBoardTiles());
    }

    @Test
    public void testImmutability() {
        GameState state = createFreshGameState();

        // Modifica vari campi
        GameState state1 = state.withPhase(GameState.Phase.ROLL_OR_ACTION);
        GameState state2 = state1.nextPlayer();
        GameState state3 = state2.withCurrentPlayerIndex(2);

        // Lo stato originale non deve essere cambiato
        assertEquals(GameState.Phase.WAITING_TURN_START, state.getPhase());
        assertEquals(0, state.getCurrentPlayerIndex());
        assertEquals("Alice", state.getCurrentPlayer().getName());

        // Ogni stato intermedio mantiene le sue proprietà
        assertEquals(GameState.Phase.ROLL_OR_ACTION, state1.getPhase());
        assertEquals(0, state1.getCurrentPlayerIndex());

        assertEquals(1, state2.getCurrentPlayerIndex());
        assertEquals(2, state3.getCurrentPlayerIndex());
    }

    @Test
    public void testToString() {
        GameState state = createFreshGameState();
        String str = state.toString();

        assertNotNull(str);
        assertTrue(str.contains("GameState"));
        assertTrue(str.contains("players="));
        assertTrue(str.contains("phase="));
    }

    @Test
    public void testGetNumberOfPlayers() {
        GameState state = createFreshGameState();
        assertEquals(3, state.getNumberOfPlayers());
        
        Player[] twoPlayers = new Player[]{players[0], players[1]};
        GameState stateWith2 = state.withPlayers(twoPlayers);
        assertEquals(2, stateWith2.getNumberOfPlayers());
    }

    @Test
    public void testGetDiceAndGetBoardTiles() {
        GameState state = createFreshGameState();
        
        assertNotNull(state.getDice());
        assertNotNull(state.getBoardTiles());
    }

    @Test
    public void testMultipleWithMethodsChaining() {
        GameState state = createFreshGameState();
        
        // Chain multiple with methods
        Dice newDice = Dice.init();
        GameState modified = state
                .withPhase(GameState.Phase.ROLL_OR_ACTION)
                .withCurrentPlayerIndex(2)
                .withDice(newDice);
        
        // Verify all changes
        assertEquals(GameState.Phase.ROLL_OR_ACTION, modified.getPhase());
        assertEquals(2, modified.getCurrentPlayerIndex());
        assertSame(newDice, modified.getDice());
        
        // Verify original unchanged
        assertEquals(GameState.Phase.WAITING_TURN_START, state.getPhase());
        assertEquals(0, state.getCurrentPlayerIndex());
        assertNotNull(state.getDice());
    }

    @Test
    public void testHasWinner() {
        GameState state = createFreshGameState();
        
        assertFalse(state.hasWinner());
        
        GameState stateWithWinner = state.withGameEnded(true, players[0]);
        assertTrue(stateWithWinner.hasWinner());
        assertEquals(players[0], stateWithWinner.getWinner());
    }

    @Test
    public void testWithCurrentPlayerIndexBounds() {
        GameState state = createFreshGameState();
        
        // Test valid indices
        GameState state0 = state.withCurrentPlayerIndex(0);
        assertEquals(0, state0.getCurrentPlayerIndex());
        
        GameState state2 = state.withCurrentPlayerIndex(2);
        assertEquals(2, state2.getCurrentPlayerIndex());
        
        // The clamp logic is tested in testWithPlayersAdjustsCurrentPlayerIndex
    }

    @Test
    public void testNextPlayerWrapsAround() {
        GameState state = createFreshGameState();
        
        // Start at index 2 (Charlie)
        state = state.withCurrentPlayerIndex(2);
        assertEquals("Charlie", state.getCurrentPlayer().getName());
        
        // Next should wrap to 0 (Alice)
        GameState nextState = state.nextPlayer();
        assertEquals(0, nextState.getCurrentPlayerIndex());
        assertEquals("Alice", nextState.getCurrentPlayer().getName());
    }

    @Test
    public void testPhaseTransitions() {
        GameState state = createFreshGameState();
        
        // Test all phase transitions
        GameState s1 = state.withPhase(GameState.Phase.ROLL_OR_ACTION);
        assertEquals(GameState.Phase.ROLL_OR_ACTION, s1.getPhase());
        
        GameState s2 = s1.withPhase(GameState.Phase.CHOOSING_DIE_FACE);
        assertEquals(GameState.Phase.CHOOSING_DIE_FACE, s2.getPhase());
        
        GameState s3 = s2.withPhase(GameState.Phase.GAME_OVER);
        assertEquals(GameState.Phase.GAME_OVER, s3.getPhase());
    }
}
