import org.junit.Test;
import java.math.BigInteger;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RainbowTableTest {

    // class under test
    private RainbowTable cut = new RainbowTable();

    @Test
    public void testConversationFunction() {
        assertEquals("0000000", cut.convertToString(0));
        assertEquals("0000001", cut.convertToString(1));
        assertEquals("0000010", cut.convertToString(36));
        assertEquals("000001z", cut.convertToString(71));
        assertEquals("0000020", cut.convertToString(72));
        assertEquals("00001jk", cut.convertToString(2000));
    }

    @Test
    public void testReductionFunctionLevel0() {

        final int cylces = 0;
        BigInteger hash = cut.generateHash("0000000");
        String reducedHash = cut.reduceFromMD5Hash(hash, cylces);

        assertEquals("It should be same length as PW.",
                7, reducedHash.length());

        assertEquals("It should to do the hash->reduce cycle once.",
                "87inwgn", reducedHash);
    }

    @Test
    public void testReductionFunctionLevel1() {

        final int cylces = 1;
        BigInteger hash = cut.generateHash("87inwgn");
        String reducedHash = cut.reduceFromMD5Hash(hash, cylces);

        assertEquals("It should to do the hash->reduce cycle 2 times.",
                "frrkiis", reducedHash);
    }

    @Test
    public void testLastReducedHash() {
        assertEquals("It should generate this reduced hash after 0 cycles.",
                "87inwgn", cut.generateLastReducedHash("0000000", 0));

        assertEquals("It should generate this reduced hash after 2 cycles.",
                "dues6fg", cut.generateLastReducedHash("0000000", 2));
    }

    @Test
    public void testMD5HashGenerator() {
        assertEquals("It should generate this MD5 hash.",
                "29c3eea3f305d6b823f562ac4be35217",
                cut.generateHash("0000000").toString(16));

        assertEquals("It should generate this MD5 hash.",
                "12e2feb5a0feccf82a8d4172a3bd51c3",
                cut.generateHash("87inwgn").toString(16));
    }

    @Test
    public void testGenerateRainbowTable() {
        Map<String, String> rt = cut.generateRainbowTable(1, 2);
        assertEquals("It should only have one password in the map.",
                1, rt.size());

        assertEquals("It should be like this after 2 cycles.",
                "0000000", rt.get("dues6fg"));
    }

    @Test
    public void testFindInRainbowTableFirstMatches() {
        Map<String, String> rt = cut.generateRainbowTable(1, 0);
        assertEquals("It should be like this after 0 cycles.",
                "0000000", rt.get("87inwgn"));

        // given the hash find the password which
        // should have been generated at cycle 2.
        String shouldBePassword = cut.searchRainbowtable(
                rt, "29c3eea3f305d6b823f562ac4be35217", 0);

        assertEquals("It should find the password for this hash.",
                "0000000", shouldBePassword);
    }

    @Test
    public void testFindInRainbowTable() {
        Map<String, String> rt = cut.generateRainbowTable(1, 3);

        // given the hash find the password which
        // should have been generated at cycle 2.
        String shouldBePassword = cut.searchRainbowtable(rt,
                "12e2feb5a0feccf82a8d4172a3bd51c3", 3);

        assertEquals("It should find the password for this hash.",
               "87inwgn", shouldBePassword);
    }

    @Test
    public void testMD5HashCrackBruteForce() {

        assertEquals("1d56a37fb6b08aa709fe90e12ca59e12",
                cut.generateHash("0bgec3d").toString(16));

        //for (int i = 0; i < 2147483647; i++) {
        //    String hash = cut.generateHash(cut.convertToString(i));
        //
        //    if (hash.equals(searchedMD5Hash)) {
        //        // Found:1d56a37fb6b08aa709fe90e12ca59e12 0bgec3d
        //        System.out.println("Found:" + hash + " "
        //        + cut.convertToString(i));
        //        return;
        //    }
        //}
    }
}