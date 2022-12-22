package it.units.heckmeck;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.stream.*;
import java.util.*;

import Heckmeck.Die;
import Heckmeck.Dice;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static Heckmeck.Die.Face.*;

public class TestDice {

    @Test
    void getFaceByNumber(){

        Assertions.assertEquals("ONE", Die.Face.ONE.toString());
        Assertions.assertEquals("TWO", Die.Face.TWO.toString());
        Assertions.assertEquals("THREE", Die.Face.THREE.toString());
        Assertions.assertEquals("FOUR", Die.Face.FOUR.toString());
        Assertions.assertEquals("FIVE", Die.Face.FIVE.toString());
        Assertions.assertEquals("WORM", Die.Face.WORM.toString());
    }

    @Test
    void convertStringToDieFace() {
        Assertions.assertAll(
                () -> Assertions.assertEquals(ONE, Die.getFaceByString("1")),
                () -> Assertions.assertEquals(TWO, Die.getFaceByString("2")),
                () -> Assertions.assertEquals(THREE, Die.getFaceByString("3")),
                () -> Assertions.assertEquals(FOUR, Die.getFaceByString("4")),
                () -> Assertions.assertEquals(FIVE, Die.getFaceByString("5")),
                () -> Assertions.assertEquals(WORM, Die.getFaceByString("w"))
        );
    }

    @Test
    void initDie(){
        Die die = Die.generateDie();
        String type = die.getClass().getSimpleName();
        Assertions.assertEquals(type, "Die");
    }

    @Test
    void rollDie(){
        Die die = Die.generateDie();
        die.rollDie();
        Die.Face result = die.getDieFace();
        Assertions.assertTrue(Stream.of(Die.Face.values()).anyMatch(e -> e.equals(result)));
    }

    @Test
    void initDice(){
        Dice dice = Dice.init();
        List <Die> diceList =  dice.getDiceList();
        int size = diceList.size();
        Assertions.assertEquals(8, size);
        for(Die d : diceList){
            String type = d.getClass().getSimpleName();
            Assertions.assertEquals(type, "Die");
        }
    }

    @Test
    void rollInitialDice(){
        Dice dice = Dice.init();
        dice.rollDice();
        List <Die> diceList =  dice.getDiceList();
        int size = diceList.size();
        Assertions.assertEquals(8, size);
        for (Die d : diceList){
            Die.Face result = d.getDieFace();
            Assertions.assertTrue(Stream.of(Die.Face.values()).anyMatch(e -> e.equals(result)));
        }
    }

    @Test
    void deleteDice(){
        Dice dice = Dice.init();
        dice.eraseDice();
        int numOfDice = dice.getNumOfDice();
        boolean test = numOfDice == 0;
        Assertions.assertTrue(test);
    }

    @Test
    void resetDice(){
        Dice dice = Dice.init();
        Assertions.assertEquals(8, dice.getNumOfDice());
        dice.eraseDice();
        Assertions.assertEquals(0, dice.getNumOfDice());
        dice.resetDice();
        Assertions.assertEquals(8, dice.getNumOfDice());

    }

    @Test
    void rollSequence(){

        Dice dice = Dice.init();
        while(dice.getNumOfDice()>0){
            dice.rollDice();
            List<Die> dieList = dice.getDiceList();
            for(Die die : dieList){
                boolean test = Arrays.asList(values()).contains(die.getDieFace());
                Assertions.assertTrue(test);
            }
            dice.chooseDice(getRandomChoosableDice(dice).getDieFace());
        }
    }
    @Test
    void addSpecificDie(){
        Dice dice = Dice.init();
        dice.eraseDice();
        dice.addSpecificDie(ONE);
        List <Die> diceList =  dice.getDiceList();
        Assertions.assertTrue(diceList.stream().anyMatch(e -> e.getDieFace().equals(ONE)));
    }

    @Test
    void isFacePresent(){
        Dice dice = Dice.init();
        dice.rollDice();
        dice.addSpecificDie(TWO);
        Assertions.assertTrue(dice.isFacePresent(TWO));
    }

    @Test
    void isWormPresent(){
        Dice dice = Dice.init();
        dice.rollDice();
        dice.addSpecificDie(WORM);
        Assertions.assertTrue(dice.isFacePresent(WORM));
    }

