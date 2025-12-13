package Heckmeck.Gateway;

import Heckmeck.GameAction;
import Heckmeck.GameResult;
import Heckmeck.GameState;

/**
 * Gateway interface for interacting with a Heckmeck game.
 * This abstraction allows the same client code to work with both local (in-memory)
 * and remote (network-based) game instances.
 * 
 * Implementations must be thread-safe if used in concurrent contexts.
 */
public interface GameGateway {
    
    /**
     * Applies a game action and returns the result.
     * 
     * @param action The action to apply
     * @return GameResult containing the new state and any errors
     * @throws IllegalStateException if the gateway is not properly initialized
     */
    GameResult applyAction(GameAction action);
    
    /**
     * Gets the current game state.
     * 
     * @return The current GameState
     * @throws IllegalStateException if the gateway is not properly initialized
     */
    GameState getState();
    
    /**
     * Checks if the game is currently active and ready to accept actions.
     * 
     * @return true if the game is active, false otherwise
     */
    boolean isActive();
    
    /**
     * Gets the player index for the local player.
     * For local games, returns -1 (all players are local).
     * For remote games, returns the index assigned by the server.
     * 
     * @return The local player index, or -1 for local games
     */
    int getMyPlayerIndex();
    
    /**
     * Closes the gateway and releases any resources.
     * After calling this method, the gateway should not be used anymore.
     */
    void close();
}
