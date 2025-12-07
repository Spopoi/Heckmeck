package Heckmeck;

import Heckmeck.Gateway.GameGateway;
import Heckmeck.Components.Die;
import Heckmeck.Components.Player;

import java.util.Objects;

/**
 * Controller that coordinates between the game logic (via GameGateway) and the presentation (via IOHandler).
 * This class orchestrates the game flow by:
 * - Translating user interactions into GameActions
 * - Applying actions through the gateway
 * - Updating the view based on the new state
 * - Handling errors and displaying them to the user
 * 
 * The controller works with any GameGateway implementation (local or remote) and any IOHandler
 * implementation (CLI, GUI, TCP), allowing for flexible game configurations.
 */
public class HeckmeckController {
    
    private final GameGateway gateway;
    private final IOHandler ioHandler;
    private final GameEngine engine;
    
    /**
     * Creates a new controller with the specified gateway and I/O handler.
     * 
     * @param gateway The gateway to use for game logic
     * @param ioHandler The I/O handler for user interaction
     * @param engine The game engine (needed for queries like canPick, canSteal)
     */
    public HeckmeckController(GameGateway gateway, IOHandler ioHandler, GameEngine engine) {
        this.gateway = Objects.requireNonNull(gateway, "gateway must not be null");
        this.ioHandler = Objects.requireNonNull(ioHandler, "ioHandler must not be null");
        this.engine = Objects.requireNonNull(engine, "engine must not be null");
    }
    
    /**
     * Main game loop. Continues until the game ends.
     */
    public void play() {
        while (gateway.isActive() && !gateway.getState().isGameEnded()) {
            playTurn();
        }
        
        if (gateway.getState().isGameEnded()) {
            showGameOver();
        }
    }
    
    /**
     * Plays a single turn for the current player.
     */
    private void playTurn() {
        GameState state = gateway.getState();
        Player currentPlayer = state.getCurrentPlayer();
        
        // Show turn start
        ioHandler.showTurnBeginConfirm(currentPlayer);
        
        // Start turn action
        GameResult result = gateway.applyAction(new GameAction.StartTurn());
        if (!result.isSuccess()) {
            handleError(result);
            return;
        }
        
        // Roll and choose dice until pick/steal or bust
        boolean turnEnded = false;
        while (!turnEnded && gateway.isActive()) {
            state = gateway.getState();
            
            if (state.getPhase() == GameState.Phase.ROLL_OR_ACTION) {
                turnEnded = handleRollOrActionPhase(state);
            } else if (state.getPhase() == GameState.Phase.CHOOSING_DIE_FACE) {
                turnEnded = handleChoosingDiePhase(state);
            } else {
                // Turn ended (moved to next player)
                turnEnded = true;
            }
        }
    }
    
    /**
     * Handles the ROLL_OR_ACTION phase where player can roll, pick, or steal.
     * 
     * @return true if turn ended, false if continuing
     */
    private boolean handleRollOrActionPhase(GameState state) {
        Player currentPlayer = state.getCurrentPlayer();
        
        // Check if forced to pick
        if (engine.isForcedToPick(state)) {
            ioHandler.printMessage("You must pick a tile!");
            return executePick(state);
        }
        
        // Show current state
        ioHandler.showPlayerData(currentPlayer, state.getDice(), state.getPlayers());
        ioHandler.showBoardTiles(state.getBoardTiles());
        
        // Check available actions
        boolean hasWorm = engine.hasWormChosen(state);
        boolean canPick = engine.canPick(state);
        boolean canSteal = engine.canSteal(state);
        
        // If player has WORM and can pick/steal, ask what they want to do
        if (hasWorm && (canPick || canSteal)) {
            // Ask player what to do
            if (canPick && askPlayerToPick(state)) {
                return executePick(state);
            } else if (canSteal && askPlayerToSteal(state)) {
                return executeSteal(state);
            }
            // Player declined both - continue rolling
        }
        
        // Roll dice (either no WORM, or player declined pick/steal)
        return executeRoll();
    }
    
    /**
     * Handles the CHOOSING_DIE_FACE phase where player selects a die face.
     * 
     * @return true if turn ended (bust), false if continuing
     */
    private boolean handleChoosingDiePhase(GameState state) {
        Player currentPlayer = state.getCurrentPlayer();
        
        // Show rolled dice
        ioHandler.showRolledDice(state.getDice());
        
        // Ask player to choose a die face
        Die.Face chosenFace = ioHandler.chooseDie(currentPlayer);
        
        GameResult result = gateway.applyAction(new GameAction.ChooseDieFace(chosenFace));
        
        if (!result.isSuccess()) {
            handleError(result);
            return false; // Try again
        }
        
        // Check if turn ended due to no more dice or no pickable faces (bust handled by engine)
        return result.newState().getPhase() == GameState.Phase.WAITING_TURN_START;
    }
    
    private boolean askPlayerToPick(GameState state) {
        int score = state.getDice().getScore();
        int availableTileNumber = state.getBoardTiles().nearestTile(score).number();
        return ioHandler.wantToPick(state.getCurrentPlayer(), score, availableTileNumber);
    }
    
    private boolean askPlayerToSteal(GameState state) {
        // Find a player to steal from
        int currentScore = state.getDice().getScore();
        Player[] players = state.getPlayers();
        
        for (int i = 0; i < players.length; i++) {
            if (i != state.getCurrentPlayerIndex() && players[i].canStealTile(currentScore)) {
                if (ioHandler.wantToSteal(state.getCurrentPlayer(), players[i])) {
                    // Found target, will execute steal
                    return true;
                }
            }
        }
        return false;
    }
    
    private boolean executePick(GameState state) {
        GameResult result = gateway.applyAction(new GameAction.PickTileAction());
        
        if (!result.isSuccess()) {
            handleError(result);
            return false;
        }
        
        return true; // Turn ended
    }
    
    private boolean executeSteal(GameState state) {
        // Find the player to steal from based on current score
        int currentScore = state.getDice().getScore();
        Player[] players = state.getPlayers();
        int targetIndex = -1;
        
        for (int i = 0; i < players.length; i++) {
            if (i != state.getCurrentPlayerIndex() && players[i].canStealTile(currentScore)) {
                if (ioHandler.wantToSteal(state.getCurrentPlayer(), players[i])) {
                    targetIndex = i;
                    break;
                }
            }
        }
        
        if (targetIndex == -1) {
            return false; // No target found, continue turn
        }
        
        GameResult result = gateway.applyAction(new GameAction.StealTileAction(targetIndex));
        
        if (!result.isSuccess()) {
            handleError(result);
            return false;
        }
        
        return true; // Turn ended
    }
    
    private boolean executeRoll() {
        GameResult result = gateway.applyAction(new GameAction.RollDice());
        
        if (!result.isSuccess()) {
            handleError(result);
            return true; // Likely a bust, turn ended
        }
        
        // Check if bust occurred (turn moved to next player)
        if (result.newState().getPhase() == GameState.Phase.WAITING_TURN_START) {
            ioHandler.showBustMessage();
            return true;
        }
        
        return false; // Continue to choosing die phase
    }
    
    private void handleError(GameResult result) {
        for (String error : result.errors()) {
            ioHandler.printError(error);
        }
    }
    
    private void showGameOver() {
        GameState state = gateway.getState();
        if (state.hasWinner()) {
            ioHandler.printMessage("Game Over! Winner: " + state.getWinner().getName());
        } else {
            ioHandler.printMessage("Game Over! No winner.");
        }
        ioHandler.backToMenu();
    }
}
