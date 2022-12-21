package it.units.heckmeck;

import Heckmeck.FileReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;

import static java.util.Map.entry;

public class TestFileReader {

    private static final String LOGO = getExpectedLogo();

    @Test
    void readsIntegerToStringMapFromJson() throws URISyntaxException {
        URL multipleTilesMapResource = TestFileReader.class.getClassLoader().getResource("MULTIPLE_TILES_MAP");
        Map<Integer, String> obtainedMap;
        Map<Integer, String> expectedMap = Map.ofEntries(
                entry(21, """
                        .------.
                        |  21  |
                        |  ~   |
                        |      |
                        '------'
                        """),
                entry(22, """
                        .------.
                        |  22  |
                        |  ~   |
                        |      |
                        '------'
                        """),
                entry(23, """
                        .------.
                        |  23  |
                        |  ~   |
                        |      |
                        '------'
                        """),
                entry(24, """
                        .------.
                        |  24  |
                        |  ~   |
                        |      |
                        '------'
                        """),
                entry(28, """
                        .------.
                        |  28  |
                        |  ~~  |
                        |      |
                        '------'
                        """),
                entry(32, """
                        .------.
                        |  32  |
                        |  ~~  |
                        |  ~   |
                        '------'
                        """),
                entry(36, """
                        .------.
                        |  36  |
                        |  ~~  |
                        |  ~~  |
                        '------'
                        """)
        );

        obtainedMap = FileReader.readTilesFromSingleJson(Path.of(multipleTilesMapResource.toURI()));

        // Not exactly as expected. Can we live well the same or not??
        // System.out.println(expectedMap.getClass() + " VS " + obtainedMap.getClass());
        Assertions.assertEquals(expectedMap, obtainedMap);
    }

    @Test
    void readLogoAsTextFromFile() throws Exception {
        URL LogoResource = TestFileReader.class.getClassLoader().getResource("TEST_LOGO");

        String actualLogo = FileReader.readLogoFromTextFile(Path.of(LogoResource.toURI()));

        Assertions.assertEquals(LOGO, actualLogo);
    }

    @Test
    void readSingleDieWithOneFaceTextRepresentation() throws Exception {
        URL dieOneResource = TestFileReader.class.getClassLoader().getResource("TEST_DIE");
        String expectedDieOneAsText = """
                .---------.
                |         |
                |    o    |
                |         |
                '---------'
                """;

        String dieOneAsText = FileReader.readDieOneFile(Path.of(dieOneResource.toURI()));

        Assertions.assertEquals(expectedDieOneAsText, dieOneAsText);
    }

    private static String getExpectedLogo() {
        return """
                  _/'')
                 / />>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>         _______
                ( ( ,--.  ,--.             ,--.                           ,--.            /\\ o o o\\
                \\ ) |  '--'  | ,---.  ,---.|  |,-. ,--,--,--. ,---.  ,---.|  |,-.        /o \\ o o o\\_______
                    |  .--.  || .-. :| .--'|     / |        || .-. :| .--'|     /       <    >------>   o /|
                    |  |  |  |\\   --.\\ `--.|  \\  \\ |  |  |  |\\   --.\\ `--.|  \\  \\        \\ o/  o   /_____/o|
                    `--'  `--' `----' `---'`--'`--'`--`--`--' `----' `---'`--'`--'        \\/______/     |oo|
                   <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<             |   o   |o/
                                                                                               |_______|/
                """;
    }

}
