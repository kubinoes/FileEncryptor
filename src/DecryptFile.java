import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.util.Arrays;

public class DecryptFile {
    private static String storeFilePath = KeyStoreManager.storeFilePath;

    static void decrypt(String pathName) {
        String[] splitPath = pathName.split("\\.");
        String[] splicedArray = Arrays.copyOfRange(splitPath, 0, splitPath.length - 1);
        String testFilePath = String.join(".", splicedArray);

        File file = new File(storeFilePath);
        // TODO message saying that key to decrypt is not available on the machine
        if (!file.exists()) {
            return;
        }
        SecretKeySpec secretKey = KeyStoreManager.getKey();

        // reading
        byte[] fileBytes = FileUtil.readAllBytes(pathName);
        // read first 16 elements from fileBytes array to get IV
        byte[] iv = Arrays.copyOfRange(fileBytes, 0, 16);
        // read the rest of the file to get input for decryption
        byte[] input = Arrays.copyOfRange(fileBytes, 16, fileBytes.length);

        try {
            // decrypting
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
            byte[] output = cipher.doFinal(input);

            // writing
            FileUtil.write(testFilePath, output);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.toString() == "exceptionjavax.crypto.IllegalBlockSizeException: last block incomplete in decryption") {
                // file has been corrupted, TODO display an error message saying that file was corrupted
            }
        }
    }
}
