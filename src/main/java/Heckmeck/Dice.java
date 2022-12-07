package Heckmeck;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static Heckmeck.Die.Face.WORM;
import static java.util.stream.Collectors.toList;

public class Dice {
    private final int  initialNumOfDice = 8;
    private List<Die> diceList = new ArrayList<Die>();
    private List<Die> chosenDiceList;


    private Dice(){
        resetChosenDice();
        for (int i = 0; i< initialNumOfDice; i++){
            Die die = Die.generateDie();
            diceList.add(die);
        }
    }

    public static Dice generateDice(){
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

    public List<Die.Face> getFaceList(){ //TODO capire se serve
        return diceList.stream().map(Die::getDieFace).toList();
    }

    public void eraseDice(){
        diceList = new ArrayList<Die>();
        chosenDiceList = new ArrayList<Die>();
    }

    public void resetDice(){
        eraseDice();
        resetChosenDice();
        for (int i = 0; i< initialNumOfDice; i++){
            Die die = Die.generateDie();
            diceList.add(die);
        }
    }

    private void resetChosenDice(){
        chosenDiceList = new ArrayList<Die>(initialNumOfDice);
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


    //TODO: checkinput
    public void chooseDice(Die.Face face) {
        if (!isFaceChosen(face)) {
            chosenDiceList.addAll(diceList.stream().filter(e -> e.getDieFace().equals(face)).toList());
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
        return chosenDiceList.stream().map(e -> e.getDieFace().toString()).toList().toString();

    }

    public List<Die.Face> getChosenFaces() {
        return diceList.stream().map(Die::getDieFace).filter(e -> isFaceChosen(e)).toList();

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
        return chosenDiceList.stream().map(e->e.getValue(e.getDieFace())).reduce(0, Integer::sum);
    }

    public boolean canPickAFace(){
        return getChosableFaces().size() != 0;
    }
}
