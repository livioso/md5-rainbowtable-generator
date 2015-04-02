import org.junit.Test;

import static org.junit.Assert.*;

public class RainbowTableTest {

    private RainbowTable cut = new RainbowTable();

    @org.junit.Before
    public void setUp() throws Exception {

    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void testResolveFromMD5Hash() throws Exception {

    }

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
    public void testReductionFunction() {

        String reducedHash = cut.reduceFromMD5Hash(
                cut.generateMD5Hash("0000000"));

        assertEquals(7, reducedHash.length());
        assertEquals("87inwgn", reducedHash);

    }

    @Test
    public void testMD5HashGenerator() {

        assertEquals("29c3eea3f305d6b823f562ac4be35217",
                cut.generateMD5Hash("0000000"));

        assertEquals("12e2feb5a0feccf82a8d4172a3bd51c3",
                cut.generateMD5Hash("87inwgn"));
    }

    @Test
    public void testMD5HashCrackBruteForce() {

        assertEquals("1d56a37fb6b08aa709fe90e12ca59e12",
                cut.generateMD5Hash("0bgec3d"));

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