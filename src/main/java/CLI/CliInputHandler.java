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
        char decision  = scan.nextLine().charAt(0);
        return decision == 'y';
    }

    @Override
    public int chooseNumberOfPlayers() {
        return Integer.parseInt(scan.nextLine())-1;
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
}
