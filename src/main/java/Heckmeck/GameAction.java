package Heckmeck;

import Heckmeck.Components.Die;

import java.io.Serializable;

/**
 * Sealed interface representing all possible actions a player can perform in Heckmeck.
 * Using sealed interfaces ensures type safety and exhaustive pattern matching.
 * 
 * All action types are defined as nested records within this interface.
 */
public sealed interface GameAction extends Serializable {
    
    /**
     * Action to start a player's turn.
     * Resets the dice and transitions to ROLL_OR_ACTION phase.
     */
    record StartTurn() implements GameAction {}
    
    /**
     * Action to roll the available dice.
     * If dice show pickable faces, transitions to CHOOSING_DIE_FACE phase.
     * Otherwise, causes a bust and ends the turn.
     */
    record RollDice() implements GameAction {}
    
    /**
     * Action to choose a specific die face after rolling.
     * The face must be present in the current roll and not already chosen.
     * 
     * @param face The die face to choose (ONE, TWO, THREE, FOUR, FIVE, or WORM)
     */
    record ChooseDieFace(Die.Face face) implements GameAction {}
    
    /**
     * Action to pick a tile from the board.
     * Requires:
     * - At least one WORM has been chosen
     * - Dice score is sufficient to pick the nearest tile
     * 
     * Ends the current turn and moves to the next player.
     */
    record PickTileAction() implements GameAction {}
    
    /**
     * Action to steal a tile from another player.
     * Requires:
     * - At least one WORM has been chosen
     * - Dice score matches exactly the value of the target player's top tile
     * - Target player is not the current player
     * 
     * Ends the current turn and moves to the next player.
     * 
     * @param robbedPlayerIndex The index of the player to steal from
     */
    record StealTileAction(int robbedPlayerIndex) implements GameAction {}
}
