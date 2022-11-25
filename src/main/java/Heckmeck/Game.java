package Heckmeck;

public class Game {

    private final Player[] players;
    private Dice dice;
    private Tiles groundTiles;
    private final OutputHandler output;
    private final InputHandler input;

    private boolean gameFinished;

    public Game(Player[] players, OutputHandler output, InputHandler input){
        this.players = players;
        this.dice = Dice.generateDice();
        this.groundTiles = Tiles.init();
        this.output = output;
        this.input = input;
        gameFinished = false;
    }
    public void play(){
        int playerNumber = 0;
        Player actualPlayer = players[playerNumber];
        while(!gameFinished){
            playerTurn(actualPlayer);
            if(playerNumber >= players.length) playerNumber = 0;
            actualPlayer = players[playerNumber++];
            output.showPlayerData(actualPlayer,dice);
            if(groundTiles.getTilesList().size()==0) gameFinished = true;
        }
    }

    private void playerTurn(Player actualPlayer){
        dice.rollDice();
        output.showDice(dice);
        System.out.println("Select die");
        dice.chooseDice(Die.intToFace(input.chooseDiceNumber()));
        output.showDice(dice);

        dice.resetDice();
    }
}
