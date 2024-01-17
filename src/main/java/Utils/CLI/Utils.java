package Utils.CLI;

import CLI.CliIOHandler;
import Utils.FileReader;

import java.net.*;
import java.nio.file.Path;
import java.util.Collection;

public class Utils {

    private static final String LOGO_FILE = "CLI/LOGO";
    private static final String RULES = "CLI/RULES";
    private static final String MENU = "CLI/MENU";

    private static final String MULTIPLAYER = "CLI/MULTIPLAYER";


    private static final String ACTUAL_PLAYER_INFO_TEMPLATE_FILE = "CLI/PLAYER_INFO_TEMPLATE";
    private Utils() {
    }

    public static String getLogo() {
        return getFileString(getPath(LOGO_FILE));
    }
    public static String getMultiplayerPath(){
        return getFileString(getPath(MULTIPLAYER));
    }

    public static String getMenu(){
        return getFileString(getPath(MENU));
    }

    public static String getRules(){
        return getFileString(getPath(RULES));
    }

    private static Path getPath(String fileName) {
        URL tilesResource = CliIOHandler.class.getClassLoader().getResource(fileName);
        Path resourcePath = null;
        try {
            resourcePath = Path.of(tilesResource.toURI());
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
        return resourcePath;
    }

    public static String getActualPlayerInfoTemplate() {
        return getFileString(getPath(ACTUAL_PLAYER_INFO_TEMPLATE_FILE));
    }

    private static String getFileString(Path filepath){
        return FileReader.readTextFile(filepath);
    }

    public static String collectionToString(Collection<?> collection) {
        TextBlock collectionAsTextBlock = new TextBlock("");
        for (var item : collection) {
            // at first iteration collectionAsText will have height=0 --> 2 spaces
            collectionAsTextBlock.concatenateWith(new TextBlock(item.toString()), 1);
        }
        return collectionAsTextBlock.toString();
    }





}
