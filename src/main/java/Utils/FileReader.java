package Utils;

import Heckmeck.Components.Die;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FileReader {
    private static final Map<Die.Face, String> faceToIconPath =
            Collections.unmodifiableMap(new HashMap<>() {{
                put(Die.Face.ONE, "src/main/resources/GUI/Dice/one.png");
                put(Die.Face.TWO, "src/main/resources/GUI/Dice/two.png");
                put(Die.Face.THREE, "src/main/resources/GUI/Dice/three.png");
                put(Die.Face.FOUR, "src/main/resources/GUI/Dice/four.png");
                put(Die.Face.FIVE, "src/main/resources/GUI/Dice/five.png");
                put(Die.Face.WORM, "src/main/resources/GUI/Dice/worm.png");
            }});
    private static final Map<Integer, String> tileNumberToIconPath =
            Collections.unmodifiableMap(new HashMap<>() {{
                put(21, "src/main/resources/GUI/Tiles/Tile_21.png");
                put(22, "src/main/resources/GUI/Tiles/Tile_22.png");
                put(23, "src/main/resources/GUI/Tiles/Tile_23.png");
                put(24, "src/main/resources/GUI/Tiles/Tile_24.png");
                put(25, "src/main/resources/GUI/Tiles/Tile_25.png");
                put(26, "src/main/resources/GUI/Tiles/Tile_26.png");
                put(27, "src/main/resources/GUI/Tiles/Tile_27.png");
                put(28, "src/main/resources/GUI/Tiles/Tile_28.png");
                put(29, "src/main/resources/GUI/Tiles/Tile_29.png");
                put(30, "src/main/resources/GUI/Tiles/Tile_30.png");
                put(31, "src/main/resources/GUI/Tiles/Tile_31.png");
                put(32, "src/main/resources/GUI/Tiles/Tile_32.png");
                put(33, "src/main/resources/GUI/Tiles/Tile_33.png");
                put(34, "src/main/resources/GUI/Tiles/Tile_34.png");
                put(35, "src/main/resources/GUI/Tiles/Tile_35.png");
                put(36, "src/main/resources/GUI/Tiles/Tile_36.png");

            }});

    public static final String newLine = System.lineSeparator();

    public static Map<Integer, String> readTilesFromSingleJson(Path filePath) {
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new java.io.FileReader(filePath.toString()));
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }
        TypeToken<Map<Integer, String>> returnType = new TypeToken<>() {};
        return gson.fromJson(reader, returnType);
    }

    public static String readTextFile(Path filePath) {
        StringBuilder res = new StringBuilder();
        try (InputStream in = Files.newInputStream(filePath);
             BufferedReader reader =
                     new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                res.append(line).append(newLine);  // Notice that will add a newline at the end
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return res.toString();
    }

    public static Map<Die.Face, String> readDieFacesFromSingleJson(Path filePath) {
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new java.io.FileReader(filePath.toString()));
        } catch (FileNotFoundException ex) {
            System.err.println(ex);
        }
        TypeToken<Map<Die.Face, String>> returnType = new TypeToken<>() {};
        return gson.fromJson(reader, returnType);
    }
    public static ImageIcon getTileIcon(int tileNumber, int height, int width){
        ImageIcon icon = new ImageIcon(tileNumberToIconPath.get(tileNumber));
        Image img = icon.getImage() ;
        Image newimg = img.getScaledInstance(width , height,  java.awt.Image.SCALE_SMOOTH ) ;
        return new ImageIcon( newimg );
    }
    public static ImageIcon getDieIcon(Die.Face face, int size){
        ImageIcon icon = new ImageIcon(faceToIconPath.get(face));
        Image img = icon.getImage() ;
        Image newimg = img.getScaledInstance(size , size,  java.awt.Image.SCALE_SMOOTH ) ;
        return new ImageIcon( newimg );
    }

}
