package exception;

public class IllegalTileNumber extends HeckmeckException {

    public IllegalTileNumber(int number) {
        super("Tile numbers must be between 21 and 36 included. "
                + number
                + " has been given");
    }

}
