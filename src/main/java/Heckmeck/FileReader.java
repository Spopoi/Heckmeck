package Heckmeck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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

}
