import org.bouncycastle.util.encoders.Hex;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class RandomIVGenerator {
    static byte[] getRandomIV() {
        byte[] defaultIV = Hex.decode("9f741fdb5d8845bdb48a94394e84f8a3");
        // generate random IV
        SecureRandom secureRandom;
        {
            try {
                secureRandom = SecureRandom.getInstance("DEFAULT", "BC");
                byte[] iv = new byte[16];
                secureRandom.nextBytes(iv);
                return iv;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return defaultIV;
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
                return defaultIV;
            }
        }
    }
}
