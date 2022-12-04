package Heckmeck;
import  java.util.*;

import static java.util.Map.entry;

public class Die {

    private Face dieFace;
    private static final Random PRNG = new Random();
    private static Map<Face, Integer> faceToIntMap = new HashMap<Face, Integer>(); // TODO scegliere che mappa usare
    private static final Map<Integer, Face> intToFaceMap =
            Collections.unmodifiableMap(new HashMap<Integer, Face>() {{
                put(1,Face.ONE);
                put(2,Face.TWO);
                put(3,Face.THREE);
                put(4,Face.FOUR);
                put(5,Face.FIVE);
                put(6,Face.WORM);
            }});

    public static final Map<String, Face> stringToFaceMap = Map.ofEntries(
            entry("1", Face.ONE),
            entry("2", Face.TWO),
            entry("3", Face.THREE),
            entry("4", Face.FOUR),
            entry("5", Face.FIVE),
            entry("w", Face.WORM));


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

    public static Die getSpecificDieByFace(Face face){
        Die die = generateDie();
        die.setDieFace(face);
        return die;
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

}
