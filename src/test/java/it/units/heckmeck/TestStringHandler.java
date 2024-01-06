package it.units.heckmeck;

import Utils.StringManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestStringHandler {
    static StringManager stringManager;
    @BeforeEach
    public void setUp(){
        stringManager = new StringManager("src/main/java/Utils/CLI/PATHS");
    }
    @Test
    public void testString(){
        String s = stringManager.getString("LOGO_FILE");
        Assertions.assertEquals(s , "CLI/LOGO");
    }
}
