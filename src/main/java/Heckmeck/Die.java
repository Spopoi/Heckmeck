package Heckmeck;
import  java.util.*;

import static java.util.Map.entry;

public class Die {

    private Face dieFace;
    private final Random PRNG;

    public enum Face {
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        WORM

    }

    // TODO scegliere che mappa usare
    private static final Map<Face,Integer> faceToIntMap = Map.ofEntries(
            entry(Face.ONE, 1),
            entry(Face.TWO, 2),
            entry(Face.THREE,3),
            entry(Face.FOUR, 4),
            entry(Face.FIVE, 5),
            entry(Face.WORM, 5));

    public static final Map<String, Face> stringToFaceMap = Map.ofEntries(
            entry("1", Face.ONE),
            entry("2", Face.TWO),
            entry("3", Face.THREE),
            entry("4", Face.FOUR),
            entry("5", Face.FIVE),
            entry("w", Face.WORM));


    private Die(){
        PRNG = new Random();
    }

    public static Die generateDie(){
        return new Die();
    }

    public Face getFaceByNumber(int n){
        return Face.values()[n];
    }

    // TODO: What if text is not present in the Map (just null or excep?)
    // TODO: Mark private stringToFaceMap?
    public static Face getFaceByString(String text) {
        return stringToFaceMap.get(text);
    }

    public int getValue(Face f){
        return faceToIntMap.get(f);
    }

    public Face getDieFace(){
        return dieFace;
    }
    public void rollDie(){
        int n = PRNG.nextInt(Face.values().length);
        Face.valueOf("ONE");
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
}
