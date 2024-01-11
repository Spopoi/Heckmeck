package Heckmeck;

import Heckmeck.Components.Player;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Rules {
    public static final int MIN_NUM_OF_PLAYERS=2;
    public static final int MAX_NUM_OF_PLAYERS=7;

    public static boolean validNumberOfPlayer(int numberOfPlayer){
        return (numberOfPlayer >= MIN_NUM_OF_PLAYERS && numberOfPlayer <= MAX_NUM_OF_PLAYERS);
    }

    public static Player whoIsTheWinner(Player[] players) {
        int highestWormScore = Arrays.stream(players).mapToInt(Player::getWormScore).max().orElse(0);
        List<Player> playersWithHighestWormScore = Arrays.stream(players).filter(p -> p.getWormScore() == highestWormScore).collect(Collectors.toList());
        if (moreThanOne(playersWithHighestWormScore)) {
            if (allPlayersHaveTiles(playersWithHighestWormScore)) {
                return playerWithHighestTile(playersWithHighestWormScore);
            } else {
                return null;
            }
        }
        return playersWithHighestWormScore.get(0);
    }

    private static boolean allPlayersHaveTiles(List<Player> playersWithHighestWormScore) {
        return playersWithHighestWormScore.stream()
                .allMatch(Player::hasTile);
    }

    private static boolean moreThanOne(List<Player> playersWithHighestWormScore) {
        return playersWithHighestWormScore.size() > 1;
    }

    private static Player playerWithHighestTile(List<Player> playersWithHighestWormScore) {
        return playersWithHighestWormScore.stream()
                .max(Comparator.comparingInt(Player::getHighestTileNumber))
                .orElse(null);
    }

}
