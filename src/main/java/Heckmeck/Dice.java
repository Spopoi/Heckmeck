
package Heckmeck;
import java.util.ArrayList;
import java.util.List;

import static Heckmeck.Die.Face.WORM;


public class Dice {
    private final int  initialNumOfDice = 8;
    private List<Die> diceList;
    private List<Die> chosenDiceList;


    private Dice(){
        resetDice();
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

    public void eraseDice(){
        diceList = new ArrayList<>(initialNumOfDice);
        chosenDiceList = new ArrayList<>();
    }

    public void resetDice(){
        eraseDice();
        for (int i = 0; i< initialNumOfDice; i++){
            Die die = Die.generateDie();
            diceList.add(die);
        }
    }
    public int getNumOfDice(){
        return diceList.size();
    }


    public void addSpecificDie(Die.Face face){
        diceList.add(Die.generateDie(face));
    }

    public boolean isFacePresent(Die.Face face){
        return diceList.stream().anyMatch(e -> e.getDieFace().equals(face));
    }

    public boolean isWormPresent(){
        return isFacePresent(WORM);
    }

    public void chooseDice(Die.Face face) {
        chosenDiceList.addAll(diceList.stream().filter(e -> e.getDieFace().equals(face)).toList());
        diceList.removeIf(e -> e.getDieFace() == face);

    }

    public  void chooseRandomDice(){
        Die.Face face = diceList.get(0).getDieFace();
        chooseDice(face);
    }
    public List <Die> getChosenDice(){
        return chosenDiceList;
    }

    public String getChosenDiceString(){
        return chosenDiceList.stream().map(e -> e.getDieFace().toString()).toList().toString();

    }

    private List <Die> getChosableFaces(){
        return diceList.stream().filter(e -> !isFaceChosen(e.getDieFace())).toList();
    }

    //renaming or changing signature
    public boolean isFaceChosen(Die.Face face) {
        return chosenDiceList.stream().anyMatch(e -> e.getDieFace().equals(face));
    }

    public boolean isWormChosen() {
        return isFaceChosen(WORM);
    }

    public int getScore() {
        return chosenDiceList.stream().mapToInt(Die::getDieScore).sum();
    }

    public boolean canPickAFace(){
        return getChosableFaces().size() != 0;
    }
}