import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class RainbowTable implements IRainbowTable {

    private final char[] characters =
            "0123456789abcdefghijklmnopqrstuvwxyz".toCharArray();

    // maps te last generate reduced hash to the beginning PW
    private Map<String, String> rainbowTable = new HashMap<>();

    // the password is a seven digit string formed by characters.^
    private final int passwordLength = 7;

    // how many passwords should be generated
    private final int amountOfPasswords;

    // amount of rounds we do the (Hash -> Reduce) cycle
    private final int rounds;

    public RainbowTable(int amountOfPasswords, int rounds) {
        this.amountOfPasswords = amountOfPasswords;
        this.rounds = rounds;
        assert (characters.length == 36);
    }

    public RainbowTable compute() {

        for (int i = 0; i < amountOfPasswords; i++) {
            final String password = convertToString(i);
            final String key = computeKey(password);
            rainbowTable.put(key, password);
        }

        return this;
    }

    public String search(String forHash) {

        BigInteger hash = new BigInteger(forHash, 16);
        String reducedHash = reduce(hash, 0);

        // very first look up -> just last reduce function applied
        if (rainbowTable.containsKey(reducedHash)) {
            return reduceReverse(rainbowTable.get(reducedHash), forHash);
        }

        // Reduce(2) -> Compare
        // Reduce(1) -> Hash -> Reduce(2) -> Compare
        // Reduce(0) -> Hash -> Reduce(1) -> Hash -> Reduce(2) -> Compare
        for (int reverseRound = rounds; reverseRound > 0; reverseRound--) {

            // start over -> no match found
            hash = new BigInteger(forHash, 16);

            // apply the Reduce -> Hash i-times
            for (int i = (rounds - reverseRound); i >= 0; i--) {
                reducedHash = reduce(hash, (rounds - i - 1));
                hash = hash(reducedHash);
            }

            reducedHash = reduce(hash, rounds);
            // see if we got a match -> if so reconstruct the
            // chain beginning from the start value (pair->value)
            if (rainbowTable.containsKey(reducedHash)) {
                return reduceReverse(rainbowTable.get(reducedHash), forHash);
            }
        }

        return "HASH_NOT_FOUND";
    }

    private BigInteger hash(String fromPlainText) {

        BigInteger hash = BigInteger.valueOf(0);

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(fromPlainText.getBytes());
            hash = new BigInteger(1, md5.digest());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hash;
    }

    private String reduce(BigInteger hash, int round) {

        final BigInteger z = BigInteger.valueOf(characters.length);
        // in order to prevent collisions each level
        // has a slightly different reduction function result
        hash = hash.add(BigInteger.valueOf(round));

        String reduced = "";
        for (int i = 0; i < passwordLength; i++) {

            BigInteger[] divAndMod = hash.divideAndRemainder(z);
            BigInteger remainder = divAndMod[1];

            reduced = characters[remainder.intValue()] + reduced;
            hash = divAndMod[0]; // next hash is the divider
        }

        return addTrailingZeros(reduced);
    }

    private String reduceReverse(String startValue, String forHash) {

        String lastReducedHash = startValue;
        for (int round = 0; round <= rounds; round++) {
            BigInteger hash = hash(lastReducedHash);

            if (hash.equals(new BigInteger(forHash, 16))) {
                break; // we found it! -> lastReducedHash
            } else {
                lastReducedHash = reduce(hash, round);
            }
        }

        return lastReducedHash;
    }

    private String computeKey(String toValue) {

        String lastReducedHash = toValue;
        // do the hash -> reduce "rounds" times
        for (int round = 0; round <= rounds; round++) {
            BigInteger hash = hash(lastReducedHash);
            lastReducedHash = reduce(hash, round);
        }

        return lastReducedHash;
    }

    private String convertToString(int fromInt) {

        // just return in this case otherwise we have
        // a bit of too struggle in the conversation.
        if (fromInt == 0) return "0000000";

        String converted = "";
        for (int i = fromInt; i >= 1; i = i / characters.length) {
            converted = characters[i % characters.length] + converted;
        }

        return addTrailingZeros(converted);
    }

    // return a string that is 7 characters long with
    // trailing '0' in front (e.g. 0000000abc => 0000abc)
    private String addTrailingZeros(String prependTo) {

        String result = "0000000";
        result += prependTo;
        result = result.substring(
                result.length() - passwordLength, result.length());

        // make sure we did it right!
        assert (result.length() == passwordLength);
        return result;
    }
}