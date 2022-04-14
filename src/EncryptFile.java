import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.security.KeyStore;

public class EncryptFile {
    static String storeFilePath = KeyStoreManager.storeFilePath;

    public static void encrypt(String fileName) {
        //check if file with key exists
        File file = new File(storeFilePath);
        KeyStore keyStore;
        if (!file.exists()) {
            // TODO logout
        }
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

        try {
            // encrypting
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            //SecretKeySpec key = new SecretKeySpec(secretKey, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
            byte[] output = cipher.doFinal(input);

            // writing
            FileUtil.write("AES/CBC/PKCS5Padding", fileName, output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}