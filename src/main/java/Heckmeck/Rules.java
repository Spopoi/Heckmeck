package Heckmeck;

import Heckmeck.Components.Player;
import Heckmeck.Components.Tile;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Rules {

    public static boolean validNumberOfPlayer(int numberOfPlayer){
        return (numberOfPlayer >= 2 && numberOfPlayer <= 7);
    }

    public static Player whoIsTheWinner(Player[] players){
        List<Player> winners = Arrays.stream(players).sorted(Comparator.comparingInt(Rules::computeScore)).toList();
        int highestWormScore = computeScore(winners.get(winners.size()-1));
        winners = winners.stream().filter(e -> computeScore(e) >= highestWormScore).collect(Collectors.toList());
        if(winners.size() == 1) return winners.get(0);
        else {
            winners.sort(Comparator.comparingInt(Rules::numberOfPlayerTile));
            int lowerNumberOfTiles = numberOfPlayerTile(winners.get(0));
            winners = winners.stream().filter(p -> numberOfPlayerTile(p) <= lowerNumberOfTiles).collect(Collectors.toList());
            if(winners.size() == 1) return winners.get(0);
            else{
                winners.sort(Comparator.comparingInt(Rules::getHighestTileNumber));
                return winners.get(winners.size()-1);
            }
        }
    }

    private static int numberOfPlayerTile(Player player){
        return player.getPlayerTiles().size();
    }

    private static int computeScore(Player player) {
        return player.getPlayerTiles().stream()
                .mapToInt(Tile::getWorms)
                .sum();
    }

    private static int getHighestTileNumber(Player player){ // TODO gestire altezza 0
        List<Tile> sortedList = player.getPlayerTiles().stream().sorted().toList();
        return sortedList.get(sortedList.size()-1).number();
    }
}
