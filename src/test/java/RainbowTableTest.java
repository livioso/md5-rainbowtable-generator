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
        BigInteger hash = cut.hashFunction("0000000");
        String reducedHash = cut.reduceFunction(hash, cylces);

        assertEquals("It should be same length as PW.",
                7, reducedHash.length());

        assertEquals("It should to do the hash->reduce cycle once.",
                "87inwgn", reducedHash);
    }

    @Test
    public void testReductionFunctionLevel1() {

        final int cylces = 1;
        BigInteger hash = cut.hashFunction("87inwgn");
        String reducedHash = cut.reduceFunction(hash, cylces);

        assertEquals("It should to do the hash->reduce cycle 2 times.",
                "frrkiis", reducedHash);
    }

    @Test
    public void testLastReducedHash() {
        cut = new RainbowTable(0);
        assertEquals("It should generate this reduced hash after 0 rounds.",
                "87inwgn", cut.computeKeyToValue("0000000"));

        cut = new RainbowTable(2);
        assertEquals("It should generate this reduced hash after 2 rounds.",
                "dues6fg", cut.computeKeyToValue("0000000"));
    }

    @Test
    public void testMD5HashGenerator() {
        assertEquals("It should generate this MD5 hash.",
                "29c3eea3f305d6b823f562ac4be35217",
                cut.hashFunction("0000000").toString(16));

        assertEquals("It should generate this MD5 hash.",
                "12e2feb5a0feccf82a8d4172a3bd51c3",
                cut.hashFunction("87inwgn").toString(16));
    }

    @Test
    public void testGenerateRainbowTable() {
        cut = new RainbowTable(2);

        Map<String, String> rt = cut.generateRainbowTable(1);
        assertEquals("It should only have one password in the map.",
                1, rt.size());

        assertEquals("It should be like this after 2 rounds.",
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
        assertEquals("It should be like this after 0 rounds.",
                "0000000", rt.get("87inwgn"));

        String shouldBePassword = cut.searchRainbowTable(
                rt, "29c3eea3f305d6b823f562ac4be35217");

        assertEquals("It should find the password for this hash.",
                "0000000", shouldBePassword);
    }

    @Test
    public void testFindInRainbowTable() {
        cut = new RainbowTable(200);

        Map<String, String> rt = cut.generateRainbowTable(1);

        String shouldBePassword = cut.searchRainbowTable(rt,
                "12e2feb5a0feccf82a8d4172a3bd51c3");

        assertEquals("It should find the password for this hash.",
                "87inwgn", shouldBePassword);
    }

    @Test
    public void testFindInRainbowTableGivenTask() {

        cut = new RainbowTable(2000);
        Map<String, String> rt = cut.generateRainbowTable(2000);

        String shouldBePassword = cut.searchRainbowTable(rt,
                "1d56a37fb6b08aa709fe90e12ca59e12");

        assertEquals("It should find the password for this hash.",
                "0bgec3d", shouldBePassword);
    }
}