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
            GameState state = gateway.getState();
            int myPlayerIndex = gateway.getMyPlayerIndex();
            boolean isMyTurn = myPlayerIndex == -1 || myPlayerIndex == state.getCurrentPlayerIndex();
            
            if (isMyTurn) {
                playTurn();
            } else {
                // Not my turn, just wait and observe
                waitForMyTurn();
            }
        }
        
        if (gateway.getState().isGameEnded()) {
            showGameOver();
        }
    }
    
    /**
     * Plays a single turn for the current player (only called when it's the local player's turn).
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
                // Show current state before handling phase
                ioHandler.showPlayerData(state.getCurrentPlayer(), state.getDice(), state.getPlayers());
                ioHandler.showBoardTiles(state.getBoardTiles());
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
        // If has WORM, try player action (pick/steal), otherwise roll
        if (engine.hasWormChosen(state)) {
            if (tryPlayerAction(state)) {
                return true; // Action taken, turn ended
            }
        }
        
        // No action taken - roll dice
        executeRoll();
        
        // Check if bust occurred after roll
        GameState newState = gateway.getState();
        if (newState.getPhase() == GameState.Phase.WAITING_TURN_START) {
            ioHandler.showBustMessage();
            return true; // Turn ended due to bust
        }
        
        return false; // Continue to choosing die phase
    }
    
    /**
     * Tries to execute a player action (pick or steal).
     * Returns true if an action was taken, false if player wants to continue rolling.
     */
    private boolean tryPlayerAction(GameState state) {
        boolean canPick = engine.canPick(state);
        boolean canSteal = engine.canSteal(state);

        // No actions available - bust will be handled on next roll
        if (!canPick && !canSteal) {
            return false;
        }
        
        boolean noMoreDice = engine.hasNoMoreDice(state);

        // Steal has priority over pick
        if (canSteal) {
            if (noMoreDice) {
                showNoMoreDiceMessage(state, canPick, canSteal);
                return executeSteal(state); // Forced - don't ask
            }
            return trySteal(state); // Optional - ask player
        }

        if (canPick) {
            if (noMoreDice) {
                showNoMoreDiceMessage(state, canPick, canSteal);
                return executePick(state); // Forced - don't ask
            }
            return tryPick(state); // Optional - ask player
        }

        return false;
    }

    
    /**
     * Shows message when player has no more dice to roll.
     */
    private void showNoMoreDiceMessage(GameState state, boolean canPick, boolean canSteal) {
        StringBuilder msg = new StringBuilder("No more dice! ");
        
        if (canPick) {
            int tileNumber = engine.getPickableTileNumber(state);
            msg.append("You must pick tile ").append(tileNumber);
            
            if (canSteal) {
                Player stealablePlayer = engine.getStealablePlayer(state);
                msg.append(" or steal tile ").append(stealablePlayer.getLastPickedTile().number())
                   .append(" from ").append(stealablePlayer.getName());
            }
        } else if (canSteal) {
            Player stealablePlayer = engine.getStealablePlayer(state);
            msg.append("You must steal tile ").append(stealablePlayer.getLastPickedTile().number())
               .append(" from ").append(stealablePlayer.getName());
        } 
        ioHandler.printMessage(msg.toString());
    }
    
    /**
     * Tries to pick a tile. Returns true if pick was executed.
     */
    private boolean tryPick(GameState state) {
        if (askPlayerToPick(state)) {
            return executePick(state);
        }
        return false;
    }
    
    /**
     * Tries to steal from another player. Returns true if steal was executed.
     */
    private boolean trySteal(GameState state) {
        if (askPlayerToSteal(state)) {
            return executeSteal(state);
        }
        return false;
    }
    
    /**
     * Handles the CHOOSING_DIE_FACE phase where player selects a die face.
     * 
     * @return true if turn ended (bust), false if continuing
     */
    private boolean handleChoosingDiePhase(GameState state) {
        Player currentPlayer = state.getCurrentPlayer();
        
        // Loop until valid choice
        while (true) {
            // Ask player to choose a die face
            Die.Face chosenFace = ioHandler.chooseDie(currentPlayer);
            
            GameResult result = gateway.applyAction(new GameAction.ChooseDieFace(chosenFace));
            
            if (!result.isSuccess()) {
                handleError(result);
                // Continue loop to ask again without re-showing dice
                continue;
            }
            
            // Check if turn ended due to no more dice or no pickable faces (bust handled by engine)
            return result.newState().getPhase() == GameState.Phase.WAITING_TURN_START;
        }
    }
    
    private boolean askPlayerToPick(GameState state) {
        int score = state.getDice().getScore();
        int availableTileNumber = engine.getPickableTileNumber(state);
        return ioHandler.wantToPick(state.getCurrentPlayer(), score, availableTileNumber);
    }
    
    private boolean askPlayerToSteal(GameState state) {
        Player stealablePlayer = engine.getStealablePlayer(state);
        if (stealablePlayer == null) {
            return false; // No stealable player found
        }
        return ioHandler.wantToSteal(state.getCurrentPlayer(), stealablePlayer);
    }
    
    private boolean executePick(GameState state) {
        int tileNumber = engine.getPickableTileNumber(state);
        GameResult result = gateway.applyAction(new GameAction.PickTileAction());
        
        if (!result.isSuccess()) {
            handleError(result);
            return false;
        }
        
        ioHandler.printMessage("You got tile number " + tileNumber + "!");
        return true; // Turn ended
    }
    
    private boolean executeSteal(GameState state) {
        Player stealablePlayer = engine.getStealablePlayer(state);
        if (stealablePlayer == null) {
            return false; // No target found, continue turn
        }
        
        // Find the index of the stealable player
        Player[] players = state.getPlayers();
        int targetIndex = -1;
        for (int i = 0; i < players.length; i++) {
            if (players[i] == stealablePlayer) {
                targetIndex = i;
                break;
            }
        }
        
        int stolenTileNumber = stealablePlayer.getLastPickedTile().number();
        GameResult result = gateway.applyAction(new GameAction.StealTileAction(targetIndex));
        
        if (!result.isSuccess()) {
            handleError(result);
            return false;
        }
        
        ioHandler.printMessage("You stole tile number " + stolenTileNumber + " from " + stealablePlayer.getName() + "!");
        return true; // Turn ended
    }
    
    private void executeRoll() {
        GameResult result = gateway.applyAction(new GameAction.RollDice());
        
        if (!result.isSuccess()) {
            handleError(result);
            return;
        }
        
        // Show rolled dice (now preserved even after bust thanks to GameEngine change)
        ioHandler.showRolledDice(result.newState().getDice());
    }
    
    private void handleError(GameResult result) {
        for (String error : result.errors()) {
            ioHandler.printError(error);
        }
    }
    
    /**
     * Waits for state updates during other players' turns.
     * The receiver thread in RemoteGameGateway will update the state asynchronously.
     */
    private void waitForMyTurn() {
        try {
            Thread.sleep(500); // Check state every 500ms
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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
