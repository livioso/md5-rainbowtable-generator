import org.junit.Test;

import java.math.BigInteger;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RainbowTableTest {

    // class under test
    private RainbowTable cut = new RainbowTable(0);

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
        BigInteger hash = cut.generateMD5Hash("0000000");
        String reducedHash = cut.reduceFromMD5Hash(hash, cylces);

        assertEquals("It should be same length as PW.",
                7, reducedHash.length());

        assertEquals("It should to do the hash->reduce cycle once.",
                "87inwgn", reducedHash);
    }

    @Test
    public void testReductionFunctionLevel1() {

        final int cylces = 1;
        BigInteger hash = cut.generateMD5Hash("87inwgn");
        String reducedHash = cut.reduceFromMD5Hash(hash, cylces);

        assertEquals("It should to do the hash->reduce cycle 2 times.",
                "frrkiis", reducedHash);
    }

    @Test
    public void testLastReducedHash() {
        cut = new RainbowTable(0);
        assertEquals("It should generate this reduced hash after 0 cycles.",
                "87inwgn", cut.generateRainbowTableEntryValue("0000000"));

        cut = new RainbowTable(2);
        assertEquals("It should generate this reduced hash after 2 cycles.",
                "dues6fg", cut.generateRainbowTableEntryValue("0000000"));
    }

    @Test
    public void testMD5HashGenerator() {
        assertEquals("It should generate this MD5 hash.",
                "29c3eea3f305d6b823f562ac4be35217",
                cut.generateMD5Hash("0000000").toString(16));

        assertEquals("It should generate this MD5 hash.",
                "12e2feb5a0feccf82a8d4172a3bd51c3",
                cut.generateMD5Hash("87inwgn").toString(16));
    }

    @Test
    public void testGenerateRainbowTable() {
        cut = new RainbowTable(2);

        Map<String, String> rt = cut.generateRainbowTable(1);
        assertEquals("It should only have one password in the map.",
                1, rt.size());

        assertEquals("It should be like this after 2 cycles.",
                "0000000", rt.get("dues6fg"));
    }

    @Test
    public void testFindInRainbowTableNoMatch() {

        Map<String, String> rt = cut.generateRainbowTable(1);

        String shoulNotdBeFound = cut.searchRainbowTable(
                rt, "c0ffeebabec0ffeebabec0ffeebabe");

        assertEquals("It should not find anything.",
                "HASH_NOT_FOUND", shoulNotdBeFound);
    }

    @Test
    public void testFindInRainbowTableFirstMatches() {
        Map<String, String> rt = cut.generateRainbowTable(1);
        assertEquals("It should be like this after 0 cycles.",
                "0000000", rt.get("87inwgn"));

        // given the hash find the password which
        // should have been generated at cycle 2.
        String shouldBePassword = cut.searchRainbowTable(
                rt, "29c3eea3f305d6b823f562ac4be35217");

        assertEquals("It should find the password for this hash.",
                "0000000", shouldBePassword);
    }

    @Test
    public void testFindInRainbowTable() {
        cut = new RainbowTable(200);

        Map<String, String> rt = cut.generateRainbowTable(1);

        // given the hash find the password which
        // should have been generated at cycle 2.
        String shouldBePassword = cut.searchRainbowTable(rt,
                "12e2feb5a0feccf82a8d4172a3bd51c3");

        assertEquals("It should find the password for this hash.",
                "87inwgn", shouldBePassword);
    }

    @Test
    public void testFindInRainbowTableGivenTask() {
        cut = new RainbowTable(2000);

        Map<String, String> rt = cut.generateRainbowTable(2000);

        // given the hash find the password which
        // should have been generated at cycle 2.
        String shouldBePassword = cut.searchRainbowTable(rt,
                "1d56a37fb6b08aa709fe90e12ca59e12");

        assertEquals("It should find the password for this hash.",
                "0bgec3d", shouldBePassword);
    }

    @Test
    public void testMD5HashCrackBruteForce() {

        assertEquals("1d56a37fb6b08aa709fe90e12ca59e12",
                cut.generateMD5Hash("0bgec3d").toString(16));

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