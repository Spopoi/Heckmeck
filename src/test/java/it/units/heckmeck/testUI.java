//package it.units.heckmeck;
//
//import Heckmeck.*;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import java.util.stream.*;
//import java.util.*;
//
//import CLI.CliHandler;
//
//import static CLI.CliHandler.*;
//import static Heckmeck.Die.face.*;
//
//public class testUI {
//
//    @Test
//    void printDie(){
//        Die die = Die.generateDie().getSpecificDie(FIVE);
//        String textToPrint = CliHandler.drawSingleDie(die);
//        String expected = "┌---------┐ ";
//        expected += "\n";
//        expected += "┊  ◎   ◎  ┊ ";
//        expected += "\n";
//        expected += "┊    ◎    ┊ ";
//        expected += "\n";
//        expected += "┊  ◎   ◎  ┊ ";
//        expected += "\n";
//        expected += "└---------┘ ";
//        expected += "\n";
//        System.out.print(textToPrint);
//        Assertions.assertEquals(expected, textToPrint);
//    }
//
//
//
//  @Test
//  void printInitialPlayerData(){
//      List <Tile> tileList = Tiles.init().getTilesList();
//      Dice dice = Dice.generateDice();
//      dice.eraseDice();
//      dice.addSpecificDie(ONE);
//      dice.addSpecificDie(TWO);
//      dice.addSpecificDie(WORM);
//      Player player = Player.generatePlayer("Pippo");
//      dice.chooseDice(ONE);
//      dice.chooseDice(TWO);
//      dice.chooseDice(WORM);
//      String playerData = drawPlayerData(player, dice);
//      String topRow =     "        Pippo's tiles:  ┌──────┓     Chosen dice: [ONE, TWO, WORM]";
//      String firstRow =   "                        │  no  │     Current dice score: 8";
//      String secondRow =  "                        │ tile │     WORM is chosen: true";
//      String thirdRow =   "                        │      │";
//      String bottomRow =  "                        └──────┘";
//      String expected = topRow + "\n" + firstRow +"\n" + secondRow + "\n" + thirdRow + "\n" + bottomRow + "\n";
//      Assertions.assertEquals(expected, playerData);
//
//  }
//
//    @Test
//    void printTiles(){
//        List <Tile> tileList = Tiles.init().getTilesList();
//        String tileString = CliHandler.drawTiles(tileList);
//        String topRow =     "┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓";
//        String firstRow =   "│  21  ││  22  ││  23  ││  24  ││  25  ││  26  ││  27  ││  28  ││  29  ││  30  ││  31  ││  32  ││  33  ││  34  ││  35  ││  36  │";
//        String secondRow =  "│  ~   ││  ~   ││  ~   ││  ~   ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  │";
//        String thirdRow =   "│      ││      ││      ││      ││      ││      ││      ││      ││  ~   ││  ~   ││  ~   ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  │";
//        String bottomRow =  "└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘";
//        String expected = topRow + "\n" + firstRow +"\n" + secondRow + "\n" + thirdRow + "\n" + bottomRow + "\n";
//        Assertions.assertEquals(expected, tileString);
//
//
//    }
//
//    @Test
//    void printPlayerDataWithRemainingTiles(){
//        Tiles board = Tiles.init();
//        List <Tile> tileList = board.getTilesList();
//        Player player = Player.generatePlayer("Pippo");
//        player.pickTileFromBoard(tileList.get(5), board);
//        Dice dice = Dice.generateDice();
//        dice.eraseDice();
//        dice.addSpecificDie(ONE);
//        dice.addSpecificDie(TWO);
//        dice.addSpecificDie(WORM);
//        dice.chooseDice(ONE);
//        dice.chooseDice(TWO);
//        dice.chooseDice(WORM);
//        tileList = board.getTilesList();
//        String data = drawTiles(tileList) + drawPlayerData(player, dice);;
//
//        String topRow =             "┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓┌──────┓";
//        String firstRow =           "│  21  ││  22  ││  23  ││  24  ││  25  ││  27  ││  28  ││  29  ││  30  ││  31  ││  32  ││  33  ││  34  ││  35  ││  36  │";
//        String secondRow =          "│  ~   ││  ~   ││  ~   ││  ~   ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  │";
//        String thirdRow =           "│      ││      ││      ││      ││      ││      ││      ││  ~   ││  ~   ││  ~   ││  ~~  ││  ~~  ││  ~~  ││  ~~  ││  ~~  │";
//        String bottomRow =          "└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘└──────┘";
//        String playerTopRow =       "        Pippo's tiles:  ┌──────┓     Chosen dice: [ONE, TWO, WORM]";
//        String playerFirstRow =     "                        │  26  │     Current dice score: 8";
//        String playerSecondRow =    "                        │  ~~  │     WORM is chosen: true";
//        String playerThirdRow =     "                        │      │";
//        String playerBottomRow =    "                        └──────┘";
//        String expected = topRow + "\n" + firstRow +"\n" + secondRow + "\n" + thirdRow + "\n" + bottomRow + "\n"
//                + playerTopRow + "\n" + playerFirstRow + "\n" + playerSecondRow + "\n"+ playerThirdRow + "\n"
//                + playerBottomRow + "\n";
//        Assertions.assertEquals(expected, data);
//
//
//
//    }
//
//
//    @Test
//    void printTopRow(){
//        Dice dice = Dice.generateDice();
//        dice.addSpecificDie(ONE);
//        dice.addSpecificDie(TWO);
//        dice.addSpecificDie(THREE);
//        List <Die> diceList = dice.getDiceList();
//        String topRow = CliHandler.getDieTopRow();
//        String expected = "┌---------┐ ";
//        Assertions.assertEquals(expected, topRow);
//    }
//
//    @Test
//    void printDiceFromList(){
//        Dice dice = Dice.generateDice();
//        dice.eraseDice();
//        dice.addSpecificDie(ONE);
//        dice.addSpecificDie(TWO);
//        dice.addSpecificDie(THREE);
//        dice.addSpecificDie(FOUR);
//        dice.addSpecificDie(FIVE);
//        dice.addSpecificDie(WORM);
//        List <Die> diceList = dice.getDiceList();
//        String textToPrint = CliHandler.drawDice(diceList);
//        String topRow =     "┌---------┐ ┌---------┐ ┌---------┐ ┌---------┐ ┌---------┐ ┌---------┐ ";
//        String firstRow =   "┊         ┊ ┊      ◎  ┊ ┊      ◎  ┊ ┊  ◎   ◎  ┊ ┊  ◎   ◎  ┊ ┊   \\=\\   ┊ ";
//        String secondRow =  "┊    ◎    ┊ ┊         ┊ ┊    ◎    ┊ ┊         ┊ ┊    ◎    ┊ ┊   /=/   ┊ ";
//        String thirdRow =   "┊         ┊ ┊  ◎      ┊ ┊  ◎      ┊ ┊  ◎   ◎  ┊ ┊  ◎   ◎  ┊ ┊   \\=\\   ┊ ";
//        String bottomRow =  "└---------┘ └---------┘ └---------┘ └---------┘ └---------┘ └---------┘ ";
//        String expected = topRow + "\n" + firstRow + "\n" + secondRow + "\n" + thirdRow + "\n" + bottomRow + "\n";
//        Assertions.assertEquals(expected, textToPrint);
//    }
//
//
//
//}
