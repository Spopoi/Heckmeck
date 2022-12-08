package Heckmeck;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Rules {

    public static boolean validNumberOfPlayer(int numberOfPlayer){
        return (numberOfPlayer >= 2 && numberOfPlayer <= 7);
    }

    public static Player whoIsTheWinner(Player[] players){
        List<Player> winners = Arrays.stream(players).sorted(Comparator.comparingInt(Player::getWormNumber)).toList();
        int highestWormScore = winners.get(winners.size()-1).getWormNumber();
        winners = winners.stream().filter(e -> e.getWormNumber() >= highestWormScore).collect(Collectors.toList());
        if(winners.size() == 1) return winners.get(0);
        else {
            winners.sort(Comparator.comparingInt(Player::getNumberOfPlayerTile));
            int lowerNumberOfTiles = winners.get(0).getNumberOfPlayerTile();
            winners = winners.stream().filter(p -> p.getNumberOfPlayerTile() <= lowerNumberOfTiles).collect(Collectors.toList());
            if(winners.size() == 1) return winners.get(0);
            else{
                winners.sort(Comparator.comparingInt(Player::getHighestTileNumber));
                return winners.get(winners.size()-1);
            }
        }
    }
}
