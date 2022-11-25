package Heckmeck;

public class Game {

    private final Player[] players;
    private Dice dice;
    private Tiles groundTiles;
    private final GameGraphics graphics;

    public Game(Player[] players, GameGraphics graphics){
        this.players = players;
        this.dice = Dice.generateDice();
        this.groundTiles = Tiles.init();
        this.graphics = graphics;
    }

}
