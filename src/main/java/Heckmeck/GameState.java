package Heckmeck;

import Heckmeck.Components.BoardTiles;
import Heckmeck.Components.Dice;
import Heckmeck.Components.Player;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents the pure state of the Heckmeck game.
 * No I/O logic, no user interaction.
 *
 * This object is designed to:
 * - be passed to the GameEngine which applies actions and produces a new GameState
 * - be serialized (for REST/WebSocket backend)
 * - be used by CLI, Swing and web frontend without changing the domain logic
 */
public final class GameState {

    public enum Phase {
        NOT_INITIALIZED,     // before initialization
        WAITING_TURN_START,  // between turns
        ROLL_OR_ACTION,      // player must decide whether to roll / pick / steal
        CHOOSING_DIE_FACE,   // player must choose a face from rolled dice
        GAME_OVER            // game finished
    }

    private final Player[] players;
    private final Dice dice;
    private final BoardTiles boardTiles;
    private final int currentPlayerIndex;
    private final Phase phase;
    private final boolean gameEnded;
    private final Player winner; // null if not yet determined or tie

    private GameState(
            Player[] players,
            Dice dice,
            BoardTiles boardTiles,
            int currentPlayerIndex,
            Phase phase,
            boolean gameEnded,
            Player winner
    ) {
        this.players = players.clone();
        this.dice = dice;
        this.boardTiles = boardTiles;
        this.currentPlayerIndex = currentPlayerIndex;
        this.phase = phase;
        this.gameEnded = gameEnded;
        this.winner = winner;
    }

    /**
     * Factory to create the initial state after players/dice/tiles setup.
     */
    public static GameState initial(Player[] players, Dice dice, BoardTiles boardTiles) {
        Objects.requireNonNull(players, "players must not be null");
        Objects.requireNonNull(dice, "dice must not be null");
        Objects.requireNonNull(boardTiles, "boardTiles must not be null");

        if (players.length == 0) {
            throw new IllegalArgumentException("There must be at least one player");
        }

        return new GameState(
                players,
                dice,
                boardTiles,
                0, // first player
                Phase.WAITING_TURN_START,
                false,
                null
        );
    }

    /**
     * Factory to create a "not initialized" state, useful if you want to further separate
     * the configuration phase (e.g. choosing number of players) from the game phase.
     */
    public static GameState notInitialized() {
        return new GameState(
                new Player[0],
                null,
                null,
                -1,
                Phase.NOT_INITIALIZED,
                false,
                null
        );
    }

    // --------- STATE GETTERS ---------

    public Player[] getPlayers() {
        return players.clone();
    }

    public int getNumberOfPlayers() {
        return players.length;
    }

    public Player getCurrentPlayer() {
        if (currentPlayerIndex < 0 || currentPlayerIndex >= players.length) {
            return null;
        }
        return players[currentPlayerIndex];
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Dice getDice() {
        return dice;
    }

    public BoardTiles getBoardTiles() {
        return boardTiles;
    }

    public Phase getPhase() {
        return phase;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    /**
     * Winner calculated according to Rules.whoIsTheWinner().
     * Can be null if:
     * - the game is not finished
     * - there was a tie
     */
    public Player getWinner() {
        return winner;
    }

    public boolean hasWinner() {
        return winner != null;
    }

    // --------- "WITH" METHODS TO CREATE NEW STATE VARIANTS ---------

    public GameState withPlayers(Player[] newPlayers) {
        return new GameState(
                newPlayers,
                this.dice,
                this.boardTiles,
                clampCurrentPlayerIndex(this.currentPlayerIndex, newPlayers),
                this.phase,
                this.gameEnded,
                this.winner
        );
    }

    public GameState withDice(Dice newDice) {
        return new GameState(
                this.players,
                newDice,
                this.boardTiles,
                this.currentPlayerIndex,
                this.phase,
                this.gameEnded,
                this.winner
        );
    }

    public GameState withBoardTiles(BoardTiles newBoardTiles) {
        return new GameState(
                this.players,
                this.dice,
                newBoardTiles,
                this.currentPlayerIndex,
                this.phase,
                this.gameEnded,
                this.winner
        );
    }

    public GameState withCurrentPlayerIndex(int newIndex) {
        return new GameState(
                this.players,
                this.dice,
                this.boardTiles,
                newIndex,
                this.phase,
                this.gameEnded,
                this.winner
        );
    }

    public GameState withPhase(Phase newPhase) {
        return new GameState(
                this.players,
                this.dice,
                this.boardTiles,
                this.currentPlayerIndex,
                newPhase,
                this.gameEnded,
                this.winner
        );
    }

    public GameState withGameEnded(boolean ended, Player winner) {
        return new GameState(
                this.players,
                this.dice,
                this.boardTiles,
                this.currentPlayerIndex,
                ended ? Phase.GAME_OVER : this.phase,
                ended,
                winner
        );
    }

    /**
     * Helper to move to the next player, keeping the rest unchanged.
     */
    public GameState nextPlayer() {
        if (players.length == 0) {
            return this;
        }
        int nextIndex = (currentPlayerIndex + 1) % players.length;
        nextIndex = clampCurrentPlayerIndex(nextIndex, players);
   
        return new GameState(
                this.players,
                this.dice,
                this.boardTiles,
                nextIndex,
                this.phase,
                this.gameEnded,
                this.winner
        );
    }

    // --------- INTERNAL UTILITIES ---------

    private static int clampCurrentPlayerIndex(int index, Player[] newPlayers) {
        if (newPlayers == null || newPlayers.length == 0) {
            return -1;
        }
        if (index < 0) {
            return 0;
        }
        if (index >= newPlayers.length) {
            return newPlayers.length - 1;
        }
        return index;
    }

    // --------- equals / hashCode / toString ---------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameState)) return false;
        GameState gameState = (GameState) o;
        return currentPlayerIndex == gameState.currentPlayerIndex
                && gameEnded == gameState.gameEnded
                && Arrays.equals(players, gameState.players)
                && Objects.equals(dice, gameState.dice)
                && Objects.equals(boardTiles, gameState.boardTiles)
                && phase == gameState.phase
                && Objects.equals(winner, gameState.winner);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(dice, boardTiles, currentPlayerIndex, phase, gameEnded, winner);
        result = 31 * result + Arrays.hashCode(players);
        return result;
    }

    @Override
    public String toString() {
        return "GameState{" +
                "players=" + Arrays.toString(players) +
                ", dice=" + dice +
                ", boardTiles=" + boardTiles +
                ", currentPlayerIndex=" + currentPlayerIndex +
                ", phase=" + phase +
                ", gameEnded=" + gameEnded +
                ", winner=" + winner +
                '}';
    }
}
