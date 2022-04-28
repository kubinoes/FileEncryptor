package utilities;

import org.bouncycastle.util.encoders.Hex;
import java.security.SecureRandom;

public class RandomIVGenerator {
    public static byte[] getRandomIV() {
        // fall back iv in case of random generation failure
        byte[] defaultIV = Hex.decode("9f741fdb5d8845bdb48a94394e84f8a3");
        // generate random IV
        SecureRandom secureRandom;
        {
            try {
                secureRandom = SecureRandom.getInstance("DEFAULT", "BC");
                byte[] iv = new byte[16];
                secureRandom.nextBytes(iv);
                return iv;
            } catch (Exception e) {
                e.printStackTrace();
                return defaultIV;
            }
        }
    }
}
