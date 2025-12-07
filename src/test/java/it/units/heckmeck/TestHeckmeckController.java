package it.units.heckmeck;

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

    /**
     * Mock IOHandler for testing.
     * Provides configurable responses for deterministic testing.
     */
    private static class MockIOHandler implements IOHandler {
        boolean backToMenuCalled = false;
        boolean printMessageCalled = false;
        String lastMessage = "";
        
        // Configurable responses
        boolean wantToPickResponse = false;
        boolean wantToStealResponse = false;
        Die.Face chooseDieResponse = Die.Face.WORM;

        @Override
        public void showTurnBeginConfirm(Player actualPlayer) {
            // No-op for these tests
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
}
