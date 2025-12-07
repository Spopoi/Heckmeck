package Heckmeck;

import java.util.List;
import java.util.Objects;

/**
 * Represents the result of applying a GameAction to a GameState.
 * Contains the new state and optionally a list of errors if the action failed.
 */
public final class GameResult {

    private final GameState newState;
    private final List<String> errors;

    public GameResult(GameState newState) {
        this(newState, List.of());
    }

    public GameResult(GameState newState, List<String> errors) {
        this.newState = Objects.requireNonNull(newState, "newState must not be null");
        this.errors = Objects.requireNonNull(errors, "errors must not be null");
    }

    public GameState newState() {
        return newState;
    }

    public List<String> errors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public boolean isSuccess() {
        return errors.isEmpty();
    }

    @Override
    public String toString() {
        return "GameResult{" +
                "newState=" + newState +
                ", errors=" + errors +
                '}';
    }
}
