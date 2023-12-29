package CLI;

import Utils.CLI.Utils;
import Utils.TCP.ConnectionHandler;

import Heckmeck.Game;
import Heckmeck.IOHandler;
import TCP.Client;
import TCP.Server.GameServer;

public class HeckmeckCLI {
    public static void main(String[] args) {
        startMenu();
    }

    public static void startMenu(){
        CliIOHandler io = new CliIOHandler();
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
                        startGameServer(numOfPlayers);
                        startLocalClient(io);

                    } else {
                        startLocalClient(io.askIPToConnect(), io);
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
    //TODO: Move methods?
    public static void startLocalClient(String IP, IOHandler io){
        Client cli = ConnectionHandler.startClient(IP, io);
        Thread cliThread = new Thread(cli);
        cliThread.start();
        io.printMessage("Local client started, waiting for your turn to begin");
        try {
            cliThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static void startLocalClient(IOHandler io){
        startLocalClient("127.0.0.1", io);
    }
    public static void startGameServer(int  numOfPlayers){
        GameServer gameServer = new GameServer(numOfPlayers);
        new Thread(gameServer).start();
    }
}
