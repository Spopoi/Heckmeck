package CLI;

import Heckmeck.Launcher;
import Utils.CLI.Utils;
import Utils.TCP.ConnectionHandler;

import Heckmeck.Game;

import java.io.IOException;

public class HeckmeckCLI extends Launcher {
    public static void main(String[] args) {
        startMenu();
    }

    public static void startMenu() {
        CliIOHandler io = new CliIOHandler(System.in, System.out);
        io.showWelcomeMessage();
        do {
            io.printMessage(Utils.getMenu());
            String choice = io.getInitialChoice();

            switch (choice) {
                case "1":
                    try {
                        startGame(io);
                    } catch (IOException e) {
                        io.printError("Error loading the file containing the messages of the game");
                        exit();
                    }
                    break;
                case "2":
                    if (io.wantToHost()) {
                        int numOfPlayers = io.chooseNumberOfPlayers();
                        ConnectionHandler.startGameServer(numOfPlayers);
                        io.printMessage("You are now hosting on this machine: tell your IP address to your friends!");
                        io.printMessage(ConnectionHandler.getLanIpAddress());
                        io.printMessage("Room opened, waiting for other players");
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
