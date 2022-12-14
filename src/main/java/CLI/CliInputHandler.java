package CLI;

import Heckmeck.Die;
import Heckmeck.InputHandler;
import exception.IllegalInput;

import java.io.IOException;
import java.util.Scanner;

public class CliInputHandler implements InputHandler {

    private final Scanner scan;

    public CliInputHandler(){
        scan = new Scanner(System.in);
    }

    @Override
    public boolean wantToPlay() {
        String decision  = getInputString();
        if(isYesOrNoChar(decision)) throw new IllegalInput("Incorrect decision, please select 'y' for playing or 'n' for quitting");
        else return "y".equalsIgnoreCase(decision);
    }

    @Override
    public int chooseNumberOfPlayers(){
        try {
            return getInputNumber();
        } catch (NumberFormatException e){
            throw new IllegalInput("Invalid input, please choose a number between 1 and 7");
        }
    }

    @Override
    public String choosePlayerName() {
        return getInputString();
    }

    @Override
    public Die.Face chooseDiceFace(){
        String chosenDice = getInputString();
        if (Die.stringToFaceMap.containsKey(chosenDice)) {
            return Die.stringToFaceMap.get(chosenDice);
        } else throw new IllegalInput("Input is not correct, choose between {1, 2, 3, 4, 5, w}: ");
    }

    @Override
    public boolean wantToPick() {
        String decision  = getInputString();
        if(isYesOrNoChar(decision)) throw new IllegalInput("Incorrect decision, please select 'y' for picking or 'n' for rolling your remaining dice");
        else return "y".equalsIgnoreCase(decision);
    }

    @Override
    public void pressEnter(){
        getInputString();
    }

    @Override
    public boolean wantToSteal() {
        String decision  = getInputString();
        if(isYesOrNoChar(decision)) throw new IllegalInput("Incorrect decision, please select 'y' for stealing or 'n' for continuing your turn");
        else return "y".equalsIgnoreCase(decision);
    }

    private boolean isYesOrNoChar(String decision){
        return(!"y".equalsIgnoreCase(decision) && !"n".equalsIgnoreCase(decision));
    }

    private String getInputString(){
        return scan.nextLine();
    }

    private int getInputNumber(){
        return Integer.parseInt(getInputString());
    }
}
