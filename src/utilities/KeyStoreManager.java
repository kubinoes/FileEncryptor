package utilities;

import viewComponents.User;
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
            keyStore.load(fis, User.getPassword());
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
            key = (SecretKeySpec) ks.getKey("key", User.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return key;
    }

    public static void storeKeyStore(KeyStore keyStore) {
        try {
            FileOutputStream fOut = new FileOutputStream(storeFilePath);
            keyStore.store(fOut, User.getPassword());
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
            KeyStore.ProtectionParameter protection = new KeyStore.PasswordProtection(User.getPassword());
            keyStore.setEntry("key", entry, protection);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
