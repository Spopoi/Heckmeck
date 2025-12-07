package it.units.heckmeck;

import Heckmeck.GameAction;
import Heckmeck.Components.Die;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestGameAction {

    @Test
    public void testStartTurnCreation() {
        GameAction action = new GameAction.StartTurn();
        
        assertNotNull(action);
        assertTrue(action instanceof GameAction.StartTurn);
    }

    @Test
    public void testRollDiceCreation() {
        GameAction action = new GameAction.RollDice();
        
        assertNotNull(action);
        assertTrue(action instanceof GameAction.RollDice);
    }

    @Test
    public void testChooseDieFaceCreation() {
        GameAction action = new GameAction.ChooseDieFace(Die.Face.WORM);
        
        assertNotNull(action);
        assertTrue(action instanceof GameAction.ChooseDieFace);
    }

    @Test
    public void testChooseDieFaceGetFace() {
        GameAction.ChooseDieFace action = new GameAction.ChooseDieFace(Die.Face.WORM);
        
        assertEquals(Die.Face.WORM, action.face());
    }

    @Test
    public void testChooseDieFaceWithAllFaces() {
        for (Die.Face face : Die.Face.values()) {
            GameAction.ChooseDieFace action = new GameAction.ChooseDieFace(face);
            assertEquals(face, action.face());
        }
    }

    @Test
    public void testPickTileActionCreation() {
        GameAction action = new GameAction.PickTileAction();
        
        assertNotNull(action);
        assertTrue(action instanceof GameAction.PickTileAction);
    }

    @Test
    public void testStealTileActionCreation() {
        GameAction action = new GameAction.StealTileAction(1);
        
        assertNotNull(action);
        assertTrue(action instanceof GameAction.StealTileAction);
    }

    @Test
    public void testStealTileActionGetRobbedPlayerIndex() {
        GameAction.StealTileAction action = new GameAction.StealTileAction(2);
        
        assertEquals(2, action.robbedPlayerIndex());
    }

    @Test
    public void testStealTileActionWithDifferentIndices() {
        for (int i = 0; i < 10; i++) {
            GameAction.StealTileAction action = new GameAction.StealTileAction(i);
            assertEquals(i, action.robbedPlayerIndex());
        }
    }

    @Test
    public void testActionEquality() {
        // StartTurn
        GameAction.StartTurn st1 = new GameAction.StartTurn();
        GameAction.StartTurn st2 = new GameAction.StartTurn();
        assertEquals(st1, st2);
        assertEquals(st1.hashCode(), st2.hashCode());

        // RollDice
        GameAction.RollDice rd1 = new GameAction.RollDice();
        GameAction.RollDice rd2 = new GameAction.RollDice();
        assertEquals(rd1, rd2);
        assertEquals(rd1.hashCode(), rd2.hashCode());

        // PickTileAction
        GameAction.PickTileAction pt1 = new GameAction.PickTileAction();
        GameAction.PickTileAction pt2 = new GameAction.PickTileAction();
        assertEquals(pt1, pt2);
        assertEquals(pt1.hashCode(), pt2.hashCode());
    }

    @Test
    public void testChooseDieFaceEquality() {
        GameAction.ChooseDieFace cdf1 = new GameAction.ChooseDieFace(Die.Face.WORM);
        GameAction.ChooseDieFace cdf2 = new GameAction.ChooseDieFace(Die.Face.WORM);
        GameAction.ChooseDieFace cdf3 = new GameAction.ChooseDieFace(Die.Face.ONE);
        
        assertEquals(cdf1, cdf2);
        assertEquals(cdf1.hashCode(), cdf2.hashCode());
        assertNotEquals(cdf1, cdf3);
    }

    @Test
    public void testStealTileActionEquality() {
        GameAction.StealTileAction sta1 = new GameAction.StealTileAction(1);
        GameAction.StealTileAction sta2 = new GameAction.StealTileAction(1);
        GameAction.StealTileAction sta3 = new GameAction.StealTileAction(2);
        
        assertEquals(sta1, sta2);
        assertEquals(sta1.hashCode(), sta2.hashCode());
        assertNotEquals(sta1, sta3);
    }

    @Test
    public void testActionToString() {
        GameAction.StartTurn st = new GameAction.StartTurn();
        assertNotNull(st.toString());
        assertTrue(st.toString().contains("StartTurn"));

        GameAction.RollDice rd = new GameAction.RollDice();
        assertNotNull(rd.toString());
        assertTrue(rd.toString().contains("RollDice"));

        GameAction.ChooseDieFace cdf = new GameAction.ChooseDieFace(Die.Face.WORM);
        assertNotNull(cdf.toString());
        assertTrue(cdf.toString().contains("ChooseDieFace"));
        assertTrue(cdf.toString().contains("WORM"));

        GameAction.PickTileAction pt = new GameAction.PickTileAction();
        assertNotNull(pt.toString());
        assertTrue(pt.toString().contains("PickTileAction"));

        GameAction.StealTileAction sta = new GameAction.StealTileAction(1);
        assertNotNull(sta.toString());
        assertTrue(sta.toString().contains("StealTileAction"));
        assertTrue(sta.toString().contains("1"));
    }

    @Test
    public void testActionInstanceOf() {
        GameAction startTurn = new GameAction.StartTurn();
        GameAction rollDice = new GameAction.RollDice();
        GameAction chooseDie = new GameAction.ChooseDieFace(Die.Face.FIVE);
        GameAction pickTile = new GameAction.PickTileAction();
        GameAction stealTile = new GameAction.StealTileAction(0);

        assertTrue(startTurn instanceof GameAction.StartTurn);
        assertFalse(startTurn instanceof GameAction.RollDice);
        
        assertTrue(rollDice instanceof GameAction.RollDice);
        assertFalse(rollDice instanceof GameAction.PickTileAction);
        
        assertTrue(chooseDie instanceof GameAction.ChooseDieFace);
        assertTrue(pickTile instanceof GameAction.PickTileAction);
        assertTrue(stealTile instanceof GameAction.StealTileAction);
    }

    @Test
    public void testActionPatternMatching() {
        GameAction action = new GameAction.ChooseDieFace(Die.Face.WORM);
        
        String result = switch (action) {
            case GameAction.StartTurn() -> "start";
            case GameAction.RollDice() -> "roll";
            case GameAction.ChooseDieFace c -> "choose:" + c.face();
            case GameAction.PickTileAction() -> "pick";
            case GameAction.StealTileAction s -> "steal:" + s.robbedPlayerIndex();
        };
        
        assertEquals("choose:WORM", result);
    }

    @Test
    public void testAllActionsAreGameActions() {
        assertTrue(new GameAction.StartTurn() instanceof GameAction);
        assertTrue(new GameAction.RollDice() instanceof GameAction);
        assertTrue(new GameAction.ChooseDieFace(Die.Face.ONE) instanceof GameAction);
        assertTrue(new GameAction.PickTileAction() instanceof GameAction);
        assertTrue(new GameAction.StealTileAction(0) instanceof GameAction);
    }

    @Test
    public void testStealTileActionWithNegativeIndex() {
        // Il record accetta valori negativi, la validazione è nel GameEngine
        GameAction.StealTileAction action = new GameAction.StealTileAction(-1);
        assertEquals(-1, action.robbedPlayerIndex());
    }

    @Test
    public void testRecordImmutability() {
        GameAction.ChooseDieFace action = new GameAction.ChooseDieFace(Die.Face.WORM);
        
        // Non possiamo modificare il campo (è final nel record)
        assertEquals(Die.Face.WORM, action.face());
        
        GameAction.StealTileAction stealAction = new GameAction.StealTileAction(1);
        assertEquals(1, stealAction.robbedPlayerIndex());
    }
}
