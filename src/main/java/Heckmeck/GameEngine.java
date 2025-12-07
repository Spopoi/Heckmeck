package Heckmeck;

import Heckmeck.Components.BoardTiles;
import Heckmeck.Components.Dice;
import Heckmeck.Components.Die;
import Heckmeck.Components.Player;
import Heckmeck.Components.Tile;

import java.util.List;
import java.util.Objects;

import static Heckmeck.Components.Die.Face.WORM;

public final class GameEngine {

    private final Rules rules;

    public GameEngine(Rules rules) {
        this.rules = Objects.requireNonNull(rules);
    }

    /**
     * Creates a new game with initialized dice and tiles.
     * Equivalent to old Game.init() without IO.
     */
    public GameState startNewGame(Player[] players) {
        Objects.requireNonNull(players, "players must not be null");
        Dice dice = Dice.init();
        BoardTiles boardTiles = BoardTiles.init();
        return GameState.initial(players, dice, boardTiles);
    }

    /**
     * Applies a GameAction to the current GameState and returns a GameResult.
     * This is the main entry point for executing game actions in a type-safe way.
     * 
     * @param state The current game state
     * @param action The action to apply
     * @return GameResult containing the new state and any errors
     */
    public GameResult apply(GameState state, GameAction action) {
        if (state.isGameEnded()) {
            return new GameResult(state, List.of("Game is already over"));
        }
        
        try {
            GameState newState = switch (action) {
                case GameAction.StartTurn() -> startTurn(state);
                case GameAction.RollDice() -> roll(state);
                case GameAction.ChooseDieFace c -> chooseDieFace(state, c.face());
                case GameAction.PickTileAction() -> pickTileAndEndTurn(state);
                case GameAction.StealTileAction s -> stealTileAndEndTurn(state, s.robbedPlayerIndex());
            };
            return new GameResult(newState);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return new GameResult(state, List.of(e.getMessage()));
        }
    }

    public boolean isGameOver(GameState state) {
        return state.isGameEnded() || !state.getBoardTiles().hasElement();
    }

    /**
     * Start of current player's turn:
     * resets the dice and sets the phase to ROLL_OR_ACTION.
     */
    public GameState startTurn(GameState state) {
        ensureNotEnded(state);
        Dice dice = state.getDice();
        dice.resetDice();
        return state.withPhase(GameState.Phase.ROLL_OR_ACTION);
    }

    /**
     * Executes a roll:
     * - if there are no dice: bust and end turn
     * - if there are dice and at least one face is pickable: move to CHOOSING_DIE_FACE
     * - if no face is pickable: bust and end turn
     */
    public GameState roll(GameState state) {
        ensureNotEnded(state);
        ensurePhase(state, GameState.Phase.ROLL_OR_ACTION);

        Dice dice = state.getDice();

        if (dice.getNumOfDice() <= 0) {
            return bust(state);
        }

        dice.rollDice();

        if (dice.canPickAFace()) {
            return state.withPhase(GameState.Phase.CHOOSING_DIE_FACE);
        } else {
            return bust(state);
        }
    }

    /**
     * Choice of die face after the roll.
     */
    public GameState chooseDieFace(GameState state, Die.Face face) {
        ensureNotEnded(state);
        ensurePhase(state, GameState.Phase.CHOOSING_DIE_FACE);

        Dice dice = state.getDice();

        if (!dice.isFacePresent(face)) {
            throw new IllegalStateException("Chosen face is not present on dice: " + face);
        }
        if (dice.isFaceChosen(face)) {
            throw new IllegalStateException("Face already chosen: " + face);
        }

        dice.chooseDice(face);

        return state.withPhase(GameState.Phase.ROLL_OR_ACTION);
    }

    /**
     * True if at least one WORM has been chosen.
     */
    public boolean hasWormChosen(GameState state) {
        return state.getDice().isFaceChosen(WORM);
    }

    /**
     * Logic: the player is forced to pick if they have chosen at least INITIAL_NUMBER_OF_DICE dice
     * and can pick a tile.
     */
    public boolean isForcedToPick(GameState state) {
        Dice dice = state.getDice();
        return dice.getChosenDice().size() >= HeckmeckRules.INITIAL_NUMBER_OF_DICE && canPick(state);
    }

    /**
     * Can pick a tile from the board with the current score?
     */
    public boolean canPick(GameState state) {
        BoardTiles boardTiles = state.getBoardTiles();
        if (!boardTiles.hasElement()) {
            return false;
        }
        Tile minValueTile = boardTiles.tiles().first();
        return state.getDice().getScore() >= minValueTile.number();
    }

