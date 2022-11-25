package CLI;

import Heckmeck.InputHandler;

import java.util.Scanner;

public class CliInputHandler implements InputHandler {

    private Scanner scan;

    public CliInputHandler(){
        scan = new Scanner(System.in);
    }
    @Override
    public int chooseNumberOfPlayers() {
        return Integer.parseInt(scan.nextLine());
    }

    @Override
    public String choosePlayerName() {
        return scan.nextLine();
    }
}
