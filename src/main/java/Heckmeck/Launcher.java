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
            //TODO: CHECK
            throw new RuntimeException(e);
        }
    }

    public Launcher(){
    }

    public static PropertiesManager getPropertiesManager(){
        return propertiesManager;
    }

    public static void startGame(IOHandler io) throws IOException {
        Game game = new Game(io);
        game.init();
        game.play();
    }

    public static void exit(){
        System.exit(0);
    }
}
