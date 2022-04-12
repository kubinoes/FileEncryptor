import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.Scanner;

public class KeyStoreManager {
    static String dir = "/Users/jakub/Desktop";
    static String storeFileName = dir + "/" + "keystore.bks";

    private static final ThreadLocal<char[]> password = new ThreadLocal<>();

    public static char[] getPassword() {
        return password.get();
    }

    public static void setPassword(char[] passwordInput) {
        password.set(passwordInput);
    }

    // load the keystore
    public static KeyStore loadKeyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance("BKS", "BC");
            FileInputStream fis = new FileInputStream(storeFileName);
            // check if password isnt already saved in memory
            // save it to memory in case it isnt
            if (password.get() == null) {
                System.out.println("Please type password");
                Scanner scanner = new Scanner(System.in);
                password.set(scanner.nextLine().toCharArray());
            }
            keyStore.load(fis, password.get());
            fis.close();
            return keyStore;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static SecretKeySpec getKey() {
        SecretKeySpec key = null;
        try {
            KeyStore ks = loadKeyStore();
            key = (SecretKeySpec) ks.getKey("key", password.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }

    public static void storeKeyStore(KeyStore keyStore) {
        try {
            FileOutputStream fOut = new FileOutputStream(storeFileName);
            keyStore.store(fOut, password.get());
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static KeyStore createKeyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance("BKS", "BC");
            keyStore.load(null, null);
            return keyStore;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void generateAndAddKey(KeyStore keyStore) {
        try {
            // generate random bytes
            SecureRandom secureRandom = SecureRandom.getInstance("DEFAULT", "BC");
            byte[] keyBytes = new byte[16];
            secureRandom.nextBytes(keyBytes);
            // generate key
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            // add key to keystore
            KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(key);
            System.out.println("Please type password");
            Scanner scanner = new Scanner(System.in);
            password.set(scanner.nextLine().toCharArray());
            KeyStore.ProtectionParameter protection = new KeyStore.PasswordProtection(password.get());
            keyStore.setEntry("key", entry, protection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
