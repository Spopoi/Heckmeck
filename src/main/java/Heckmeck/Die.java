package Heckmeck;
import  java.util.*;

public class Die {

    private Face dieFace;
    private static final Random PRNG = new Random();
    private static Map<Face, Integer> faceToIntMap = new HashMap<Face, Integer>();
    private static final Map<Integer, Face> intToFaceMap =
            Collections.unmodifiableMap(new HashMap<Integer, Face>() {{
                put(1,Face.ONE);
                put(2,Face.TWO);
                put(3,Face.THREE);
                put(4,Face.FOUR);
                put(5,Face.FIVE);
                put(6,Face.WORM);
            }});

    private Die(){
        faceToIntMap.put(Face.ONE, 1);
        faceToIntMap.put(Face.TWO, 2);
        faceToIntMap.put(Face.THREE, 3);
        faceToIntMap.put(Face.FOUR, 4);
        faceToIntMap.put(Face.FIVE, 5);
        faceToIntMap.put(Face.WORM, 5);
    }

    public static Face intToFace(int value){
        return intToFaceMap.get(value);
    }
    public static Die generateDie(){
        return new Die();
    }

    public Face getFaceByNumber(int n){
        return Face.values()[n];
    }

    public int getValue(Face f){
        return faceToIntMap.get(f);
    }

    public Face getDieFace(){
        return dieFace;
    }
    public void rollDie(){
        int n = PRNG.nextInt(Face.values().length);
        setDieFace(getFaceByNumber(n));
    }

    public Die getSpecificDie(Face f){
        setDieFace(f);
        return this;
    }

    private void setDieFace(Face f){
        dieFace = f;
    }


    public enum Face {
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
