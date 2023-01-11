package Heckmeck;

public interface IOHandler {

    void printMessage(String message);
    void showTurnBeginConfirm(String playerName);
    void showWelcomeMessage();

    int chooseNumberOfPlayers();
/*        output.printMessage("Choose number of players between 2 and 7:");
        while(true){
            try{
                int numberOfPlayer = input.chooseNumberOfPlayers();
                if(Rules.validNumberOfPlayer(numberOfPlayer)) return numberOfPlayer;
                else throw new IllegalInput("Invalid number of player, please select a number between 2 and 7");
            } catch (IllegalInput e) {
                output.printMessage(e.getMessage());
            }
        }
    }*/
    String choosePlayerName(int playerNumber);
/*        output.printMessage("Insert the name for player" + playerNumber);
        while(true) {
            try {
                String playerName = input.choosePlayerName();
                if (playerName.isBlank()) throw new IllegalInput("Blank name, choose a valid a one");
                else return playerName;
            } catch (IllegalInput e) {
                output.printMessage(e.getMessage());
            }
        }
    }*/
    void showBoardTiles(BoardTiles boardTiles);

    boolean wantToPick(int diceScore);
/*        output.printMessage("Actual score: " + diceScore);
        output.showWantToPick();
        while(true) {
            try {
                return input.wantToPick();
            } catch (IllegalInput e) {
                output.printMessage(e.getMessage());
            }
        }
    }*/
    boolean wantToSteal(Player robbedPlayer);
   /*     output.showWantToSteal(robbedPlayer);
        while(true) {
            try {
                return input.wantToSteal();
            } catch (IllegalInput e) {
                output.printMessage(e.getMessage());
            }
        }
    }*/

    void showPlayerData(Player player, Dice dice, Player[] players);

    //void showDice(Dice dice);

    Die.Face chooseDie(Dice dice);
//        output.printMessage("Pick one unselected face");
//        while (true) {
//            try {
//                return input.chooseDiceFace();
//            } catch (IllegalInput e) {
//                output.printMessage(e.getMessage());
//            }
//        }
//    }
    void showBustMessage();

    String getInputString();

    String printError(String text);
}
