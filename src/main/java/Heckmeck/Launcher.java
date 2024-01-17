package Heckmeck;

import java.io.IOException;

public abstract class Launcher {
    public Launcher(){
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
