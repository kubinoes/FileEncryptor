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


    static char[] storePW = "burger".toCharArray(), secretKeyPW = "pizza".toCharArray();

    // load the keystore
    public static KeyStore loadKeyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance("BKS", "BC");
            FileInputStream fis = new FileInputStream(storeFileName);
            keyStore.load(fis, storePW);
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
            System.out.println("Please type password");
            Scanner scanner = new Scanner(System.in);
            char[] pw = scanner.nextLine().toCharArray();
            key = (SecretKeySpec) ks.getKey("key", pw);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }

    public static void storeKeyStore(KeyStore keyStore) {
        try {
            FileOutputStream fOut = new FileOutputStream(storeFileName);
            keyStore.store(fOut, storePW);
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
            char[] pw = scanner.nextLine().toCharArray();
            KeyStore.ProtectionParameter protection = new KeyStore.PasswordProtection(pw);
            keyStore.setEntry("key", entry, protection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