    @Test
    void chooseDie(){
        Dice dice = Dice.init();
        dice.eraseDice();
        dice.addSpecificDie(TWO);
        dice.chooseDice(TWO);
        List <Die> chosenDice = dice.getChosenDice();
        Assertions.assertTrue(chosenDice.stream().anyMatch(e -> e.getDieFace().equals(TWO)));
    }

    @Test
    void chooseRandomDice(){
        Dice dice = Dice.init();
        dice.rollDice();
        Die.Face face = dice.getDiceList().get(0).getDieFace();
        dice.chooseDice(getRandomChoosableDice(dice).getDieFace());
        List <Die> chosenDice = dice.getChosenDice();
        Assertions.assertEquals(face, chosenDice.get(0).getDieFace());
    }

    private Die getRandomChoosableDice(Dice dice){
        return dice.getDiceList().get(0);
    }

    @Test
    void isDieChosen(){
        Dice dice = Dice.init();
        dice.eraseDice();
        dice.addSpecificDie(THREE);
        dice.chooseDice(THREE);
        Assertions.assertTrue(dice.isFaceChosen(THREE));
    }

    @Test
    void isWormChosen(){
        Dice dice = Dice.init();
        dice.rollDice();
        dice.addSpecificDie(WORM);
        dice.chooseDice(WORM);
        Assertions.assertTrue(dice.isFaceChosen(WORM));
    }

    @Test
    void getChosenDice(){
        Dice dice = Dice.init();
        dice.eraseDice();
        dice.addSpecificDie(ONE);
        dice.addSpecificDie(TWO);
        dice.addSpecificDie(THREE);

        dice.chooseDice(ONE);
        Assertions.assertEquals(2, dice.getNumOfDice());
        dice.chooseDice(TWO);
        Assertions.assertEquals(1, dice.getNumOfDice());
        dice.chooseDice(THREE);
        Assertions.assertEquals(0, dice.getNumOfDice());

        List <Die> chosenDice = dice.getChosenDice();
        Assertions.assertTrue(chosenDice.stream().anyMatch(e -> e.getDieFace().equals(ONE)));
        Assertions.assertTrue(chosenDice.stream().anyMatch(e -> e.getDieFace().equals(TWO)));
        Assertions.assertTrue(chosenDice.stream().anyMatch(e -> e.getDieFace().equals(THREE)));
    }

    @ParameterizedTest
    @CsvSource({"ONE,1", "TWO, 2","THREE,3","FOUR,4","FIVE,5","WORM,5"})
    void getDieScore(Die.Face face, int score){
        Die die = Die.generateDie(face);
        Assertions.assertEquals(die.getDieScore(),score);
    }

    @Test
    void getScore(){
        Dice dice = Dice.init();
        dice.eraseDice();
        dice.addSpecificDie(ONE);
        dice.addSpecificDie(TWO);
        dice.addSpecificDie(THREE);
        dice.addSpecificDie(WORM);
        dice.addSpecificDie(WORM);

        dice.chooseDice(ONE);
        dice.chooseDice(TWO);
        dice.chooseDice(THREE);
        dice.chooseDice(WORM);
        dice.chooseDice(WORM);
        int score = dice.getScore();
        Assertions.assertEquals(1 + 2 + 3 + 5 + 5 , score);
    }


    @Test
    void gameRollSequence(){
        Dice dice = Dice.init();
        dice.rollDice();
        Assertions.assertEquals(8, dice.getDiceList().size());
        Die.Face face1 = dice.getDiceList().get(0).getDieFace();
        dice.chooseDice(getRandomChoosableDice(dice).getDieFace());
        Assertions.assertTrue(dice.isFaceChosen(face1));
        Assertions.assertEquals(8 - dice.getChosenDice().size(), dice.getDiceList().size() );
        while(dice.getDiceList().size() != 0){
            dice.rollDice();
            List <Die> availableDice = dice.getDiceList();
            Die.Face face = dice.getDiceList().get(0).getDieFace();
            dice.chooseDice(getRandomChoosableDice(dice).getDieFace());
            Assertions.assertTrue(dice.isFaceChosen(face));
            Assertions.assertEquals(8 - dice.getChosenDice().size(), dice.getDiceList().size() );
        }
    }


}