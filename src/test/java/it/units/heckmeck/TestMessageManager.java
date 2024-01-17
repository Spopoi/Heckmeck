package it.units.heckmeck;

import Utils.MessageManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static Utils.MessageManager.getIOHandlerPropertiesPath;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMessageManager {

    @Test
    public void correctlyGetMessageFromKey() throws IOException {
        MessageManager messageManager = new MessageManager(getIOHandlerPropertiesPath());
        String expectedMessage = "                      Welcome in Heckmeck";

        String readMessage = messageManager.getMessage("welcomeMsg");

        assertEquals(expectedMessage, readMessage);
    }

}
