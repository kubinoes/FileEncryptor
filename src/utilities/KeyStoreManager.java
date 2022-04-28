package utilities;

import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

public class KeyStoreManager {
    public static String storeFilePath = (System.getProperty("user.home")) + "/FileEncryptor/" + "keystore.bks";

    // check if keystore exists
    public static boolean loadKeyStore(char[] psw) {
        try {
            KeyStore keyStore = KeyStore.getInstance("BKS", "BC");
            FileInputStream fis = new FileInputStream(storeFilePath);
            keyStore.load(fis, psw);
            fis.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // load the keystore
    public static KeyStore loadKeyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance("BKS", "BC");
            FileInputStream fis = new FileInputStream(storeFilePath);
            keyStore.load(fis, PasswordUtil.getPassword());
            fis.close();
            return keyStore;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // get secure key
    public static SecretKeySpec getKey() {
        SecretKeySpec key = null;
        try {
            KeyStore ks = loadKeyStore();
            key = (SecretKeySpec) ks.getKey("key", PasswordUtil.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }

    // store the new keystore file on harddisk
    public static void storeKeyStore(KeyStore keyStore) {
        try {
            FileOutputStream fOut = new FileOutputStream(storeFilePath);
            keyStore.store(fOut, PasswordUtil.getPassword());
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // create an instance of new empty keystore
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

    // add a random key to the new keystore
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
            KeyStore.ProtectionParameter protection = new KeyStore.PasswordProtection(PasswordUtil.getPassword());
            keyStore.setEntry("key", entry, protection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
