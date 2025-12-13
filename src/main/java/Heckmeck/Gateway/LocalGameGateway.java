package Heckmeck.Gateway;

import Heckmeck.GameAction;
import Heckmeck.GameEngine;
import Heckmeck.GameResult;
import Heckmeck.GameState;
import Heckmeck.Components.Player;

import java.util.Objects;

/**
 * Local implementation of GameGateway that holds the game state in memory.
 * This is a synchronous, single-process implementation suitable for:
 * - Local single-player or hot-seat multiplayer games
 * - Testing and development
 * - CLI or GUI applications running on a single machine
 * 
 * This class is NOT thread-safe. If concurrent access is needed,
 * external synchronization must be applied.
 */
public class LocalGameGateway implements GameGateway {
    
    private final GameEngine engine;
    private GameState currentState;
    private boolean active;
    
    /**
     * Creates a new local gateway with an existing game state.
     * 
     * @param engine The game engine to use for action processing
     * @param initialState The initial game state
     */
    public LocalGameGateway(GameEngine engine, GameState initialState) {
        this.engine = Objects.requireNonNull(engine, "engine must not be null");
        this.currentState = Objects.requireNonNull(initialState, "initialState must not be null");
        this.active = true;
    }
    
    /**
     * Creates a new local gateway and starts a new game with the given players.
     * 
     * @param engine The game engine to use
     * @param players The players for the new game
     */
    public LocalGameGateway(GameEngine engine, Player[] players) {
        this.engine = Objects.requireNonNull(engine, "engine must not be null");
        Objects.requireNonNull(players, "players must not be null");
        this.currentState = engine.startNewGame(players);
        this.active = true;
    }
    
    @Override
    public GameResult applyAction(GameAction action) {
        ensureActive();
        Objects.requireNonNull(action, "action must not be null");
        
        GameResult result = engine.apply(currentState, action);
        
        // Update state only if action was successful or if we want to preserve error state
        // For now, we always update (even on error, state is unchanged by design)
        currentState = result.newState();
        
        // If game ended, mark as inactive
        if (currentState.isGameEnded()) {
            active = false;
        }
        
        return result;
    }
    
    @Override
    public GameState getState() {
        ensureActive();
        return currentState;
    }
    
    @Override
    public boolean isActive() {
        return active;
    }
    
    @Override
    public int getMyPlayerIndex() {
        // For local games, all players are local, so return -1
        return -1;
    }
    
    @Override
    public void close() {
        active = false;
        // No resources to release for local gateway
    }
    
    private void ensureActive() {
        if (!active) {
            throw new IllegalStateException("Gateway is not active");
        }
    }
}
