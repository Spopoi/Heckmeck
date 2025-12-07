package it.units.heckmeck;

import Heckmeck.Gateway.GameGateway;
import Heckmeck.Gateway.LocalGameGateway;
import Heckmeck.GameAction;
import Heckmeck.GameEngine;
import Heckmeck.GameResult;
import Heckmeck.GameState;
import Heckmeck.HeckmeckRules;
import Heckmeck.Components.Die;
import Heckmeck.Components.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static it.units.heckmeck.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestLocalGameGateway {

    private GameEngine engine;
    private Player[] players;

    @BeforeEach
    public void setUp() {
        engine = new GameEngine(new HeckmeckRules());
        players = createTestPlayers();
    }

    @Test
    public void testCreateGatewayWithPlayers() {
        GameGateway gateway = new LocalGameGateway(engine, players);
        
        assertTrue(gateway.isActive());
        assertNotNull(gateway.getState());
        assertEquals(3, gateway.getState().getNumberOfPlayers());
        assertEquals(GameState.Phase.WAITING_TURN_START, gateway.getState().getPhase());
    }

    @Test
    public void testCreateGatewayWithExistingState() {
        GameState initialState = createFreshGameState(players);
        GameGateway gateway = new LocalGameGateway(engine, initialState);
        
        assertTrue(gateway.isActive());
        assertEquals(initialState, gateway.getState());
    }

    @Test
    public void testCreateGatewayWithNullEngineThrowsException() {
        assertThrows(NullPointerException.class, () -> new LocalGameGateway(null, players));
    }

    @Test
    public void testCreateGatewayWithNullPlayersThrowsException() {
        assertThrows(NullPointerException.class, () -> new LocalGameGateway(engine, (Player[]) null));
    }

    @Test
    public void testCreateGatewayWithNullStateThrowsException() {
        assertThrows(NullPointerException.class, () -> new LocalGameGateway(engine, (GameState) null));
    }

    @Test
    public void testApplyActionStartTurn() {
        GameGateway gateway = new LocalGameGateway(engine, players);
        GameAction action = new GameAction.StartTurn();
        
        GameResult result = gateway.applyAction(action);
        
        assertTrue(result.isSuccess());
        assertEquals(GameState.Phase.ROLL_OR_ACTION, gateway.getState().getPhase());
    }

    @Test
    public void testApplyActionUpdatesState() {
        GameGateway gateway = new LocalGameGateway(engine, players);
        
        GameState initialState = gateway.getState();
        assertEquals(GameState.Phase.WAITING_TURN_START, initialState.getPhase());
        
        gateway.applyAction(new GameAction.StartTurn());
        
        GameState newState = gateway.getState();
        assertEquals(GameState.Phase.ROLL_OR_ACTION, newState.getPhase());
        assertNotEquals(initialState, newState);
    }

    @Test
    public void testApplyActionWithNullActionThrowsException() {
        GameGateway gateway = new LocalGameGateway(engine, players);
        
        assertThrows(NullPointerException.class, () -> gateway.applyAction(null));
    }

    @Test
    public void testApplySequenceOfActions() {
        // Start with deterministic state to allow a real sequence
        GameState state = createFreshGameState(players);
        state = setupDiceWithFaces(state, Die.Face.WORM, Die.Face.FIVE, Die.Face.ONE);
        GameGateway gateway = new LocalGameGateway(engine, state);
        
        // 1. Start turn
        GameResult result1 = gateway.applyAction(new GameAction.StartTurn());
        assertTrue(result1.isSuccess());
        assertEquals(GameState.Phase.ROLL_OR_ACTION, gateway.getState().getPhase());
        
        // 2. Setup deterministic roll result and move to CHOOSING_DIE_FACE
        GameState currentState = gateway.getState();
        currentState = setupDiceWithFaces(currentState, Die.Face.WORM, Die.Face.FIVE)
                .withPhase(GameState.Phase.CHOOSING_DIE_FACE);
        gateway = new LocalGameGateway(engine, currentState);
        
        // 3. Choose die face
        GameResult result2 = gateway.applyAction(new GameAction.ChooseDieFace(Die.Face.WORM));
        assertTrue(result2.isSuccess());
        assertEquals(GameState.Phase.ROLL_OR_ACTION, gateway.getState().getPhase());
        assertTrue(gateway.getState().getDice().isFaceChosen(Die.Face.WORM));
    }

    @Test
    public void testApplyActionOnInvalidStateReturnsError() {
        GameGateway gateway = new LocalGameGateway(engine, players);
        
        // Try to roll without starting turn
        GameResult result = gateway.applyAction(new GameAction.RollDice());
        
        assertFalse(result.isSuccess());
        assertTrue(result.hasErrors());
        // State should remain unchanged
        assertEquals(GameState.Phase.WAITING_TURN_START, gateway.getState().getPhase());
    }

    @Test
    public void testGatewayBecomesInactiveWhenGameEnds() {
        GameState endedState = createFreshGameState(players)
                .withGameEnded(true, players[0]);
        GameGateway gateway = new LocalGameGateway(engine, endedState);
        
        // Gateway starts active even with ended game
        assertTrue(gateway.isActive());
        
        // Try to apply action - should get error but gateway becomes inactive
        GameResult result = gateway.applyAction(new GameAction.StartTurn());
        assertFalse(result.isSuccess());
        
        assertFalse(gateway.isActive());
    }

    @Test
    public void testCloseGateway() {
        GameGateway gateway = new LocalGameGateway(engine, players);
        
        assertTrue(gateway.isActive());
        
        gateway.close();
        
        assertFalse(gateway.isActive());
    }

    @Test
    public void testGetStateAfterCloseThrowsException() {
        GameGateway gateway = new LocalGameGateway(engine, players);
        gateway.close();
        
        assertThrows(IllegalStateException.class, () -> gateway.getState());
    }

    @Test
    public void testApplyActionAfterCloseThrowsException() {
        GameGateway gateway = new LocalGameGateway(engine, players);
        gateway.close();
        
        assertThrows(IllegalStateException.class, 
            () -> gateway.applyAction(new GameAction.StartTurn()));
    }

    @Test
    public void testIsActiveReturnsFalseAfterClose() {
        GameGateway gateway = new LocalGameGateway(engine, players);
        
        assertTrue(gateway.isActive());
        gateway.close();
        assertFalse(gateway.isActive());
        
        // Multiple close calls are safe
        gateway.close();
        gateway.close();
        assertFalse(gateway.isActive());
    }

    @Test
    public void testGatewayPreservesStateOnError() {
        GameGateway gateway = new LocalGameGateway(engine, players);
        GameState stateBefore = gateway.getState();
        
        // Try invalid action
        GameResult result = gateway.applyAction(new GameAction.RollDice());
        
        assertFalse(result.isSuccess());
        // State should be the same (GameEngine.apply returns original state on error)
        assertEquals(stateBefore.getPhase(), gateway.getState().getPhase());
        assertEquals(stateBefore.getCurrentPlayerIndex(), gateway.getState().getCurrentPlayerIndex());
    }

    @Test
    public void testCompleteGameFlow() {
        // Create gateway with state ready for picking a tile
        GameState state = createFreshGameState(players);
        state = engine.startTurn(state);
        state = setupDiceWithScore25(state).withPhase(GameState.Phase.ROLL_OR_ACTION);
        
        GameGateway gateway = new LocalGameGateway(engine, state);
        
        // Pick tile
        GameResult result = gateway.applyAction(new GameAction.PickTileAction());
        assertTrue(result.isSuccess());
        assertEquals(GameState.Phase.WAITING_TURN_START, gateway.getState().getPhase());
        assertEquals(1, gateway.getState().getCurrentPlayerIndex()); // Next player
        assertTrue(gateway.isActive());
    }
}
