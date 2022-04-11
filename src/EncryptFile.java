import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.util.encoders.Hex;
import javax.crypto.spec.IvParameterSpec;

public class EncryptFile {
    public static void encrypt(String fileName) {
        byte[] keyBytes = Hex.decode("000102030405060708090a0b0c0d0e0f");
        byte[] iv = RandomIVGenerator.getRandomIV();
        try {
            // reading
            byte[] input = FileUtil.readAllBytes(fileName);

            // encrypting
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            byte[] output = cipher.doFinal(input);

            // writing
            FileUtil.write("AES/CBC/PKCS5Padding", fileName, output, Hex.toHexString(iv));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}