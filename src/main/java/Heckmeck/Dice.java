package Heckmeck;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static Heckmeck.Die.Face.WORM;

public class Dice {
    private final int  initialNumOfDice = 8;
    private List<Die> diceList = new ArrayList<Die>();
    private List<Die> chosenDiceList = new ArrayList<Die>(initialNumOfDice);


    private Dice(){
        for (int i = 0; i< initialNumOfDice; i++){
            Die die = Die.generateDie();
            diceList.add(die);
        }
    }

    public static Dice generateDice(){
        return new Dice();
    }

    public List <Die> getList(){
        return diceList;
    }

    public void rollDice(){
        for (Die d : diceList){
            d.rollDie();
        }
    }
/*    public void rollDice(int n){
        numOfDice = diceList.size();
        for (Die d : diceList){
            d.rollDie();
        }
    }*/

    public List <Die> getDiceList(){
        return diceList;
    }

    public void eraseDice(){
        diceList = new ArrayList<Die>();
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

    public void removeDie(){
        int index = diceList.size() - 1;
        diceList.remove(index);
    }
    public void removeDice(int n){
        for(int i = 0; i<n; i++){
            removeDie();
        }
    }

    public void addSpecificDie(Die.Face face){
        Die die = Die.generateDie();
        die.getSpecificDie(face);
        diceList.add(die);
    }

    public boolean isFacePresent(Die.Face face){
        return diceList.stream().anyMatch(e -> e.getDieFace().equals(face));
    }

    public boolean isWormPresent(){
        return isFacePresent(WORM);
    }


    public void chooseDice(Die.Face face) {
        if (!isDieChosen(face)){
            chosenDiceList.addAll(diceList.stream().filter(e -> e.getDieFace().equals(face)).collect(Collectors.toList()));
            diceList.removeIf(e -> e.getDieFace() == face);
        }

    }

    public  void chooseRandomDice(){
        Die.Face face = diceList.get(0).getDieFace();
        chooseDice(face);
    }
    public List <Die> getChosenDice(){
        return chosenDiceList;
    }

    public String getChosenDiceString(){
        return chosenDiceList.stream().map(e -> e.getDieFace().toString()).collect(Collectors.toList()).toString();

    }

    public List <Die> getChosenFaces() {
        return chosenDiceList.stream().distinct().collect(Collectors.toList());
    }

    public boolean isDieChosen(Die.Face face) {
        return chosenDiceList.stream().anyMatch(e -> e.getDieFace().equals(face));
    }

    public boolean isWormChosen() {
        return isDieChosen(WORM);
    }

    public int getScore() {
        return chosenDiceList.stream().map(e->e.getValue(e.getDieFace())).reduce(0, Integer::sum);
    }

}
