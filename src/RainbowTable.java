import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
            'z' // refactor me :(
    };

    public RainbowTable() {
        // ['0' -'9'] and ['a' - 'z]
        assert (characters.length == 36);

        buildRainbowTable();
    }

    private void buildRainbowTable() {
        for (int i = 0; i <= 2000; i++) {
            final String password = convertToString(i);
            passwordToHash.put(password, buildRainbowTableEntry(password));
        }
    }

    public String convertToString(int fromInt) {

        String result = "0000000";
        if (fromInt == 0) return result;

        String converted = "";
        for (int i = fromInt; i >= 1; i = i / characters.length) {
            converted = characters[i % characters.length] + converted;
        }

        return addTrailingZeros(converted);
    }

    public RainbowTableEntry buildRainbowTableEntry(String fromPassword) {

        BigInteger md5Hash = generateMD5Hash(fromPassword);

        String reducedHashStart = "";
        String reducedHashEnd = "";

        RainbowTableEntry entry =
                new RainbowTableEntry(reducedHashStart, reducedHashEnd);

        return entry;
    }

    public BigInteger generateMD5Hash(String fromPlainText) {

        BigInteger md5Hash = BigInteger.valueOf(0);

        try {

            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(fromPlainText.getBytes());
            md5Hash = new BigInteger(1, md5.digest());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return md5Hash;
    }

    public String reduceFromMD5Hash(BigInteger hash, int level) {

        String result = "0000000";

        final BigInteger z = BigInteger.valueOf(characters.length);

        // in order to prevent collisions each level
        // has a slightly different reduction function
        hash = hash.add(BigInteger.valueOf(level));

        String reduced = "";
        for (int i = 0; i < 7; i++) {

            BigInteger[] divAndMod = hash.divideAndRemainder(z);
            BigInteger remainder = divAndMod[1];

            reduced = characters[remainder.intValue()] + reduced;

            // next hash is the divider
            hash = divAndMod[0];
        }

        return addTrailingZeros(reduced);
    }

    // return a string that is 7 characters long with
    // trailing '0' in front (e.g. 0000000abc => 0000abc)
    private String addTrailingZeros(String prependTo) {
        String result = "0000000";

        result += prependTo;
        result = result.substring(result.length() - 7, result.length());

        return result ;
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
