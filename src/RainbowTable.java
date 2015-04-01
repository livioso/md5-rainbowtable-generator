import java.util.*;

public class RainbowTable {

    Map<String, RainbowTableEntry> passwordToHash = new HashMap<>();
    List<Character> characters = new ArrayList<>();

    public RainbowTable() {
        buildRainbowTable();

        // digits ['0' - '9']
        for (char n = '0'; n <= '9'; n++) {
            characters.add(n);
        }

        // characters ['a' - 'z']
        for (char c = 'a'; c <= 'z'; c++) {
            characters.add(c);
        }
    }

    private void buildRainbowTable() {
        Iterator<Character> iter = characters.iterator();
        iter.next();
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
