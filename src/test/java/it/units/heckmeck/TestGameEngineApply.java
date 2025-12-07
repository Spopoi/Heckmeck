package it.units.heckmeck;

import Heckmeck.GameAction;
import Heckmeck.GameEngine;
import Heckmeck.GameResult;
import Heckmeck.GameState;
import Heckmeck.HeckmeckRules;
import Heckmeck.Rules;
import Heckmeck.Components.Die;
import Heckmeck.Components.Player;
import Heckmeck.Components.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static it.units.heckmeck.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestGameEngineApply {

    private GameEngine engine;
    private Player[] players;

    @BeforeEach
    public void setUp() {
        Rules rules = new HeckmeckRules();
        engine = new GameEngine(rules);
        players = createTestPlayers();
    }

    @Test
    public void testApplyStartTurnAction() {
        GameState state = createFreshGameState(players);
        GameAction action = new GameAction.StartTurn();
        
        GameResult result = engine.apply(state, action);
        
        assertTrue(result.isSuccess());
        assertFalse(result.hasErrors());
        assertEquals(GameState.Phase.ROLL_OR_ACTION, result.newState().getPhase());
    }

    @Test
    public void testApplyStartTurnOnEndedGameReturnsError() {
        GameState state = createFreshGameState(players);
        GameState endedState = state.withGameEnded(true, players[0]);
        GameAction action = new GameAction.StartTurn();
        
        GameResult result = engine.apply(endedState, action);
        
        assertFalse(result.isSuccess());
        assertTrue(result.hasErrors());
        assertEquals("Game is already over", result.errors().get(0));
        assertEquals(endedState, result.newState()); // Stato non modificato
    }

    @Test
    public void testApplyRollDiceAction() {
        GameState state = createFreshGameState(players);
        state = engine.startTurn(state);
        GameAction action = new GameAction.RollDice();
        
        GameResult result = engine.apply(state, action);
        
        assertTrue(result.isSuccess());
        // Può essere CHOOSING_DIE_FACE o WAITING_TURN_START (bust)
        assertTrue(
            result.newState().getPhase() == GameState.Phase.CHOOSING_DIE_FACE ||
            result.newState().getPhase() == GameState.Phase.WAITING_TURN_START
        );
    }

    @Test
    public void testApplyRollDiceInWrongPhaseReturnsError() {
        GameState state = createFreshGameState(players);
        // Stato in WAITING_TURN_START, non ROLL_OR_ACTION
        GameAction action = new GameAction.RollDice();
        
        GameResult result = engine.apply(state, action);
        
        assertFalse(result.isSuccess());
        assertTrue(result.hasErrors());
        assertTrue(result.errors().get(0).contains("Invalid phase"));
    }

    @Test
    public void testApplyChooseDieFaceAction() {
        GameState state = createFreshGameState(players);
        state = engine.startTurn(state);
        
        // Setup deterministic dice
        state = setupDiceWithFaces(state, Die.Face.WORM, Die.Face.FIVE)
                .withPhase(GameState.Phase.CHOOSING_DIE_FACE);
        
        GameAction action = new GameAction.ChooseDieFace(Die.Face.WORM);
        GameResult result = engine.apply(state, action);
        
        assertTrue(result.isSuccess());
        assertEquals(GameState.Phase.ROLL_OR_ACTION, result.newState().getPhase());
        assertTrue(result.newState().getDice().isFaceChosen(Die.Face.WORM));
    }

    @Test
    public void testApplyChooseDieFaceNotPresentReturnsError() {
        GameState state = createFreshGameState(players);
        state = engine.startTurn(state);
        
        state = setupDiceWithFaces(state, Die.Face.ONE, Die.Face.TWO)
                .withPhase(GameState.Phase.CHOOSING_DIE_FACE);
        
        GameAction action = new GameAction.ChooseDieFace(Die.Face.WORM);
        GameResult result = engine.apply(state, action);
        
        assertFalse(result.isSuccess());
        assertTrue(result.hasErrors());
        assertTrue(result.errors().get(0).contains("not present"));
    }

    @Test
    public void testApplyPickTileActionSuccessfully() {
        GameState state = createFreshGameState(players);
        state = engine.startTurn(state);
        
        state = setupDiceWithScore25(state)
                .withPhase(GameState.Phase.ROLL_OR_ACTION);
        
        GameAction action = new GameAction.PickTileAction();
        GameResult result = engine.apply(state, action);
        
        assertTrue(result.isSuccess());
        assertEquals(GameState.Phase.WAITING_TURN_START, result.newState().getPhase());
        assertEquals(1, result.newState().getCurrentPlayerIndex()); // Next player
    }

    @Test
    public void testApplyPickTileWithoutWormReturnsError() {
        GameState state = createFreshGameState(players);
        state = engine.startTurn(state);
        
        state = setupAndChooseDice(state, Die.Face.FIVE, Die.Face.FIVE)
                .withPhase(GameState.Phase.ROLL_OR_ACTION);
        
        GameAction action = new GameAction.PickTileAction();
        GameResult result = engine.apply(state, action);
        
        assertFalse(result.isSuccess());
        assertTrue(result.hasErrors());
        assertTrue(result.errors().get(0).contains("WORM"));
    }

    @Test
    public void testApplyStealTileActionSuccessfully() {
        GameState state = createFreshGameState(players);
        
        // Give Bob a tile
        Player bob = state.getPlayers()[1];
        bob.pickTile(Tile.generateTile(25));
        Player[] updatedPlayers = state.getPlayers();
        updatedPlayers[1] = bob;
        state = state.withPlayers(updatedPlayers);
        
        state = engine.startTurn(state);
        
        state = setupDiceWithScore25(state)
                .withPhase(GameState.Phase.ROLL_OR_ACTION);
        
        GameAction action = new GameAction.StealTileAction(1);
        GameResult result = engine.apply(state, action);
        
        assertTrue(result.isSuccess());
        assertEquals(GameState.Phase.WAITING_TURN_START, result.newState().getPhase());
    }

    @Test
    public void testApplyStealTileWithoutWormReturnsError() {
        GameState state = createFreshGameState(players);
        state = engine.startTurn(state);
        
        GameAction action = new GameAction.StealTileAction(1);
        GameResult result = engine.apply(state, action);
        
        assertFalse(result.isSuccess());
        assertTrue(result.hasErrors());
        assertTrue(result.errors().get(0).contains("WORM"));
    }

    @Test
    public void testApplyStealTileFromSelfReturnsError() {
        GameState state = createFreshGameState(players);
        
        // Give Bob a tile so canSteal() is true
        Player bob = state.getPlayers()[1];
        bob.pickTile(Tile.generateTile(25));
        Player[] updatedPlayers = state.getPlayers();
        updatedPlayers[1] = bob;
        state = state.withPlayers(updatedPlayers);
        
        state = engine.startTurn(state);
        
        // Setup dice with score 25 (WORM + 4 FIVE)
        state = setupDiceWithScore25(state)
                .withPhase(GameState.Phase.ROLL_OR_ACTION);
        
        int currentIndex = state.getCurrentPlayerIndex();
        GameAction action = new GameAction.StealTileAction(currentIndex);
        GameResult result = engine.apply(state, action);
        
        assertFalse(result.isSuccess());
        assertTrue(result.hasErrors());
        assertTrue(result.errors().get(0).contains("yourself"));
    }

    @Test
    public void testApplyStealTileWithInvalidIndexReturnsError() {
        GameState state = createFreshGameState(players);
        
        // Give Bob a tile so canSteal() is true
        Player bob = state.getPlayers()[1];
        bob.pickTile(Tile.generateTile(25));
        Player[] updatedPlayers = state.getPlayers();
        updatedPlayers[1] = bob;
        state = state.withPlayers(updatedPlayers);
        
        state = engine.startTurn(state);
        
        // Setup dice with score 25 (WORM + 4 FIVE)
        state = setupDiceWithScore25(state)
                .withPhase(GameState.Phase.ROLL_OR_ACTION);
        
        GameAction action = new GameAction.StealTileAction(999);
        GameResult result = engine.apply(state, action);
        
        assertFalse(result.isSuccess());
        assertTrue(result.hasErrors());
        assertTrue(result.errors().get(0).contains("Invalid"));
    }

    @Test
    public void testApplySequenceOfActions() {
        GameState state = createFreshGameState(players);
        
        // 1. Start turn
        GameResult result1 = engine.apply(state, new GameAction.StartTurn());
        assertTrue(result1.isSuccess());
        state = result1.newState();
        
        // 2. Setup deterministic dice
        state = setupDiceWithFaces(state, Die.Face.WORM, Die.Face.ONE)
                .withPhase(GameState.Phase.CHOOSING_DIE_FACE);
        
        // 3. Choose die face
        GameResult result2 = engine.apply(state, new GameAction.ChooseDieFace(Die.Face.WORM));
        assertTrue(result2.isSuccess());
        state = result2.newState();
        
        assertEquals(GameState.Phase.ROLL_OR_ACTION, state.getPhase());
        assertTrue(state.getDice().isFaceChosen(Die.Face.WORM));
    }

    @Test
    public void testApplyPreservesStateOnError() {
        GameState state = createFreshGameState(players);
        GameState.Phase originalPhase = state.getPhase();
        int originalPlayerIndex = state.getCurrentPlayerIndex();
        
        // Try invalid action
        GameAction action = new GameAction.RollDice();
        GameResult result = engine.apply(state, action);
        
        assertFalse(result.isSuccess());
        // State should be unchanged
        assertEquals(originalPhase, result.newState().getPhase());
        assertEquals(originalPlayerIndex, result.newState().getCurrentPlayerIndex());
    }

    @Test
    public void testApplyAllActionTypes() {
        // Test that all action types are handled
        GameState state = createFreshGameState(players);
        
        GameAction[] actions = {
            new GameAction.StartTurn(),
            new GameAction.RollDice(),
            new GameAction.ChooseDieFace(Die.Face.WORM),
            new GameAction.PickTileAction(),
            new GameAction.StealTileAction(1)
        };
        
        for (GameAction action : actions) {
            GameResult result = engine.apply(state, action);
            assertNotNull(result);
            assertNotNull(result.newState());
            // Non tutti avranno successo con lo stato iniziale, ma non devono crashare
        }
    }
}
