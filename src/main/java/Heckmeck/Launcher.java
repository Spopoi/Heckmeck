package Heckmeck;

import Heckmeck.Gateway.GameGateway;
import Heckmeck.Gateway.LocalGameGateway;
import Heckmeck.Components.Player;
import Utils.PropertiesManager;

import java.io.IOException;

public abstract class Launcher {
    private static final PropertiesManager propertiesManager;

    static {
        try {
            propertiesManager = new PropertiesManager(PropertiesManager.getIOHandlerPropertiesPath());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading the file containing the messages of the game");
            throw new RuntimeException(e);
        }
    }

    public Launcher(){
    }

    public static PropertiesManager getPropertiesManager(){
        return propertiesManager;
    }

    /**
     * Starts a game using the new architecture with GameGateway and Controller.
     * This is the recommended approach for new code.
     * 
     * @param io The I/O handler to use
     * @throws IOException if properties cannot be loaded
     */
    public static void startGame(IOHandler io) throws IOException {
        Rules rules = new HeckmeckRules();
        GameEngine engine = new GameEngine(rules);
        
        // Get number of players and names
        int numPlayers = io.chooseNumberOfPlayers();
        Player[] players = new Player[numPlayers];
        
        for (int i = 0; i < numPlayers; i++) {
            players[i] = Player.generatePlayer(i);
            String playerName = io.choosePlayerName(players[i]);
            players[i].setPlayerName(playerName);
        }
        
        // Create gateway and controller
        GameGateway gateway = new LocalGameGateway(engine, players);
        HeckmeckController controller = new HeckmeckController(gateway, io, engine);
        
        // Start the game
        controller.play();
        
        // Clean up
        gateway.close();
    }

    public static void exit(){
        System.exit(0);
    }
}
