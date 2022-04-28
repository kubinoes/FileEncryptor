package security;

import org.bouncycastle.util.encoders.Hex;
import utilities.FileUtil;
import utilities.KeyStoreManager;
import utilities.RandomIVGenerator;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.security.MessageDigest;
import java.util.Arrays;

public class FileCipher {
    public static void encrypt(String fileName) throws Exception {
        //check if file with key exists and throw an exception to stop executing the encryption if missing
        File file = new File(KeyStoreManager.storeFilePath);
        if (!file.exists()) {
            throw new Exception("Keystore missing");
        }
        // fetch encryption key
        SecretKeySpec secretKey = KeyStoreManager.getKey();
        // generate random iv
        byte[] iv = RandomIVGenerator.getRandomIV();
        // read file
        byte[] fileBytes = FileUtil.readAllBytes(fileName);
        // create message digest
        byte[] digest;
        try {
            digest = createDigest(fileBytes);
        } catch (Exception e) {
            throw new Exception("Encryption failed. Hash value couldn't be created.");
        }
        // encrypt and write to the file
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
            byte[] output = cipher.doFinal(fileBytes);
            // add message digest, iv and cipher text together and write to a file
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(digest);
            outputStream.write(iv);
            outputStream.write(output);

            FileUtil.write(fileName, outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Encryption failed.");
        }
    }
    public static void decrypt(String pathName) throws Exception {
        Exception exception = new Exception("Corrupted file, failed to decrypt!");

        //check if file with key exists and throw an exception to stop executing the decryption if missing
        File file = new File(KeyStoreManager.storeFilePath);
        if (!file.exists()) {
            throw new Exception("Keystore missing");
        }

        SecretKeySpec secretKey = KeyStoreManager.getKey();
        // reading
        byte[] fileBytes = FileUtil.readAllBytes(pathName);
        // read digest 0..32 bytes
        byte[] digest = Arrays.copyOfRange(fileBytes, 0, 32);
        // read iv 32..48 bytes
        byte[] iv = Arrays.copyOfRange(fileBytes, 32, 48);
        // read the rest of the file that needs to be decrypted
        byte[] input = Arrays.copyOfRange(fileBytes, 48, fileBytes.length);
        // decrypt and write out the file
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
            byte[] output = cipher.doFinal(input);
            // check if stored and computed digests match
            if (verifyDigest(digest, output)) {
                FileUtil.write(pathName, output);
            } else {
                throw exception;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw exception;
        }
    }
    public static byte[] createDigest(byte[] input) throws Exception {
        try {
            // compute and return hash value from plaintext
            MessageDigest digest = MessageDigest.getInstance("SHA-256", "BC");
            return digest.digest(input);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Message digest not created!");
        }
    }
    public static boolean verifyDigest(byte[] storedDigest, byte[] plainText) {
        try {
            byte[] computedDigest = createDigest(plainText);
            return MessageDigest.isEqual(computedDigest, storedDigest);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
