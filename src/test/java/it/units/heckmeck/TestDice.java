package it.units.heckmeck;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.stream.*;
import java.util.*;

import Heckmeck.Die;
import Heckmeck.Dice;
import static Heckmeck.Die.Face.*;

public class TestDice {

    @Test
    void getFaceByNumber(){                // Fare tostring()

        Assertions.assertEquals("ONE", Die.Face.ONE.toString());
        Assertions.assertEquals("TWO", Die.Face.TWO.toString());
        Assertions.assertEquals("THREE", Die.Face.THREE.toString());
        Assertions.assertEquals("FOUR", Die.Face.FOUR.toString());
        Assertions.assertEquals("FIVE", Die.Face.FIVE.toString());
        Assertions.assertEquals("WORM", Die.Face.WORM.toString());
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
        Dice dice = Dice.generateDice();
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
        Dice dice = Dice.generateDice();
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
        Dice dice = Dice.generateDice();
        dice.eraseDice();
        int numOfDice = dice.getNumOfDice();
        boolean test = numOfDice == 0;
        Assertions.assertTrue(test);
    }

    @Test
    void resetDice(){
        Dice dice = Dice.generateDice();
        Assertions.assertEquals(8, dice.getNumOfDice());
        dice.eraseDice();
        Assertions.assertEquals(0, dice.getNumOfDice());
        dice.resetDice();
        Assertions.assertEquals(8, dice.getNumOfDice());

    }

    @Test
    void removeDie(){
        Dice dice = Dice.generateDice();
        Assertions.assertEquals(8, dice.getNumOfDice());
        dice.removeDie();
        Assertions.assertEquals(7, dice.getNumOfDice());
        dice.removeDie();
        Assertions.assertEquals(6, dice.getNumOfDice());
    }

    @Test
    void removeDice(){
        int n = 7;
        Dice dice = Dice.generateDice();
        Assertions.assertEquals(8, dice.getNumOfDice());
        dice.removeDice(n);
        Assertions.assertEquals(8-n, dice.getNumOfDice());
    }

    @Test
    void rollSequence(){
        int [] seq = {8, 7, 6, 5, 4, 3, 2, 1};
        Dice dice = Dice.generateDice();
        for(int n : seq){
            dice.rollDice();
            List <Die> diceList =  dice.getDiceList();
            Assertions.assertEquals(n, diceList.size());
            for (Die d : diceList){
                Die.Face result = d.getDieFace();
                boolean test = Stream.of(Die.Face.values()).anyMatch(e -> e.equals(result));
                Assertions.assertTrue(test);
            }
            dice.removeDie();
        }
    }
    @Test
    void addSpecificDie(){
        Dice dice = Dice.generateDice();
        dice.eraseDice();
        dice.addSpecificDie(ONE);
        List <Die> diceList =  dice.getDiceList();
        Assertions.assertTrue(diceList.stream().anyMatch(e -> e.getDieFace().equals(ONE)));
    }

    @Test
    void isFacePresent(){
        Dice dice = Dice.generateDice();
        dice.rollDice();
        dice.addSpecificDie(TWO);
        Assertions.assertTrue(dice.isFacePresent(TWO));
    }

    @Test
    void isWormPresent(){
        Dice dice = Dice.generateDice();
        dice.rollDice();
        dice.addSpecificDie(WORM);
        Assertions.assertTrue(dice.isWormPresent());
    }

    @Test
    void chooseDie(){
        Dice dice = Dice.generateDice();
        dice.eraseDice();
        dice.addSpecificDie(TWO);
        dice.chooseDice(TWO);
        List <Die> chosenDice = dice.getChosenDice();
        Die.Face f = TWO;
        Assertions.assertTrue(chosenDice.stream().anyMatch(e -> e.getDieFace().equals(TWO)));
    }

    @Test
    void chooseRandomDice(){
        Dice dice = Dice.generateDice();
        dice.rollDice();
        Die.Face face = dice.getDiceList().get(0).getDieFace();
        dice.chooseRandomDice();
        List <Die> chosenDice = dice.getChosenDice();
        Assertions.assertEquals(face, chosenDice.get(0).getDieFace());
    }

    @Test
    void isDieChosen(){
        Dice dice = Dice.generateDice();
        dice.eraseDice();
        dice.addSpecificDie(THREE);
        dice.chooseDice(THREE);
        Assertions.assertTrue(dice.isDieChosen(THREE));
    }

    @Test
    void isWormChosen(){
        Dice dice = Dice.generateDice();
        dice.rollDice();
        dice.addSpecificDie(WORM);
        dice.chooseDice(WORM);
        Assertions.assertTrue(dice.isWormChosen());
    }

    @Test
    void getChosenDice(){
        Dice dice = Dice.generateDice();
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
    @Test
    void getDieValue(){
        Die die = Die.generateDie();
        Assertions.assertEquals(1, die.getValue(ONE));
        Assertions.assertEquals(2, die.getValue(TWO));
        Assertions.assertEquals(3, die.getValue(THREE));
        Assertions.assertEquals(4, die.getValue(FOUR));
        Assertions.assertEquals(5, die.getValue(FIVE));
        Assertions.assertEquals(5, die.getValue(WORM));
    }

    @Test
    void getScore(){
        Dice dice = Dice.generateDice();
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
        Dice dice = Dice.generateDice();
        dice.rollDice();
        Assertions.assertEquals(8, dice.getDiceList().size());
        Die.Face face1 = dice.getDiceList().get(0).getDieFace();
        dice.chooseRandomDice();
        Assertions.assertTrue(dice.isDieChosen(face1));
        Assertions.assertEquals(8 - dice.getChosenDice().size(), dice.getDiceList().size() );
        while(dice.getDiceList().size() != 0){
            dice.rollDice();
            List <Die> availableDice = dice.getDiceList();
            Die.Face face = dice.getDiceList().get(0).getDieFace();
            dice.chooseRandomDice();
            Assertions.assertTrue(dice.isDieChosen(face));
            Assertions.assertEquals(8 - dice.getChosenDice().size(), dice.getDiceList().size() );
        }
    }





/*

    @Test
    void choseValueOne(){
        List <Die.face> diceList = Dice.rollDice();
        Die.face face = Die.face.ONE;
        Dice.choseValue(face);
        //List <Die.face> choseDice = Dice.getChosenDice();



    }



    @Test
    void checkDiceFairness(){
        return;
    }
*/


}
