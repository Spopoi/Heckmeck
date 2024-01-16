package Heckmeck;

public abstract class Launcher {
    public void Launcher(){
    }

    public static void startGame(IOHandler io){
        Game game = new Game(io);
        game.init();
        game.play();
    }
}
