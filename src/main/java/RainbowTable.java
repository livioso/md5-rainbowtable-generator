import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class RainbowTable {

    // the password is a seven digit string formed by this characters.
    final private Character[] characters = {
            '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e',
            'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o',
            'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y',
            'z' // refactor me :(
    };

    // how many characters the password has.
    final int passwordLength = 7;

    public RainbowTable() {
        // ['0' -'9'] and ['a' - 'z]
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

    public BigInteger generateHash(String fromPlainText) {

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

    public String generateLastReducedHash(String password, int amountOfCycles) {

        String lastReducedHash = password;
        for(int cycle = 0; cycle <= amountOfCycles; cycle++) {
            BigInteger hash = generateHash(lastReducedHash);
            lastReducedHash = reduceFromMD5Hash(hash, cycle);
            System.out.println(lastReducedHash + ", " +cycle);
        }

        return lastReducedHash;
    }

    public String reduceFromMD5Hash(BigInteger hash, int level) {

        final BigInteger z = BigInteger.valueOf(characters.length);
        // in order to prevent collisions each level
        // has a slightly different reduction function result
        hash = hash.add(BigInteger.valueOf(level));

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
            int amoutOfPasswords, int amountOfCycles)
    {

        Map<String, String> passwordToLastReduced = new HashMap<>();

        for (int i = 0; i < amoutOfPasswords; i++) {
            final String password = convertToString(i);
            final String reducedHash =
                    generateLastReducedHash(password, amountOfCycles);

            passwordToLastReduced.put(reducedHash, password);
        }

        return passwordToLastReduced;
    }

    public String searchRainbowtable(
            Map<String, String> rainbowTable, String lookedForHash, int amountOfCycles)
    {
        final int cycles = amountOfCycles;




        BigInteger hash = new BigInteger(lookedForHash, 16);
        String reducedHash = "";

        //if(rainbowTable.containsKey(reducedHash)) {
        //    return "MATCH_FOUND";
        //}
        
        // Example with 2 cycles / rounds:
        // Reduce(2) -> Compare
        // Reduce(1) -> Hash -> Reduce(2) -> Compare
        // Reduce(0) -> Hash -> Reduce(1) -> Hash -> Reduce(2) -> Compare
        for (int rCycle = cycles; rCycle > 0; rCycle--) {

            hash = new BigInteger(lookedForHash, 16);

            for (int fCycle = cycles-rCycle; fCycle >= 0; fCycle--) {

                reducedHash = reduceFromMD5Hash(hash, cycles - fCycle-1);
                hash = generateHash(reducedHash);

                System.out.print("Reduce(" + reducedHash + ", " + ((cycles-fCycle-1)) + ") -> Hash -> ");

                //System.out.print("Reduce(" + ((cycles-fCycle-1)) + ") -> Hash -> ");
            }

            reducedHash = reduceFromMD5Hash(hash, cycles);
            System.out.print("Reduce(" + (reducedHash + ", " + cycles) + ") -> Compare \n");


            if(rainbowTable.containsKey(reducedHash)) {
                return "MATCH_FOUND";
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