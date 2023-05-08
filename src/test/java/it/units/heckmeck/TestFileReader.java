package it.units.heckmeck;

import Heckmeck.Die;
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

        String actualLogo = FileReader.readTextFile(Path.of(LogoResource.toURI()));

        Assertions.assertEquals(LOGO, actualLogo.replaceAll("\\r\\n?", "\n"));
    }

    @Test
    void readDieFaceToStringMapFromJson() throws Exception {
        URL diceMapResource = TestFileReader.class.getClassLoader().getResource("DICE_MAP");
        Map<Die.Face, String> obtainedMap;
        Map<Die.Face, String> expectedMap = Map.ofEntries(
                entry(Die.Face.ONE, """
                .---------.
                |         |
                |    o    |
                |         |
                '---------'
                """),
                entry(Die.Face.TWO, """
                .---------.
                |      o  |
                |         |
                |  o      |
                '---------'
                """),
                entry(Die.Face.THREE, """
                .---------.
                |      o  |
                |    o    |
                |  o      |
                '---------'
                """),
                entry(Die.Face.FOUR, """
                .---------.
                |  o   o  |
                |         |
                |  o   o  |
                '---------'
                """),
                entry(Die.Face.FIVE, """
                .---------.
                |  o   o  |
                |    o    |
                |  o   o  |
                '---------'
                """),
                entry(Die.Face.WORM, """
                .---------.
                |   \\=\\   |
                |   /=/   |
                |   \\=\\   |
                '---------'
                """)
        );

        obtainedMap = FileReader.readDieFacesFromSingleJson(Path.of(diceMapResource.toURI()));

        // Not exactly as expected. Can we live well the same or not??
        // System.out.println(expectedMap.getClass() + " VS " + obtainedMap.getClass());
        Assertions.assertEquals(expectedMap, obtainedMap);
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
