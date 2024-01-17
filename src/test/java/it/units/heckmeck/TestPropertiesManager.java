package it.units.heckmeck;

import Utils.PropertiesManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static Utils.PropertiesManager.getIOHandlerPropertiesPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPropertiesManager {

    @Test
    public void correctlyGetMessageFromKey() throws IOException {
        PropertiesManager propertiesManager = new PropertiesManager(getIOHandlerPropertiesPath());
        String expectedMessage = "                      Welcome in Heckmeck";

        String readMessage = propertiesManager.getMessage("welcomeMsg");

        assertEquals(expectedMessage, readMessage);
    }

}
