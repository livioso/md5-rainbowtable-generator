import org.junit.Test;
import java.math.BigInteger;
import static org.junit.Assert.*;

public class RainbowTableTest {

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

        final int level = 0;

        BigInteger hash = cut.generateMD5Hash("0000000");
        String reducedHash = cut.reduceFromMD5Hash(hash, level);

        assertEquals(7, reducedHash.length());
        assertEquals("87inwgn", reducedHash);
    }

    @Test
    public void testReductionFunctionLevel1() {

        final int level = 1;

        BigInteger hash = cut.generateMD5Hash("87inwgn");
        String reducedHash = cut.reduceFromMD5Hash(hash, level);

        assertEquals(7, reducedHash.length());
        assertEquals("frrkiis", reducedHash);
    }

    @Test
    public void testLastReducedHash() {
        assertEquals("87inwgn", cut.generateLastReducedHash("0000000", 0));
        assertEquals("dues6fg", cut.generateLastReducedHash("0000000", 2));
    }

    @Test
    public void testMD5HashGenerator() {

        assertEquals("29c3eea3f305d6b823f562ac4be35217",
                cut.generateMD5Hash("0000000").toString(16));

        assertEquals("12e2feb5a0feccf82a8d4172a3bd51c3",
                cut.generateMD5Hash("87inwgn").toString(16));
    }

    @Test
    public void testMD5HashCrackBruteForce() {

        assertEquals("1d56a37fb6b08aa709fe90e12ca59e12",
                cut.generateMD5Hash("0bgec3d").toString(16));

        //for (int i = 0; i < 2147483647; i++) {
        //    String hash = cut.generateMD5Hash(cut.convertToString(i));
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