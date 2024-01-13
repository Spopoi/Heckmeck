package it.units.heckmeck;

import Utils.MessageManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestMessageManager {

    @Test
    public void correctlyGetMessageFromKey() throws IOException {
        MessageManager messageManager = new MessageManager();
        String expectedMessage = "                      Welcome in Heckmeck";

        String readMessage = messageManager.getMessage("welcomeMsg");

        assertEquals(expectedMessage, readMessage);
    }
}
