package Heckmeck;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileReader {

    public static String readFile(Path filePath) {
        StringBuilder res = new StringBuilder();
        try (InputStream in = Files.newInputStream(filePath);
             BufferedReader reader =
                     new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                res.append(line).append("\n");  // Notice that will add a newline at the end
            }
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return res.toString();
    }

    public static List<String> readMultipleTilesFromFile(Path filePath) {
        List<String> listOfTilesAsText = new ArrayList<>();
        StringBuilder tileAsText = new StringBuilder();
        try (InputStream in = Files.newInputStream(filePath);
             BufferedReader reader =
                     new BufferedReader(new InputStreamReader(in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (! line.equals("")) {
                    tileAsText.append(line).append("\n");  // Notice that will add a newline at the end
                }
                else {
                    listOfTilesAsText.add(tileAsText.toString());
                    tileAsText = new StringBuilder();
                }
            }
            listOfTilesAsText.add(tileAsText.toString());
        } catch (IOException ex) {
            System.err.println(ex);
        }
        return listOfTilesAsText;
    }

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
