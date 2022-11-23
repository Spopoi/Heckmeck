package it.units.heckmeck;

import Heckmeck.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.stream.*;
import java.util.*;

import CLI.CliHandler;

import static CLI.CliHandler.*;
import static Heckmeck.Die.face.*;

public class testUI {

    @Test
    void printDie(){
        Die die = Die.generateDie().getSpecificDie(FIVE);
        String textToPrint = CliHandler.drawSingleDie(die);
        String expected = "┌---------┐ ";
        expected += "\n";
        expected += "┊  ◎   ◎  ┊ ";
        expected += "\n";
        expected += "┊    ◎    ┊ ";
        expected += "\n";
        expected += "┊  ◎   ◎  ┊ ";
        expected += "\n";
        expected += "└---------┘ ";
        expected += "\n";
        System.out.print(textToPrint);
        Assertions.assertEquals(expected, textToPrint);
    }

  /*  @Test
    void printPlayerTile(){
        Tiles board = Tiles.init();
        List <Tile> tileList = board.getTilesList();
        Tile tile = tileList.get(5);

        Player player = Player.generatePlayer("Pippo");
        player.pickTileFromBoard(tile, board);
        String tileString = drawPlayerTile(player);

        String topRow = "        Pippo's tiles  ┌─────┓";
        String firstRow = "                       │   26   │";
        String secondRow = "                       │   ~~   │";
        String thirdRow = "                       │     │";
        String bottomRow = "                       └─────┘";
        String expected = topRow + "\n" + firstRow +"\n" + secondRow + "\n" + thirdRow + "\n" + bottomRow + "\n";
        Assertions.assertEquals(expected, tileString);

    }

   */

    @Test
    void printTiles(){
        List <Tile> tileList = Tiles.init().getTilesList();

        String tileString = CliHandler.drawTiles(tileList);
        String topRow =     "┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓";
        String firstRow =   "│  21  ││  22  ││  23  ││  24  ││  25  ││  26  ││  27  ││  28  ││  29  ││  30  ││  31  ││  32  ││  33  ││  34  ││  35  ││  36  │";
        String secondRow =  "│  ~   ││  ~   ││  ~   ││  ~   ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  │";
        String thirdRow =   "│      ││      ││      ││      ││      ││      ││      ││      ││  ~   ││  ~   ││  ~   ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  │";
        String bottomRow =  "└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘";
        String expected = topRow + "\n" + firstRow +"\n" + secondRow + "\n" + thirdRow + "\n" + bottomRow + "\n";
        Assertions.assertEquals(expected, tileString);

    }


    @Test
    void printTopRow(){
        Dice dice = Dice.generateDice();
        dice.addSpecificDie(ONE);
        dice.addSpecificDie(TWO);
        dice.addSpecificDie(THREE);
        List <Die> diceList = dice.getDiceList();
        String topRow = CliHandler.getDieTopRow();
        String expected = "┌---------┐ ";
        Assertions.assertEquals(expected, topRow);
    }

    @Test
    void printDiceFromList(){
        Dice dice = Dice.generateDice();
        dice.eraseDice();
        dice.addSpecificDie(ONE);
        dice.addSpecificDie(TWO);
        dice.addSpecificDie(THREE);
        dice.addSpecificDie(FOUR);
        dice.addSpecificDie(FIVE);
        dice.addSpecificDie(WORM);
        List <Die> diceList = dice.getDiceList();
        String textToPrint = CliHandler.drawDice(diceList);
        String topRow =     "┌---------┐ ┌---------┐ ┌---------┐ ┌---------┐ ┌---------┐ ┌---------┐ ";
        String firstRow =   "┊         ┊ ┊      ◎  ┊ ┊      ◎  ┊ ┊  ◎   ◎  ┊ ┊  ◎   ◎  ┊ ┊   \\=\\   ┊ ";
        String secondRow =  "┊    ◎    ┊ ┊         ┊ ┊    ◎    ┊ ┊         ┊ ┊    ◎    ┊ ┊   /=/   ┊ ";
        String thirdRow =   "┊         ┊ ┊  ◎      ┊ ┊  ◎      ┊ ┊  ◎   ◎  ┊ ┊  ◎   ◎  ┊ ┊   \\=\\   ┊ ";
        String bottomRow =  "└---------┘ └---------┘ └---------┘ └---------┘ └---------┘ └---------┘ ";
        String expected = topRow + "\n" + firstRow + "\n" + secondRow + "\n" + thirdRow + "\n" + bottomRow + "\n";
        Assertions.assertEquals(expected, textToPrint);
    }



}
