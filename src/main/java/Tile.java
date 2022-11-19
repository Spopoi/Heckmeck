import java.util.Objects;

public class Tile {

    private final int number;
    private final int worms;
    public Tile(int number, int worms) {
        this.number=number;
        this.worms=worms;
    }

    public int getNumber(){
        return number;
    }
    public int getWorms(){
        return worms;
    }

    public static Tile generateTile(int number, int worms){
        return new Tile(number,worms);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return number == tile.number && worms == tile.worms;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, worms);
    }
}
