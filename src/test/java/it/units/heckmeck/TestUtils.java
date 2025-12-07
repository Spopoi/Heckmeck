package it.units.heckmeck;

import Heckmeck.Components.BoardTiles;
import Heckmeck.Components.Dice;
import Heckmeck.Components.Die;
import Heckmeck.Components.Player;
import Heckmeck.GameState;

/**
 * Utility class for common test setup operations.
 * Provides helper methods to create deterministic test scenarios.
 */
public class TestUtils {

    /**
     * Creates a fresh GameState with the given players.
     */
    public static GameState createFreshGameState(Player[] players) {
        return GameState.initial(players, Dice.init(), BoardTiles.init());
    }

    /**
     * Creates a standard set of 3 test players (Alice, Bob, Charlie).
     */
    public static Player[] createTestPlayers() {
        Player[] players = new Player[]{
                Player.generatePlayer(0),
                Player.generatePlayer(1),
                Player.generatePlayer(2)
        };
        players[0].setPlayerName("Alice");
        players[1].setPlayerName("Bob");
        players[2].setPlayerName("Charlie");
        return players;
    }

    /**
     * Creates a set of test players with custom size.
     */
    public static Player[] createTestPlayers(int count) {
        Player[] players = new Player[count];
        String[] names = {"Alice", "Bob", "Charlie", "David", "Eve", "Frank"};
        for (int i = 0; i < count; i++) {
            players[i] = Player.generatePlayer(i);
            players[i].setPlayerName(i < names.length ? names[i] : "Player" + i);
        }
        return players;
    }

    /**
     * Sets up dice with specific faces. Clears existing dice first.
     */
    public static GameState setupDiceWithFaces(GameState state, Die.Face... faces) {
        Dice dice = state.getDice();
        dice.resetDice();
        dice.getDiceList().clear();
        for (Die.Face face : faces) {
            dice.addSpecificDie(face);
        }
        return state.withDice(dice);
    }

    /**
     * Sets up dice with WORM and four FIVE faces (score = 25).
     * Also chooses both WORM and FIVE.
     */
    public static GameState setupDiceWithScore25(GameState state) {
        Dice dice = state.getDice();
        dice.resetDice();
        dice.getDiceList().clear();
        dice.addSpecificDie(Die.Face.WORM);
        for (int i = 0; i < 4; i++) {
            dice.addSpecificDie(Die.Face.FIVE);
        }
        dice.chooseDice(Die.Face.WORM);
        dice.chooseDice(Die.Face.FIVE);
        return state.withDice(dice);
    }

    /**
     * Sets up dice with specific faces and chooses them.
     */
    public static GameState setupAndChooseDice(GameState state, Die.Face faceToChoose, Die.Face... allFaces) {
        Dice dice = state.getDice();
        dice.resetDice();
        dice.getDiceList().clear();
        for (Die.Face face : allFaces) {
            dice.addSpecificDie(face);
        }
        dice.chooseDice(faceToChoose);
        return state.withDice(dice);
    }

    /**
     * Creates an empty board (all tiles removed).
     */
    public static BoardTiles createEmptyBoard() {
        BoardTiles board = BoardTiles.init();
        while (board.hasElement()) {
            board.removeLastTile();
        }
        return board;
    }
}
