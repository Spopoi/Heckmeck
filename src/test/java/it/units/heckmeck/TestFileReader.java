package it.units.heckmeck;

import Heckmeck.FileReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Map;

import static java.util.Map.entry;

public class TestFileReader {

    private static final String LOGO = getExpectedLogo();

    public static final Map<String, String> fileNameToCorrectDieFace = Map.ofEntries(
            entry("TEST_DIE_ONE", """
                .---------.
                |         |
                |    o    |
                |         |
                '---------'
                """),
            entry("TEST_DIE_TWO", """
                .---------.
                |      o  |
                |         |
                |  o      |
                '---------'
                """),
            entry("TEST_DIE_THREE", """
                .---------.
                |      o  |
                |    o    |
                |  o      |
                '---------'
                """)
    );


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

    @ParameterizedTest
    @ValueSource(strings = {"TEST_DIE_ONE", "TEST_DIE_TWO", "TEST_DIE_THREE"})
    void readSingleDieTextRepresentation(String fileName) throws Exception {
        URL dieResource = TestFileReader.class.getClassLoader().getResource(fileName);
        String expectedDieAsText = fileNameToCorrectDieFace.get(fileName);

        String dieAsText = FileReader.readDieFile(Path.of(dieResource.toURI()));

        Assertions.assertEquals(expectedDieAsText, dieAsText);
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
