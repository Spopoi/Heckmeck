package Heckmeck;
import  java.util.*;

public class Die {

    private face dieFace;
    private static final Random PRNG = new Random();

    private static Map<face, Integer> values = new HashMap<face, Integer>();


    private Die(){
        values.put(face.ONE, 1);
        values.put(face.TWO, 2);
        values.put(face.THREE, 3);
        values.put(face.FOUR, 4);
        values.put(face.FIVE, 5);
        values.put(face.WORM, 5);
    }

    public static Die generateDie(){
        return new Die();
    }

    public  face getFaceByNumber(int n){
        return face.values()[n];
    }

    public int getValue(face f){
        return values.get(f);
    }


    public face getDieFace(){
        return dieFace;
    }
    public void rollDie(){
        int n = PRNG.nextInt(face.values().length);
        setDieFace(getFaceByNumber(n));
    }

    public Die getSpecificDie(face f){
        setDieFace(f);
        return this;
    }

    private void setDieFace(face f){
        dieFace = f;
    }


    public enum face {
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        WORM

    }
/*
    @Override
    public String toString() {
        return value.toString();
    }*/

}
