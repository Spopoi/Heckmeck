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

    public static Path getLogoPath() {
        URL tilesResource = CliIOHandler.class.getClassLoader().getResource(LOGO_FILE);
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

    public static String collectionToText(Collection<?> collection) {
        String collectionAsText = "";
        for (var item : collection) {
            // at first iteration collectionAsText will have height=0 --> 2 spaces
            collectionAsText = new TextBlockConcatenator(collectionAsText, item.toString(), 1).concatenate();
        }
        return collectionAsText;
    }

}
