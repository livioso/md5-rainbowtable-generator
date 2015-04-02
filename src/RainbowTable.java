import java.util.HashMap;
import java.util.Map;

public class RainbowTable {

    private Map<String, RainbowTableEntry> passwordToHash = new HashMap<>();

    private Character[] characters = {
            '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o',
            'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y',
            'z' // refactor me :-(
    };

    public RainbowTable() {
        assert (characters.length == 36);
        buildRainbowTable();
    }

    private void buildRainbowTable() {
        for (int i = 0; i <= 2000; i++) {
            System.out.println(i + ": " + numberToString(i));
        }
    }

    public String numberToString(int n) {

        String result = "0000000";
        // special case for 0 as we would
        // struggle to handle it properly
        // in the loop below
        if (n == 0) return result;

        String converted = "";
        for (int i = n; i >= 1; i = i / characters.length) {
            converted = characters[i % characters.length] + converted;
        }

        result += converted;
        // the interesting part is
        // in the last 7 characters
        return result.substring(
                result.length() - 7,
                result.length());
    }

    private RainbowTableEntry buildRainbowTableEntry(String fromHash) {


        String reducedHashStart = "";
        String reducedHashEnd = "";

        RainbowTableEntry entry =
                new RainbowTableEntry(reducedHashStart, reducedHashEnd);

        return entry;
    }

    public String resolveFromMD5Hash(String md5hash) {
        return "";
    }

    private class RainbowTableEntry {

        private String startHash;
        private String endHash;

        public RainbowTableEntry(String startHash, String endHash) {
            this.startHash = startHash;
            this.endHash = endHash;
        }
    }
}