    /**
     * Can steal a tile from another player with the current score?
     */
    public boolean canSteal(GameState state) {
        int playerScore = state.getDice().getScore();
        if (playerScore < Tile.tileMinNumber) return false;

        for (Player robbedPlayer : state.getPlayers()) {
            if (robbedPlayer.canStealTile(playerScore)) {
                return true;
            }
        }
        return false;
    }

    /**
     * PICK action:
     * - requires WORM chosen and canPick == true
     * - removes the nearest tile (<= score) from the board
     * - adds it to the current player
     * - ends the turn (next player) or closes the game if there are no more tiles
     */
    public GameState pickTileAndEndTurn(GameState state) {
        ensureNotEnded(state);
        ensurePhase(state, GameState.Phase.ROLL_OR_ACTION);

        Dice dice = state.getDice();
        BoardTiles boardTiles = state.getBoardTiles();

        if (!dice.isFaceChosen(WORM)) {
            throw new IllegalStateException("Cannot pick a tile without having chosen at least one WORM.");
        }
        if (!canPick(state)) {
            throw new IllegalStateException("Cannot pick a tile with current dice score.");
        }

        int diceScore = dice.getScore();
        Tile availableTile = boardTiles.nearestTile(diceScore);

        Player[] players = state.getPlayers();
        int currentIndex = state.getCurrentPlayerIndex();
        Player current = players[currentIndex];

        boardTiles.remove(availableTile);
        current.pickTile(availableTile);
        players[currentIndex] = current;

        GameState updated = state
                .withBoardTiles(boardTiles)
                .withPlayers(players);

        return finalizeTurnAndMaybeEndGame(updated);
    }

    /**
     * STEAL action on a specific player index.
     */
    public GameState stealTileAndEndTurn(GameState state, int robbedPlayerIndex) {
        ensureNotEnded(state);
        ensurePhase(state, GameState.Phase.ROLL_OR_ACTION);

        Dice dice = state.getDice();
        int currentIndex = state.getCurrentPlayerIndex();

        if (!dice.isFaceChosen(WORM)) {
            throw new IllegalStateException("Cannot steal without having chosen at least one WORM.");
        }
        if (!canSteal(state)) {
            throw new IllegalStateException("No steal is possible with current score.");
        }

        Player[] players = state.getPlayers();

        if (robbedPlayerIndex < 0 || robbedPlayerIndex >= players.length) {
            throw new IllegalArgumentException("Invalid robbed player index: " + robbedPlayerIndex);
        }
        if (robbedPlayerIndex == currentIndex) {
            throw new IllegalStateException("Cannot steal from yourself.");
        }

        int playerScore = dice.getScore();
        Player current = players[currentIndex];
        Player robbed = players[robbedPlayerIndex];

        if (!robbed.canStealTile(playerScore)) {
            throw new IllegalStateException("Target player has no stealable tile for score " + playerScore);
        }

        current.stealTileFromPlayer(robbed);

        players[currentIndex] = current;
        players[robbedPlayerIndex] = robbed;

        GameState updated = state.withPlayers(players);

        return finalizeTurnAndMaybeEndGame(updated);
    }

    // -------------------------------------------------------------
    // INTERNAL CORE LOGIC
    // -------------------------------------------------------------

    private GameState bust(GameState state) {
        Player[] players = state.getPlayers();
        int currentIndex = state.getCurrentPlayerIndex();
        Player current = players[currentIndex];
        BoardTiles boardTiles = state.getBoardTiles();

        if (current.hasTile()) {
            boardTiles.add(current.getLastPickedTile());
            current.removeLastPickedTile();
        }
        boardTiles.removeLastTile();

        players[currentIndex] = current;

        GameState updated = state
                .withPlayers(players)
                .withBoardTiles(boardTiles);

        return finalizeTurnAndMaybeEndGame(updated);
    }

    private GameState finalizeTurnAndMaybeEndGame(GameState state) {
        Dice dice = state.getDice();
        dice.resetDice();

        BoardTiles boardTiles = state.getBoardTiles();

        if (!boardTiles.hasElement()) {
            Player winner = rules.whoIsTheWinner(state.getPlayers());
            return state.withGameEnded(true, winner);
        } else {
            GameState afterNext = state.nextPlayer();
            return afterNext.withPhase(GameState.Phase.WAITING_TURN_START);
        }
    }

    private void ensureNotEnded(GameState state) {
        if (state.isGameEnded()) {
            throw new IllegalStateException("Game is already over.");
        }
    }

    private void ensurePhase(GameState state, GameState.Phase expected) {
        if (state.getPhase() != expected) {
            throw new IllegalStateException(
                    "Invalid phase. Expected " + expected + " but was " + state.getPhase()
            );
        }
    }
}
