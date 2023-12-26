package CLI;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collection;

class Utils {

    private static final String LOGO_FILE = "LOGO";
    private static final String ACTUAL_PLAYER_INFO_TEMPLATE_FILE = "PLAYER_INFO_TEMPLATE";

    private Utils() {
    }

    //TODO FAre un getFilesPath
    public static Path getLogoPath() {
        return getPath(LOGO_FILE);
    }

    public static Path getPath(String fileName) {
        URL tilesResource = CliIOHandler.class.getClassLoader().getResource(fileName);
        Path resourcePath = null;
        try {
            resourcePath = Path.of(tilesResource.toURI());
        } catch (URISyntaxException ex) {
            System.out.println(ex);
        }
        return resourcePath;
    }

    public static Path getActualPlayerInfoTemplate() {
        URL tilesResource = CliIOHandler.class.getClassLoader().getResource(ACTUAL_PLAYER_INFO_TEMPLATE_FILE);
        Path resourcePath = null;
        try {
            resourcePath = Path.of(tilesResource.toURI());
        } catch (URISyntaxException ex) {
            System.out.println(ex);
        }
        return resourcePath;
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
