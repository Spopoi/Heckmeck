package CLI;

import Heckmeck.Launcher;
import Utils.CLI.Utils;
import Utils.TCP.ConnectionHandler;

import Heckmeck.Game;

import java.io.IOException;

public class HeckmeckCLI extends Launcher {
    public static void main(String[] args) {
        try {
            startMenu();
        } catch (IOException ex) {
            System.out.println("Error loading the file containing the messages of the game");
        }
    }

    public static void startMenu() throws IOException {
        CliIOHandler io = new CliIOHandler(System.in, System.out);
        io.showWelcomeMessage();
        do {
            io.printMessage(Utils.getMenu());
            String choice = io.getInitialChoice();

            switch (choice) {
                case "1":
                    startGame(io);
                    break;
                case "2":
                    if (io.wantToHost()) {
                        int numOfPlayers = io.chooseNumberOfPlayers();
                        ConnectionHandler.startGameServer(numOfPlayers);
                        io.printMessage("You are now hosting on this machine: tell your IP address to your friends!");
                        io.printMessage(ConnectionHandler.getLanIpAddress());
                        ConnectionHandler.startLocalClient(io);

                    } else {
                        ConnectionHandler.startLocalClient(io.askIPToConnect(), io);
                    }
                    break;
                case "3":
                    io.printMessage(Utils.getRules());
                    break;
                case "4":
                    io.printMessage("Exiting Heckmeck. Goodbye!");
                    exit();
                    return; // Exit the program
                default:
                    break;
            }
        } while (true);
    }
}
