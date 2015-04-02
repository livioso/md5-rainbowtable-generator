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
        assertEquals("0000000", cut.numberToString(0));
        assertEquals("0000001", cut.numberToString(1));
        assertEquals("0000010", cut.numberToString(36));
        assertEquals("000001z", cut.numberToString(71));
        assertEquals("0000020", cut.numberToString(72));
        assertEquals("00001jk", cut.numberToString(2000));
    }
}