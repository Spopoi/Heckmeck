package Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MessageManager {
    private static final String IO_HANDLER = "src/main/resources/IOHandlerMessages.properties";
    private static final String PATH = "src/main/resources/Path.properties";
    private static final String GAME_MESSAGE = "src/main/resources/GameMessages.properties";

    private final Properties gameMessagesProperties;

    public MessageManager(String propertiesPath) throws IOException {
        gameMessagesProperties = new Properties();
        gameMessagesProperties.load(new FileInputStream(propertiesPath));
    }

    public String getMessage(String key) {
        return gameMessagesProperties.getProperty(key);
    }

    public static String getIOHandlerPropertiesPath () {
        return IO_HANDLER;
    }

    public static String getPathPropertiesPath(){
        return PATH;
    }

    public static String getGameMessagePropertiesPath(){
        return GAME_MESSAGE;
    }
}