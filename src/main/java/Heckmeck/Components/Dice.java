package Heckmeck.Components;
import Heckmeck.Rules;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Dice {
    private final List<Die> diceList;
    private final List<Die> chosenDiceList;
    private int score;


    private Dice(){
        diceList = new ArrayList<>(Rules.INITIAL_NUMBER_OF_DICE);
        chosenDiceList = new ArrayList<>();
        IntStream.range(0, Rules.INITIAL_NUMBER_OF_DICE)
                .forEach(i -> diceList.add(Die.generateDie()));
    }

    public static Dice init(){
        return new Dice();
    }

    public void rollDice(){
        for (Die d : diceList){
            d.rollDie();
        }
    }

    public List <Die> getDiceList(){
        return diceList;
    }


    public void resetDice(){
        diceList.clear();
        chosenDiceList.clear();
        IntStream.range(0, Rules.INITIAL_NUMBER_OF_DICE)
                .forEach(i -> diceList.add(Die.generateDie()));
        score = 0;
    }
    public int getNumOfDice(){
        return diceList.size();
    }

    public void addSpecificDie(Die.Face face){
        diceList.add(Die.generateDie(face));
    }

    public void chooseDice(Die.Face face) {
        chosenDiceList.addAll(diceList.stream().filter(e -> e.getDieFace().equals(face)).toList());
        diceList.removeIf(e -> e.getDieFace().equals(face));
        score = chosenDiceList.stream().mapToInt(Die::getScore).sum();
    }

    public List <Die> getChosenDice(){
        return chosenDiceList;
    }


    public boolean isFaceChosen(Die.Face face) {
        return chosenDiceList.stream().anyMatch(e -> e.getDieFace().equals(face));
    }

    public boolean isFacePresent(Die.Face face){
        return diceList.stream().anyMatch(e -> e.getDieFace().equals(face));
    }

    public int getScore() {
        return score;
    }

    public boolean canPickAFace(){
        List<Die> choosableDie = diceList.stream().filter(e -> !isFaceChosen(e.getDieFace())).toList();
        return !choosableDie.isEmpty();
    }

}
