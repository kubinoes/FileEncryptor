import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.util.encoders.Hex;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.security.KeyStore;

public class EncryptFile {
    static String dir = "/Users/jakub/Desktop";
    static String storeFileName = dir + "/" + "keystore.bks";

    public static void encrypt(String fileName) {
        //check if file with key exists
        File file = new File(storeFileName);
        KeyStore keyStore;
        if (!file.exists()) {
            keyStore = KeyStoreManager.createKeyStore();
            KeyStoreManager.generateAndAddKey(keyStore);
            KeyStoreManager.storeKeyStore(keyStore);
        }
        SecretKeySpec secretKey = KeyStoreManager.getKey();
        byte[] iv = RandomIVGenerator.getRandomIV();
        try {
            // reading
            byte[] input = FileUtil.readAllBytes(fileName);

            // encrypting
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            //SecretKeySpec key = new SecretKeySpec(secretKey, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));
            byte[] output = cipher.doFinal(input);

            // writing
            FileUtil.write("AES/CBC/PKCS5Padding", fileName, output, Hex.toHexString(iv));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}