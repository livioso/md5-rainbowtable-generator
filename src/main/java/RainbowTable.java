import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class RainbowTable {

    final private Character[] characters = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z' // refactor me :(
    };

    // the password is a seven digit string formed by characters. ^
    final int passwordLength = 7;

    // amount of rounds we do the (Hash -> Reduce) cycle
    final int rounds;

    public RainbowTable(int rounds) {
        this.rounds = rounds;
        assert (characters.length == 36);
    }

    public String convertToString(int fromInt) {

        // just return in this case otherwise we have
        // a bit of too struggle in the conversation.
        if (fromInt == 0) return "0000000";

        String converted = "";
        for (int i = fromInt; i >= 1; i = i / characters.length) {
            converted = characters[i % characters.length] + converted;
        }

        return addTrailingZeros(converted);
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

    public String generateLastReducedHash(String password) {

        String lastReducedHash = password;
        for(int cycle = 0; cycle <= rounds; cycle++) {
            BigInteger hash = generateMD5Hash(lastReducedHash);
            lastReducedHash = reduceFromMD5Hash(hash, cycle);
        }

        return lastReducedHash;
    }

    public String searchChainForHash(String startValue, String lookedForHash) {

        String lastReducedHash = startValue;

        for(int cycle = 0; cycle <= rounds; cycle++) {
            BigInteger hash = generateMD5Hash(lastReducedHash);

            if(hash.equals(new BigInteger(lookedForHash, 16))) {
                 break; // we found it! -> lastReducedHash
            } else {
                lastReducedHash = reduceFromMD5Hash(hash, cycle);
            }
        }

        return lastReducedHash;
    }

    public String reduceFromMD5Hash(BigInteger hash, int round) {

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

    public Map<String, String> generateRainbowTable(
            int amoutOfPasswords)
    {

        Map<String, String> rainbowTable = new HashMap<>();

        for (int i = 0; i < amoutOfPasswords; i++) {
            final String password = convertToString(i);
            final String reducedHash =
                    generateLastReducedHash(password);

            rainbowTable.put(reducedHash, password);
        }

        return rainbowTable;
    }

    public String searchRainbowTable(
            Map<String, String> rainbowTable, String lookedForHash)
    {
        BigInteger hash = new BigInteger(lookedForHash, 16);
        String reducedHash = reduceFromMD5Hash(hash, 0);

        // very first look up -> just last reduce function applied
        if(rainbowTable.containsKey(reducedHash)) {
            return searchChainForHash(
                    rainbowTable.get(reducedHash), lookedForHash);
        }
        
        // Example with 2 rounds:
        // Reduce(2) -> Compare
        // Reduce(1) -> Hash -> Reduce(2) -> Compare
        // Reduce(0) -> Hash -> Reduce(1) -> Hash -> Reduce(2) -> Compare
        for (int reverseRound = rounds; reverseRound > 0; reverseRound--) {

            // start over -> no match found
            hash = new BigInteger(lookedForHash, 16);

            // apply the Reduce -> Hash i-times
            for (int i = (rounds - reverseRound); i >= 0; i--) {
                reducedHash = reduceFromMD5Hash(hash, (rounds - i - 1));
                hash = generateMD5Hash(reducedHash);
            }

            reducedHash = reduceFromMD5Hash(hash, rounds);
            // see if we got a match -> if so reconstruct the
            // chain beginning from the start value (pair->value)
            if(rainbowTable.containsKey(reducedHash)) {
                return searchChainForHash(
                        rainbowTable.get(reducedHash), lookedForHash);
            }
        }

        return "HASH_NOT_FOUND"; // no match
    }

    // return a string that is 7 characters long with
    // trailing '0' in front (e.g. 0000000abc => 0000abc)
    private String addTrailingZeros(String prependTo) {

        String result = "0000000";
        result += prependTo;
        result = result.substring(
                result.length() - passwordLength, result.length());

        // make sure we did it right!
        assert(result.length() == passwordLength);
        return result ;
    }
}