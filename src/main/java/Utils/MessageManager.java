package Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class MessageManager {

    private static final String GAME_MESSAGES_FILE = "src/main/resources/GameMessages.properties";

    private final Properties gameMessagesProperties;

    public MessageManager() throws IOException {
        gameMessagesProperties = new Properties();
        gameMessagesProperties.load(new FileInputStream(GAME_MESSAGES_FILE));
    }

    public String getMessage(String key) {
        return gameMessagesProperties.getProperty(key);
    }

}
