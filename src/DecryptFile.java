import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.util.encoders.Hex;
import javax.crypto.spec.IvParameterSpec;
import java.lang.reflect.Array;
import java.util.Arrays;

public class DecryptFile {
    byte[] keyBytes = Hex.decode("000102030405060708090a0b0c0d0e0f");

    static void decrypt(String pathName) {
        String[] splitPath = pathName.split("\\.");
        String[] splicedArray = Arrays.copyOfRange(splitPath, 0, splitPath.length - 2);
        String testFilePath = String.join(".", splicedArray);

        byte[] keyBytes = Hex.decode("000102030405060708090a0b0c0d0e0f");
        try {
            // reading
            String ivString = FileUtil.getIV("AES/CBC/PKCS5Padding", pathName);
            IvParameterSpec iv = new IvParameterSpec(Hex.decode(ivString));
            byte[] input = FileUtil.readAllBytes("AES/CBC/PKCS5Padding", pathName);

            // decrypting
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] output = cipher.doFinal(input);

            // writing
            FileUtil.write(testFilePath, output);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("exception" + e.toString());
            if (e.toString() == "exceptionjavax.crypto.IllegalBlockSizeException: last block incomplete in decryption") {
                // file has been corrupted, TODO display a message
            }
        }
    }
}
