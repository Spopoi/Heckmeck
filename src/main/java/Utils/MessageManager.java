package Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MessageManager {

    public enum PropertiesFileIdentifier {

        IOHANDLER_MESSAGES ("src/main/resources/IOHandlerMessages.properties"),

        GAME_MESSAGES("src/main/resources/GameMessages.properties");

        private final String pathAsString;

        PropertiesFileIdentifier(String pathAsString) {
            this.pathAsString = pathAsString;
        }

        private String getPathAsString() {
            return pathAsString;
        }

    }

    private final Properties gameMessagesProperties;

    public MessageManager(PropertiesFileIdentifier fileIdentifier) throws IOException {
        gameMessagesProperties = new Properties();
        gameMessagesProperties.load(new FileInputStream(fileIdentifier.getPathAsString()));
    }

    public String getMessage(String key) {
        return gameMessagesProperties.getProperty(key);
    }

}
