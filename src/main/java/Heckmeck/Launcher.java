package Heckmeck;

public abstract class Launcher {
    public Launcher(){
    }

    public static void startGame(IOHandler io){
        Game game = new Game(io);
        game.init();
        game.play();
    }

    public static void exit(){
        System.exit(0);
    }
}
