package Heckmeck;

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

    public static void startGame(IOHandler io) throws IOException {
        Rules rules = new HeckmeckRules();
        Game game = new Game(io, rules);
        game.init();
        game.play();
    }

    public static void exit(){
        System.exit(0);
    }
}
