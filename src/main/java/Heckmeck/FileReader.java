package Heckmeck;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.nio.file.Path;
import java.util.Map;

public class FileReader {

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

}
