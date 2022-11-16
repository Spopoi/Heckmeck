public class Tile {

    private final int number;
    private final int worms;
    public Tile(int number, int worms) {
        this.number=number;
        this.worms=worms;
    }

    public int number(){
        return number;
    }
    public int worms(){
        return worms;
    }
}
