package it.units.heckmeck;

import Heckmeck.GameAction;
import Heckmeck.GameResult;
import Heckmeck.Gateway.GameGateway;
import Heckmeck.Gateway.LocalGameGateway;
import Heckmeck.GameEngine;
import Heckmeck.GameState;
import Heckmeck.HeckmeckController;
import Heckmeck.HeckmeckRules;
import Heckmeck.IOHandler;
import Heckmeck.Components.BoardTiles;
import Heckmeck.Components.Die;
import Heckmeck.Components.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static it.units.heckmeck.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for HeckmeckController.
 * These tests verify that the controller properly coordinates game flow.
 */
public class TestHeckmeckController {

    private GameEngine engine;
    private Player[] players;
    private MockIOHandler mockIO;

    @BeforeEach
    public void setUp() {
        engine = new GameEngine(new HeckmeckRules());
        players = createTestPlayers();
        mockIO = new MockIOHandler();
    }

    @Test
    public void testControllerCreation() {
        GameGateway gateway = new LocalGameGateway(engine, players);
        HeckmeckController controller = new HeckmeckController(gateway, mockIO, engine);
        
        assertNotNull(controller);
        gateway.close();
    }

    @Test
    public void testControllerRequiresGateway() {
        assertThrows(NullPointerException.class, 
            () -> new HeckmeckController(null, mockIO, engine));
    }

    @Test
    public void testControllerRequiresIOHandler() {
        GameGateway gateway = new LocalGameGateway(engine, players);
        assertThrows(NullPointerException.class, 
            () -> new HeckmeckController(gateway, null, engine));
        gateway.close();
    }

    @Test
    public void testControllerRequiresEngine() {
        GameGateway gateway = new LocalGameGateway(engine, players);
        assertThrows(NullPointerException.class, 
            () -> new HeckmeckController(gateway, mockIO, null));
        gateway.close();
    }

    @Test
    public void testPlayEndsImmediatelyWhenGameAlreadyEnded() {
        GameState endedState = createFreshGameState(players)
                .withGameEnded(true, players[0]);
        GameGateway gateway = new LocalGameGateway(engine, endedState);
        HeckmeckController controller = new HeckmeckController(gateway, mockIO, engine);
        
        controller.play();
        
        // Should show game over and exit cleanly
        assertTrue(mockIO.backToMenuCalled);
        assertTrue(mockIO.printMessageCalled);
        gateway.close();
    }

    @Test
    public void testPlayEndsWhenBoardIsEmpty() {
        BoardTiles emptyBoard = createEmptyBoard();
        GameState state = GameState.initial(players, createFreshGameState(players).getDice(), emptyBoard);
        
        GameGateway gateway = new LocalGameGateway(engine, state);
        HeckmeckController controller = new HeckmeckController(gateway, mockIO, engine);
        
        controller.play();
        
        // Board empty means game should end
        assertTrue(mockIO.backToMenuCalled);
        gateway.close();
    }

    @Test
    public void testPlayDisplaysWinnerWhenGameEnds() {
        GameState endedState = createFreshGameState(players)
                .withGameEnded(true, players[1]);
        GameGateway gateway = new LocalGameGateway(engine, endedState);
        HeckmeckController controller = new HeckmeckController(gateway, mockIO, engine);
        
        controller.play();
        
        assertTrue(mockIO.lastMessage.contains("Winner"));
        assertTrue(mockIO.lastMessage.contains("Bob"));
        gateway.close();
    }

    @Test
    public void testPlayDisplaysNoWinnerWhenGameEndsWithoutWinner() {
        GameState endedState = createFreshGameState(players)
                .withGameEnded(true, null);
        GameGateway gateway = new LocalGameGateway(engine, endedState);
        HeckmeckController controller = new HeckmeckController(gateway, mockIO, engine);
        
        controller.play();
        
        assertTrue(mockIO.lastMessage.contains("No winner"));
        gateway.close();
    }

    @Test
    public void testControllerWaitsWhenNotPlayerTurn() {
        // Create a remote-like gateway that simulates another player's turn
        GameState initialState = createFreshGameState(players);
        MockRemoteGateway gateway = new MockRemoteGateway(initialState, engine, 1); // My player index is 1
        
        // Start with player 0's turn (not my turn)
        gateway.setState(initialState.withCurrentPlayerIndex(0));
        
        MockIOHandler ioHandler = new MockIOHandler();
        HeckmeckController controller = new HeckmeckController(gateway, ioHandler, engine);
        
        // Run play in a separate thread since it will wait
        Thread playThread = new Thread(() -> controller.play());
        playThread.start();
        
        // Give it time to enter wait state
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            fail("Test interrupted");
        }
        
