import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.util.encoders.Hex;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;

public class DecryptFile {
    static String dir = "/Users/jakub/Desktop";
    static String storeFileName = dir + "/" + "keystore.bks";

    static void decrypt(String pathName) {
        String[] splitPath = pathName.split("\\.");
        String[] splicedArray = Arrays.copyOfRange(splitPath, 0, splitPath.length - 2);
        String testFilePath = String.join(".", splicedArray);

        File file = new File(storeFileName);
        // TODO message saying that key to decrypt is not available on the machine
        if (!file.exists()) {
            return;
        }
        SecretKeySpec secretKey = KeyStoreManager.getKey();

        try {
            // reading
            String ivString = FileUtil.getIV("AES/CBC/PKCS5Padding", pathName);
            IvParameterSpec iv = new IvParameterSpec(Hex.decode(ivString));
            byte[] input = FileUtil.readAllBytes("AES/CBC/PKCS5Padding", pathName);

            // decrypting
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] output = cipher.doFinal(input);

            // writing
            FileUtil.write(testFilePath, output);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("exception" + e.toString());
            if (e.toString() == "exceptionjavax.crypto.IllegalBlockSizeException: last block incomplete in decryption") {
                // file has been corrupted, TODO display an error message saying that file was corrupted
            }
        }
    }
}
