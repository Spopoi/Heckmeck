package CLI;

import Heckmeck.Die;
import Heckmeck.InputHandler;
import exception.IllegalInput;

import java.io.IOException;
import java.util.Scanner;

public class CliInputHandler implements InputHandler {

    private Scanner scan;

    public CliInputHandler(){
        scan = new Scanner(System.in);
    }

    @Override
    public boolean wantToPlay() {
        String decision  = scan.nextLine();
        if(isYesOrNoChar(decision)) throw new IllegalInput("Incorrect decision, please select 'y' for playing or 'n' for quitting");
        else return "y".equalsIgnoreCase(decision);
    }

    @Override
    public int chooseNumberOfPlayers(){
        return Integer.parseInt(scan.nextLine());
    }

    @Override
    public String choosePlayerName() {
        return scan.nextLine();
    }

    @Override
    public Die.Face chooseDiceNumber() throws IllegalInput {
        String chosenDice = scan.nextLine();
        if (Die.stringToFaceMap.containsKey(chosenDice)) {
            return Die.stringToFaceMap.get(chosenDice);
        } else throw new IllegalInput("Input is not correct, choose between {1, 2, 3, 4, 5, w}: ");
    }

    @Override
    public boolean wantToPick() {
        String decision  = scan.nextLine();
        if(isYesOrNoChar(decision)) throw new IllegalInput("Incorrect decision, please select 'y' for picking or 'n' for rolling your remaining dice");
        else return "y".equalsIgnoreCase(decision);
    }

    @Override
    public void pressAnyKey() throws IOException {
        System.in.read();
    }

    @Override
    public boolean wantToSteal() {
        String decision  = scan.nextLine();
        if(isYesOrNoChar(decision)) throw new IllegalInput("Incorrect decision, please select 'y' for stealing or 'n' for continuing your turn");
        else return "y".equalsIgnoreCase(decision);
    }

    private boolean isYesOrNoChar(String decision){
        return(!"y".equalsIgnoreCase(decision) && !"n".equalsIgnoreCase(decision));
    }
}