        // Verify controller is waiting (showTurnBeginConfirm not called yet)
        assertFalse(ioHandler.turnBeginCalled);
        
        // Simulate state update: now it's player 1's turn (my turn)
        gateway.setState(initialState.withCurrentPlayerIndex(1).withPhase(GameState.Phase.WAITING_TURN_START));
        
        // Give it time to detect the change
        try {
            Thread.sleep(600);
        } catch (InterruptedException e) {
            fail("Test interrupted");
        }
        
        // Now turn begin should have been called
        assertTrue(ioHandler.turnBeginCalled);
        
        // Clean up
        gateway.close();
        playThread.interrupt();
        try {
            playThread.join(1000);
        } catch (InterruptedException e) {
            fail("Failed to stop play thread");
        }
    }

    @Test
    public void testLocalGameAlwaysAllowsActions() {
        // Local game should always consider it "my turn"
        GameState initialState = createFreshGameState(players);
        GameGateway gateway = new LocalGameGateway(engine, initialState);
        
        // getMyPlayerIndex should return -1 for local games
        assertEquals(-1, gateway.getMyPlayerIndex());
        
        gateway.close();
    }

    /**
     * Mock IOHandler for testing.
     * Provides configurable responses for deterministic testing.
     */
    private static class MockIOHandler implements IOHandler {
        boolean backToMenuCalled = false;
        boolean printMessageCalled = false;
        boolean turnBeginCalled = false;
        String lastMessage = "";
        
        // Configurable responses
        boolean wantToPickResponse = false;
        boolean wantToStealResponse = false;
        Die.Face chooseDieResponse = Die.Face.WORM;

        @Override
        public void showTurnBeginConfirm(Player actualPlayer) {
            turnBeginCalled = true;
        }

        @Override
        public void showBoardTiles(BoardTiles boardTiles) {
            // No-op
        }

        @Override
        public void showPlayerData(Player actualPlayer, Heckmeck.Components.Dice dice, Player[] players) {
            // No-op
        }

        @Override
        public void showRolledDice(Heckmeck.Components.Dice dice) {
            // No-op
        }

        @Override
        public Die.Face chooseDie(Player actualPlayer) {
            return chooseDieResponse;
        }

        @Override
        public boolean wantToPick(Player actualPlayer, int diceScore, int availableTileNumber) {
            return wantToPickResponse;
        }

        @Override
        public boolean wantToSteal(Player actualPlayer, Player robbedPlayer) {
            return wantToStealResponse;
        }

        @Override
        public void showBustMessage() {
            // No-op
        }

        @Override
        public void printMessage(String s) {
            printMessageCalled = true;
            lastMessage = s;
        }

        @Override
        public void printError(String s) {
            // No-op
        }

        @Override
        public int chooseNumberOfPlayers() {
            return 3;
        }

        @Override
        public String choosePlayerName(Player player) {
            return "TestPlayer";
        }

        @Override
        public void backToMenu() {
            backToMenuCalled = true;
        }
    }
    
    /**
     * Mock gateway that simulates a remote game with a specific player index.
     */
    private static class MockRemoteGateway implements GameGateway {
        private GameState currentState;
        private final GameEngine engine;
        private final int myPlayerIndex;
        private boolean active = true;
        
        public MockRemoteGateway(GameState initialState, GameEngine engine, int myPlayerIndex) {
            this.currentState = initialState;
            this.engine = engine;
            this.myPlayerIndex = myPlayerIndex;
        }
        
        public void setState(GameState newState) {
            this.currentState = newState;
        }
        
        @Override
        public GameResult applyAction(GameAction action) {
            GameResult result = engine.apply(currentState, action);
            if (result.isSuccess()) {
                currentState = result.newState();
            }
            return result;
        }
        
        @Override
        public GameState getState() {
            return currentState;
        }
        
        @Override
        public boolean isActive() {
            return active;
        }
        
        @Override
        public int getMyPlayerIndex() {
            return myPlayerIndex;
        }
        
        @Override
        public void close() {
            active = false;
        }
    }
}
