package exception;

import Heckmeck.Player;
import Heckmeck.Tile;

public class IllegalTileTheft extends HeckmeckException {

    public IllegalTileTheft(Tile desiredTile, Player player) {
        super("Can not steal tile "
                + desiredTile.getNumber()
                + " from "
                + player.getName());
    }

}
