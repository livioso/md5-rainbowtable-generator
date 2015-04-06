import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RainbowTableTest {

    // class under test
    private IRainbowTable rt = new RainbowTable(1, 0);

    @Test
    public void testFindInRainbowTableGivenTask() {
        rt = new RainbowTable(2000, 2000);
        assertEquals("It should find the password for this hash.", "0bgec3d",
                rt.compute().search("1d56a37fb6b08aa709fe90e12ca59e12"));
    }

    @Test
    public void testFindInRainbowTable() {
        rt = new RainbowTable(1, 200);
        assertEquals("It should find the password for this hash.", "87inwgn",
                rt.compute().search("12e2feb5a0feccf82a8d4172a3bd51c3"));
    }

    @Test
    public void testFindInRainbowTableNoMatch() {
        assertEquals("It should not find anything.", "HASH_NOT_FOUND",
                rt.compute().search("c0ffeebabe"));
    }

    @Test
    public void testFindInRainbowTableFirstMatches() {
        assertEquals("It should be like this after 0 rounds.", "0000000",
                rt.compute().search("29c3eea3f305d6b823f562ac4be35217"));
    }
}