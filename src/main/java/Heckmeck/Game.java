package Heckmeck;

public class Game {

    private final Player[] players;
    private Dice dice;
    private Tiles groundTiles;
    private final OutputHandler output;
    private final InputHandler input;

    public Game(Player[] players, OutputHandler output, InputHandler input){
        this.players = players;
        this.dice = Dice.generateDice();
        this.groundTiles = Tiles.init();
        this.output = output;
        this.input = input;
    }
    public void play(){
        dice.rollDice();
        output.showDice(dice);
    }
}
