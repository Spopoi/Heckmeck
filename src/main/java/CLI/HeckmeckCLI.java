package CLI;

import Utils.CLI.Utils;
import Utils.TCP.ConnectionHandler;

import Heckmeck.Game;

public class HeckmeckCLI {
    public static void main(String[] args) {
        startMenu();
    }

    public static void startMenu(){
        CliIOHandler io = new CliIOHandler(System.in, System.out);
        io.showWelcomeMessage();

        do {
            io.printMessage(Utils.getMenu());
            String choice = io.getInitialChoice();

            switch (choice) {
                case "1":
                    Game game = new Game(io);
                    game.init();
                    game.play();
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
                    System.exit(0);
                    return; // Exit the program
                default:
                    break;
            }
        } while (true);
    }
}
