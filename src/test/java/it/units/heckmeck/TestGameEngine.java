package it.units.heckmeck;

import Heckmeck.GameEngine;
import Heckmeck.GameState;
import Heckmeck.HeckmeckRules;
import Heckmeck.Rules;
import Heckmeck.Components.BoardTiles;
import Heckmeck.Components.Dice;
import Heckmeck.Components.Die;
import Heckmeck.Components.Player;
import Heckmeck.Components.Tile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static it.units.heckmeck.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestGameEngine {

    private GameEngine engine;
    private Rules rules;
    private Player[] players;

    @BeforeEach
    public void setUp() {
        rules = new HeckmeckRules();
        engine = new GameEngine(rules);
        players = createTestPlayers();
    }

    @Test
    public void testStartNewGame() {
        GameState state = engine.startNewGame(players);

        assertNotNull(state);
        assertEquals(3, state.getNumberOfPlayers());
        assertEquals(0, state.getCurrentPlayerIndex());
        assertEquals(GameState.Phase.WAITING_TURN_START, state.getPhase());
        assertFalse(state.isGameEnded());
        assertNotNull(state.getDice());
        assertNotNull(state.getBoardTiles());
    }

    @Test
    public void testStartNewGameWithNullPlayersThrowsException() {
        assertThrows(NullPointerException.class, () -> {
            engine.startNewGame(null);
        });
    }

    @Test
    public void testStartTurn() {
        GameState state = createFreshGameState(players);
        
        GameState newState = engine.startTurn(state);

        assertEquals(GameState.Phase.ROLL_OR_ACTION, newState.getPhase());
    }

    @Test
    public void testStartTurnOnEndedGameThrowsException() {
        GameState state = createFreshGameState(players);
        GameState endedState = state.withGameEnded(true, players[0]);

        assertThrows(IllegalStateException.class, () -> {
            engine.startTurn(endedState);
        });
    }

    @Test
    public void testRollWithAvailableDice() {
        GameState state = createFreshGameState(players);
        state = engine.startTurn(state);

        GameState rolledState = engine.roll(state);

        // Dopo il roll, se ci sono facce pickabili, va in CHOOSING_DIE_FACE
        // altrimenti fa bust (dipende dal risultato del dado)
        assertTrue(
            rolledState.getPhase() == GameState.Phase.CHOOSING_DIE_FACE ||
            rolledState.getPhase() == GameState.Phase.WAITING_TURN_START // bust
        );
    }

    @Test
    public void testRollWithWrongPhaseThrowsException() {
        GameState state = createFreshGameState(players);
        // Fase WAITING_TURN_START, non ROLL_OR_ACTION

        assertThrows(IllegalStateException.class, () -> {
            engine.roll(state);
        });
    }

    @Test
    public void testRollWithNoDiceCausesBust() {
        GameState state = createFreshGameState(players);
        state = engine.startTurn(state);
        
        // Crea scenario con 0 dadi disponibili
        Dice testDice = state.getDice();
        testDice.resetDice();
        testDice.getDiceList().clear();
        
        assertEquals(0, testDice.getNumOfDice(), "Should have no dice");
        
        GameState stateWithNoDice = state.withDice(testDice);
        GameState bustedState = engine.roll(stateWithNoDice);
        
        // Dopo bust, dovrebbe passare al prossimo giocatore
        assertEquals(GameState.Phase.WAITING_TURN_START, bustedState.getPhase());
        assertEquals(1, bustedState.getCurrentPlayerIndex(), "Should move to next player after bust");
    }

    @Test
    public void testChooseDieFace() {
        GameState state = createFreshGameState(players);
        state = engine.startTurn(state);
        
        // Roll dadi freschi - deve sempre avere facce disponibili
        Dice testDice = state.getDice();
        testDice.rollDice();
        
        // Dopo un roll fresco, DEVE esserci almeno una faccia pickabile
        assertTrue(testDice.canPickAFace(), "After fresh roll, there must be pickable faces");
        
        GameState stateWithPhase = state.withPhase(GameState.Phase.CHOOSING_DIE_FACE);
        GameState stateWithDice = stateWithPhase.withDice(testDice);
        
        // Prendi la prima faccia disponibile (garantita da canPickAFace())
        Die.Face faceToChoose = testDice.getDiceList().get(0).getDieFace();
        
        GameState newState = engine.chooseDieFace(stateWithDice, faceToChoose);
        assertEquals(GameState.Phase.ROLL_OR_ACTION, newState.getPhase());
        assertTrue(newState.getDice().isFaceChosen(faceToChoose));
    }

    @Test
    public void testChooseDieFaceNotPresentThrowsException() {
        GameState state = createFreshGameState(players);
        state = engine.startTurn(state);
        
        // Crea uno scenario deterministico: aggiungi solo ONE, TWO, THREE, FOUR, FIVE
        // WORM sarà garantito mancante
        Dice testDice = state.getDice();
        testDice.resetDice();
        testDice.getDiceList().clear(); // Svuota i dadi esistenti
        
        testDice.addSpecificDie(Die.Face.ONE);
        testDice.addSpecificDie(Die.Face.TWO);
        testDice.addSpecificDie(Die.Face.THREE);
        testDice.addSpecificDie(Die.Face.FOUR);
        testDice.addSpecificDie(Die.Face.FIVE);
        
        // Verifica che WORM non sia presente
        assertFalse(testDice.isFacePresent(Die.Face.WORM), 
            "WORM should not be present in the dice list");
        
        GameState stateWithPhase = state.withPhase(GameState.Phase.CHOOSING_DIE_FACE);
        GameState finalState = stateWithPhase.withDice(testDice);
        
        // Prova a scegliere WORM che non è presente
        assertThrows(IllegalStateException.class, () -> {
            engine.chooseDieFace(finalState, Die.Face.WORM);
        });
    }

    @Test
    public void testChooseDieFaceAlreadyChosenThrowsException() {
        GameState state = createFreshGameState(players);
        state = engine.startTurn(state);
        
        Dice testDice = state.getDice();
        testDice.rollDice();
        
        assertTrue(testDice.canPickAFace(), "After fresh roll, there must be pickable faces");
        
        GameState stateWithPhase = state.withPhase(GameState.Phase.CHOOSING_DIE_FACE);
        GameState stateWithDice = stateWithPhase.withDice(testDice);
        
        // Prendi la prima faccia disponibile
        Die.Face faceToChoose = testDice.getDiceList().get(0).getDieFace();
        
        GameState newState = engine.chooseDieFace(stateWithDice, faceToChoose);
        
        // Prova a scegliere di nuovo la stessa faccia
        assertThrows(IllegalStateException.class, () -> {
            engine.chooseDieFace(newState, faceToChoose);
        });
    }

    @Test
    public void testHasWormChosen() {
        GameState state = createFreshGameState(players);
        
        Dice testDice = state.getDice();
        testDice.resetDice();
        
        assertFalse(engine.hasWormChosen(state.withDice(testDice)));
        
        // Crea uno scenario deterministico con un WORM
        testDice.getDiceList().clear();
        testDice.addSpecificDie(Die.Face.WORM);
        testDice.addSpecificDie(Die.Face.ONE);
        testDice.addSpecificDie(Die.Face.TWO);
        
        assertTrue(testDice.isFacePresent(Die.Face.WORM), "WORM should be present");
        
        testDice.chooseDice(Die.Face.WORM);
        assertTrue(engine.hasWormChosen(state.withDice(testDice)));
    }

    @Test
    public void testCanPick() {
        GameState state = createFreshGameState(players);
        
        Dice testDice = state.getDice();
        testDice.resetDice();
        
        // Senza punteggio sufficiente
        assertFalse(engine.canPick(state.withDice(testDice)));
        
        // Crea scenario deterministico: WORM + quattro FIVE = 5 + 20 = 25 punti
        testDice.getDiceList().clear();
        testDice.addSpecificDie(Die.Face.WORM);  // 5 punti
        testDice.addSpecificDie(Die.Face.FIVE);  // 5 punti
        testDice.addSpecificDie(Die.Face.FIVE);
        testDice.addSpecificDie(Die.Face.FIVE);
        testDice.addSpecificDie(Die.Face.FIVE);
        
        testDice.chooseDice(Die.Face.WORM);
        testDice.chooseDice(Die.Face.FIVE);
        
        assertEquals(25, testDice.getScore());
        assertTrue(engine.canPick(state.withDice(testDice)), 
            "Should be able to pick with score of 25");
    }

    @Test
    public void testCanPickWithEmptyBoard() {
        // Crea board vuoto
        BoardTiles emptyBoard = BoardTiles.init();
        while (emptyBoard.hasElement()) {
            emptyBoard.removeLastTile();
        }
        
        GameState state = GameState.initial(players, Dice.init(), emptyBoard);
        
        Dice testDice = state.getDice();
        testDice.resetDice();
        testDice.getDiceList().clear();
        testDice.addSpecificDie(Die.Face.WORM);
        for (int i = 0; i < 7; i++) {
            testDice.addSpecificDie(Die.Face.FIVE);
        }
        
        testDice.chooseDice(Die.Face.WORM);
        testDice.chooseDice(Die.Face.FIVE);
        
        // Anche con punteggio alto, non si può prendere da un board vuoto
        assertFalse(engine.canPick(state.withDice(testDice)), 
            "Should not be able to pick from empty board");
    }

    @Test
    public void testCanSteal() {
        GameState state = createFreshGameState(players);
        
        // Dai una tessera a un giocatore
        Player player1 = state.getPlayers()[1];
        player1.pickTile(Tile.generateTile(25));
        
        Player[] updatedPlayers = state.getPlayers();
        updatedPlayers[1] = player1;
        state = state.withPlayers(updatedPlayers);
        
        // Scenario 1: senza punteggio sufficiente
        Dice testDice = state.getDice();
        testDice.resetDice();
        assertFalse(engine.canSteal(state.withDice(testDice)));
        
        // Scenario 2: con punteggio 25 (stesso valore della tessera del player1)
        testDice.getDiceList().clear();
        testDice.addSpecificDie(Die.Face.WORM);  // 5 punti
        testDice.addSpecificDie(Die.Face.FIVE);  // 5 punti x 4 = 20
        testDice.addSpecificDie(Die.Face.FIVE);
        testDice.addSpecificDie(Die.Face.FIVE);
        testDice.addSpecificDie(Die.Face.FIVE);
        
        testDice.chooseDice(Die.Face.WORM);
        testDice.chooseDice(Die.Face.FIVE);
        
        assertEquals(25, testDice.getScore());
        assertTrue(engine.canSteal(state.withDice(testDice)), 
            "Should be able to steal with score of 25 matching player1's tile");
    }

    @Test
    public void testCanStealWithNoValidPlayers() {
        GameState state = createFreshGameState(players);
        
        // Nessun giocatore ha tessere
        Dice testDice = state.getDice();
        testDice.resetDice();
        testDice.getDiceList().clear();
        testDice.addSpecificDie(Die.Face.WORM);
        for (int i = 0; i < 4; i++) {
            testDice.addSpecificDie(Die.Face.FIVE);
        }
        
        testDice.chooseDice(Die.Face.WORM);
        testDice.chooseDice(Die.Face.FIVE);
        
        // Anche con punteggio alto, non si può rubare se nessuno ha tessere
        assertFalse(engine.canSteal(state.withDice(testDice)), 
            "Should not be able to steal when no player has tiles");
    }

    @Test
    public void testPickTileAndEndTurnWithoutWormThrowsException() {
        GameState state = createFreshGameState(players);
        GameState turnStartedState = engine.startTurn(state);
        
        // Senza aver scelto un WORM
        assertThrows(IllegalStateException.class, () -> {
            engine.pickTileAndEndTurn(turnStartedState);
        });
    }

    @Test
    public void testPickTileAndEndTurnSuccessfully() {
        GameState state = createFreshGameState(players);
        state = engine.startTurn(state);
        
        // Crea scenario deterministico: WORM + 4 FIVE = 25 punti
        Dice testDice = state.getDice();
        testDice.resetDice();
        testDice.getDiceList().clear();
        testDice.addSpecificDie(Die.Face.WORM);
        for (int i = 0; i < 4; i++) {
            testDice.addSpecificDie(Die.Face.FIVE);
        }
        
        testDice.chooseDice(Die.Face.WORM);
        testDice.chooseDice(Die.Face.FIVE);
        
        int initialBoardSize = state.getBoardTiles().tiles().size();
        
        GameState stateWithDice = state.withDice(testDice).withPhase(GameState.Phase.ROLL_OR_ACTION);
        GameState endedState = engine.pickTileAndEndTurn(stateWithDice);
        
        // Verifica che il turno sia finito e sia passato al prossimo giocatore
        assertEquals(GameState.Phase.WAITING_TURN_START, endedState.getPhase());
        assertEquals(1, endedState.getCurrentPlayerIndex(), "Should move to next player");
        
        // Verifica che una tessera sia stata rimossa dal board
        assertEquals(initialBoardSize - 1, endedState.getBoardTiles().tiles().size());
        
        // Verifica che il giocatore abbia acquisito una tessera
        assertTrue(endedState.getPlayers()[0].hasTile(), "Player should have at least one tile");
    }

    @Test
    public void testStealTileWithoutWormThrowsException() {
        GameState state = createFreshGameState(players);
        GameState turnStartedState = engine.startTurn(state);
        
        assertThrows(IllegalStateException.class, () -> {
            engine.stealTileAndEndTurn(turnStartedState, 1);
        });
    }

    @Test
    public void testStealTileAndEndTurnSuccessfully() {
        GameState state = createFreshGameState(players);
        state = engine.startTurn(state);
        
        // Dai una tessera al giocatore 1 (Bob)
        Player bob = state.getPlayers()[1];
        bob.pickTile(Tile.generateTile(25));
        Player[] updatedPlayers = state.getPlayers();
        updatedPlayers[1] = bob;
        state = state.withPlayers(updatedPlayers);
        
        // Crea dadi con WORM e punteggio 25
        Dice testDice = state.getDice();
        testDice.resetDice();
        testDice.getDiceList().clear();
        testDice.addSpecificDie(Die.Face.WORM);
        for (int i = 0; i < 4; i++) {
            testDice.addSpecificDie(Die.Face.FIVE);
        }
        
        testDice.chooseDice(Die.Face.WORM);
        testDice.chooseDice(Die.Face.FIVE);
        
        GameState stateWithDice = state.withDice(testDice).withPhase(GameState.Phase.ROLL_OR_ACTION);
        GameState endedState = engine.stealTileAndEndTurn(stateWithDice, 1);
        
        // Verifica che il turno sia finito
        assertEquals(GameState.Phase.WAITING_TURN_START, endedState.getPhase());
        assertEquals(1, endedState.getCurrentPlayerIndex(), "Should move to next player");
        
        // Alice dovrebbe aver guadagnato una tessera (ora ha almeno una)
        assertTrue(endedState.getPlayers()[0].hasTile(), "Alice should have stolen a tile");
        
        // Bob dovrebbe aver perso la tessera (non ne ha più)
        assertFalse(endedState.getPlayers()[1].hasTile(), "Bob should have lost the tile");
    }

    @Test
    public void testStealFromSelfThrowsException() {
        GameState state = createFreshGameState(players);
        state = engine.startTurn(state);
        
        // Crea uno scenario deterministico con WORM
        Dice testDice = state.getDice();
        testDice.resetDice();
        testDice.getDiceList().clear();
        testDice.addSpecificDie(Die.Face.WORM);
        testDice.addSpecificDie(Die.Face.FIVE);
        
        assertTrue(testDice.isFacePresent(Die.Face.WORM), "WORM should be present");
        
        testDice.chooseDice(Die.Face.WORM);
        GameState finalState = state.withDice(testDice);
        
        int currentIndex = finalState.getCurrentPlayerIndex();
        assertThrows(IllegalStateException.class, () -> {
            engine.stealTileAndEndTurn(finalState, currentIndex);
        });
    }

    @Test
    public void testStealWithInvalidPlayerIndexThrowsException() {
        GameState state = createFreshGameState(players);
        GameState turnStartedState = engine.startTurn(state);
        
        // Dai una tessera con valore 25 a un giocatore per rendere lo steal possibile
        Player player1 = turnStartedState.getPlayers()[1];
        player1.pickTile(Tile.generateTile(25)); // Tessera con valore 25
        Player[] updatedPlayers = turnStartedState.getPlayers();
        updatedPlayers[1] = player1;
        
        // Crea uno scenario deterministico: WORM + quattro FIVE = 5 (WORM) + 4*5 = 25 punti
        Dice testDice = turnStartedState.getDice();
        testDice.resetDice();
        testDice.getDiceList().clear();
        testDice.addSpecificDie(Die.Face.WORM);
        testDice.addSpecificDie(Die.Face.FIVE);
        testDice.addSpecificDie(Die.Face.FIVE);
        testDice.addSpecificDie(Die.Face.FIVE);
        testDice.addSpecificDie(Die.Face.FIVE);
        
        // Scegli WORM e tutti i FIVE per raggiungere 25 punti
        testDice.chooseDice(Die.Face.WORM);
        testDice.chooseDice(Die.Face.FIVE);
        
        assertEquals(25, testDice.getScore(), "Score should be exactly 25");
        
        GameState stateWithDice = turnStartedState.withPlayers(updatedPlayers).withDice(testDice);
        
        assertThrows(IllegalArgumentException.class, () -> {
            engine.stealTileAndEndTurn(stateWithDice, -1);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            engine.stealTileAndEndTurn(stateWithDice, 999);
        });
    }

    @Test
    public void testIsForcedToPick() {
        GameState state = createFreshGameState(players);
        
        Dice testDice = state.getDice();
        testDice.resetDice();
        
        // Scenario 1: senza dadi scelti
        assertFalse(engine.isForcedToPick(state.withDice(testDice)));
        
        // Scenario 2: con tutti gli 8 dadi scelti e punteggio sufficiente
        // Crea 8 dadi: 1 WORM + 7 FIVE = 5 + 35 = 40 punti (> 21, quindi canPick = true)
        testDice.getDiceList().clear();
        testDice.addSpecificDie(Die.Face.WORM);
        for (int i = 0; i < 7; i++) {
            testDice.addSpecificDie(Die.Face.FIVE);
        }
        
        // Scegli tutti i dadi
        testDice.chooseDice(Die.Face.WORM);
        testDice.chooseDice(Die.Face.FIVE);
        
        assertEquals(8, testDice.getChosenDice().size(), "All 8 dice should be chosen");
        assertEquals(40, testDice.getScore());
        assertTrue(engine.canPick(state.withDice(testDice)));
        assertTrue(engine.isForcedToPick(state.withDice(testDice)), 
            "Should be forced to pick when all 8 dice are chosen and can pick");
    }

    @Test
    public void testGameEngineRequiresRules() {
        assertThrows(NullPointerException.class, () -> {
            new GameEngine(null);
        });
    }

    @Test
    public void testMultipleTurnsProgression() {
        GameState state = engine.startNewGame(players);
        
        assertEquals("Alice", state.getCurrentPlayer().getName());
        
        // Inizia turno Alice
        state = engine.startTurn(state);
        assertEquals(GameState.Phase.ROLL_OR_ACTION, state.getPhase());
        
        // Simula un bust per passare al prossimo giocatore
        // (il bust può avvenire in vari modi, qui testiamo solo la progressione)
    }

    @Test
    public void testCompleteTurnWithPickTile() {
        GameState state = engine.startNewGame(players);
        assertEquals("Alice", state.getCurrentPlayer().getName());
        
        // 1. Start turn
        state = engine.startTurn(state);
        assertEquals(GameState.Phase.ROLL_OR_ACTION, state.getPhase());
        
        // 2. Setup deterministic dice for first roll
        Dice testDice = state.getDice();
        testDice.resetDice();
        testDice.getDiceList().clear();
        testDice.addSpecificDie(Die.Face.WORM);
        testDice.addSpecificDie(Die.Face.WORM);
        testDice.addSpecificDie(Die.Face.ONE);
        state = state.withDice(testDice);
        
        // 3. Roll (simulated)
        state = state.withPhase(GameState.Phase.CHOOSING_DIE_FACE);
        
        // 4. Choose WORM
        state = engine.chooseDieFace(state, Die.Face.WORM);
        assertEquals(GameState.Phase.ROLL_OR_ACTION, state.getPhase());
        assertTrue(state.getDice().isFaceChosen(Die.Face.WORM));
        
        // 5. Setup second roll
        testDice = state.getDice();
        testDice.getDiceList().clear();
        for (int i = 0; i < 4; i++) {
            testDice.addSpecificDie(Die.Face.FIVE);
        }
        state = state.withDice(testDice).withPhase(GameState.Phase.CHOOSING_DIE_FACE);
        
        // 6. Choose FIVE
        state = engine.chooseDieFace(state, Die.Face.FIVE);
        
        // 7. Now we have WORM + FIVE chosen, score should be enough
        assertTrue(state.getDice().getScore() >= 21);
        assertTrue(engine.canPick(state));
        
        // 8. Pick tile and end turn
        state = engine.pickTileAndEndTurn(state);
        
        // 9. Verify turn ended and player got tile
        assertEquals(GameState.Phase.WAITING_TURN_START, state.getPhase());
        assertEquals("Bob", state.getCurrentPlayer().getName());
        assertTrue(state.getPlayers()[0].hasTile(), "Alice should have a tile after picking");
    }

    @Test
    public void testIsGameOver() {
        GameState state = createFreshGameState(players);
        
        assertFalse(engine.isGameOver(state));
        
        // Testa con gioco finito
        state = state.withGameEnded(true, players[0]);
        assertTrue(engine.isGameOver(state));
        
        // Testa con boardTiles vuoto
        BoardTiles emptyBoard = BoardTiles.init();
        while (emptyBoard.hasElement()) {
            emptyBoard.removeLastTile();
        }
        state = GameState.initial(players, Dice.init(), emptyBoard);
        assertTrue(engine.isGameOver(state));
    }

    @Test
    public void testDicePreservedAfterBustUntilNextTurn() {
        GameState state = createFreshGameState(players);
        state = engine.startTurn(state);
        
        // Scegliamo alcuni dadi per simulare un turno in corso
        Dice testDice = state.getDice();
        testDice.resetDice();
        testDice.getDiceList().clear();
        // Aggiungi dadi specifici
        for (int i = 0; i < 3; i++) {
            testDice.addSpecificDie(Die.Face.ONE);
        }
        testDice.chooseDice(Die.Face.ONE); // Sceglie TUTTI i dadi con faccia ONE (3 dadi)
        
        state = state.withDice(testDice);
        assertEquals(3, testDice.getChosenDice().size(), "Should have chosen 3 dice");
        
        // Roll con 0 dadi disponibili causa bust
        testDice.getDiceList().clear(); // Rimuoviamo tutti i dadi non scelti
        state = state.withDice(testDice);
        GameState bustedState = engine.roll(state);
        
        // DOPO BUST: i dadi scelti devono essere ancora presenti (non resettati)
        assertEquals(GameState.Phase.WAITING_TURN_START, bustedState.getPhase());
        assertEquals(1, bustedState.getCurrentPlayerIndex(), "Should move to next player");
        assertEquals(3, bustedState.getDice().getChosenDice().size(), 
            "Dice should NOT be reset after bust - preserved for display");
        
        // All'INIZIO DEL TURNO SUCCESSIVO: i dadi devono essere resettati
        GameState nextTurnState = engine.startTurn(bustedState);
        assertEquals(GameState.Phase.ROLL_OR_ACTION, nextTurnState.getPhase());
        assertEquals(8, nextTurnState.getDice().getNumOfDice(), "Dice should be reset at start of new turn");
        assertEquals(0, nextTurnState.getDice().getChosenDice().size(), "No dice should be chosen at turn start");
    }
}
