package exception;

public class IllegalTileAddition extends HeckmeckException {

    public IllegalTileAddition(int number) {
        super("Tile number "
                + number
                + " is already present in the collection");
    }

}
