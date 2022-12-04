package CLI;

import Heckmeck.InputHandler;

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
    public int chooseDiceNumber() {
        return Integer.parseInt(scan.nextLine());
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
