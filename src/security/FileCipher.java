package security;

import utilities.FileUtil;
import utilities.KeyStoreManager;
import utilities.RandomIVGenerator;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.util.Arrays;

public class FileCipher {
    public static void encrypt(String fileName) throws Exception {
        //check if file with key exists
        File file = new File(KeyStoreManager.storeFilePath);
        if (!file.exists()) {
            throw new Exception("Keystore missing");
        }
        // fetch encryption key
        SecretKeySpec secretKey = KeyStoreManager.getKey();
        // generate iv
        byte[] iv = RandomIVGenerator.getRandomIV();
        // read file
        byte[] fileBytes = FileUtil.readAllBytes(fileName);
        // merge iv and file byte arrays
        int ivLength = iv.length;
        int fileLength = fileBytes.length;
        byte[] input = new byte[ivLength + fileLength];
        System.arraycopy(iv, 0, input, 0, ivLength);
        System.arraycopy(fileBytes, 0, input, ivLength, fileLength);
        // encrypt and write the file
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
            byte[] output = cipher.doFinal(input);

            FileUtil.write(fileName, output);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Encryption failed.");
        }
    }
    public static void decrypt(String pathName) throws Exception {

        File file = new File(KeyStoreManager.storeFilePath);
        if (!file.exists()) {
            throw new Exception("Keystore missing");
        }
        SecretKeySpec secretKey = KeyStoreManager.getKey();
        // reading
        byte[] fileBytes = FileUtil.readAllBytes(pathName);
        // read first 16 elements from fileBytes array to get IV
        byte[] iv = Arrays.copyOfRange(fileBytes, 0, 16);
        // read the rest of the file that needs to be decrypted
        byte[] input = Arrays.copyOfRange(fileBytes, 16, fileBytes.length);

        // decrypt and write out the file
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
            byte[] output = cipher.doFinal(input);

            FileUtil.write(pathName, output);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("File couldn't be decrypted!");
        }
    }
}
