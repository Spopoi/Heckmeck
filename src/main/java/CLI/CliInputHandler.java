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
        while(!"y".equalsIgnoreCase(decision) && !"n".equalsIgnoreCase(decision)) {
            decision = scan.nextLine();
        }
        return "y".equalsIgnoreCase(decision);
    }

    @Override
    public int chooseNumberOfPlayers() {  // Should use throws??
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
        }
        throw new IllegalInput("Input is not correct, choose between {1, 2, 3, 4, 5, w}: ");
    }

    @Override
    public boolean wantToPick() {
        char decision  = scan.nextLine().charAt(0);
        return decision == 'y';
    }

    @Override
    public void pressAnyKey() throws IOException {
        System.in.read();
    }

    @Override
    public boolean wantToSteal() {
        char decision  = scan.nextLine().charAt(0);
        return decision == 'y';
    }
}
