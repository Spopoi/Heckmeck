package Utils;

import Heckmeck.Components.Die;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class FileReader {
    public static final String newLine = System.lineSeparator();

    public static Map<Integer, String> readTilesFromSingleJson(Path filePath) {
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new java.io.FileReader(filePath.toString()));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
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
            ex.printStackTrace();
        }
        return res.toString();
    }

    public static Map<Die.Face, String> readDieFacesFromSingleJson(Path filePath) {
        Gson gson = new Gson();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new java.io.FileReader(filePath.toString()));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        TypeToken<Map<Die.Face, String>> returnType = new TypeToken<>() {};
        return gson.fromJson(reader, returnType);
    }
}
