package Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class StringManager{
    private final Properties properties = new Properties();
    public StringManager(String filePath) {
        loadProperties(filePath);
    }
    public String getString(String key) {
        return properties.getProperty(key, "Key not found");
    }

    private void loadProperties(String filePath) {
        try (FileInputStream input = new FileInputStream(filePath)) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
