import javafx.scene.control.Button;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.util.Arrays;

public class TestButton extends Button {
    TestButton() {
        setText("Run Test");
        setOnAction(a ->{
            // create a file in user directory
            String testFilePath = System.getProperty("user.home") + "/testFileEncryptor.txt";
            try {
                File fileToEncrypt = new File(testFilePath);
                if (fileToEncrypt.createNewFile()) {
                    System.out.println("Empty file created.");
                } else {
                    System.out.println("File already exists. Test failed...");
                    return;
                }
            } catch(IOException e) {
                System.out.println("Test failed");
                e.printStackTrace();
                return;
            }
            // write a string to the test file
            try {
                FileWriter fileWriter = new FileWriter(testFilePath);
                fileWriter.write("Very secret message");
                fileWriter.close();
                System.out.println("Successfully wrote to the file.");
            } catch (IOException e) {
                System.out.println("Test failed");
                e.printStackTrace();
                return;
            }
            // set password to the memory
            String password = "superSecretPassword";
            User.setPassword(password.toCharArray());
            // delete existing keystore
            File storeFile = new File(System.getProperty("user.home") + "/FileEncryptor/keystore.bks");
            if (storeFile.exists()) {
                if (storeFile.delete()) {
                    System.out.println("Successfully deleted the existing keystore");
                } else {
                    System.out.println("File wasnt deleted. Test failed...");
                    return;
                }
            }
            // create test keystore
            try {
                KeyStore keyStore = KeyStoreManager.createKeyStore();
                KeyStoreManager.generateAndAddKey(keyStore);
                KeyStoreManager.storeKeyStore(keyStore);
            } catch (Exception e) {
                System.out.println("Test failed");
                return;
            }
            // encrypt it
            EncryptFile.encrypt(testFilePath);
            System.out.println("File encrypted");
            // rename the original file to test... file
            File originalFile = new File(testFilePath);
            File rename = new File(System.getProperty("user.home") + "/test.testFileEncryptor.txt");
            if (originalFile.renameTo(rename)) {
                System.out.println("Original file successfully renamed");
            } else {
                System.out.println("Original file renaming failed. Test failed");
                return;
            }
            // decrypt
            DecryptFile.decrypt(testFilePath + ".aes");
            System.out.println("File decrypted");
            // compare cipher text to original
            byte[] originalFileBytes = FileUtil.readAllBytes(System.getProperty("user.home") + "/test.testFileEncryptor.txt");
            byte[] cipherFileBytesWithIV = FileUtil.readAllBytes(testFilePath + ".aes");
            byte[] cipherFileBytes = Arrays.copyOfRange(cipherFileBytesWithIV, 16, cipherFileBytesWithIV.length);
            if (Arrays.equals(originalFileBytes, cipherFileBytes)) {
                System.out.println("Original file is the same as encrypted file. Test failed.");
                return;
            } else {
                System.out.print("Original:  ");
                for (int i = 0; i < originalFileBytes.length; i++) {
                    System.out.print(originalFileBytes[i]);
                }
                System.out.println(" ");
                System.out.print("Encrypted: ");
                for (int i = 0; i < cipherFileBytes.length; i++) {
                    System.out.print(cipherFileBytes[i]);
                }
                System.out.println(" ");
                System.out.println("Encrypted file is different than the original.");
            }
            // compare original with decrypted file
            byte[] decryptedFileBytes = FileUtil.readAllBytes(testFilePath);
            if (Arrays.equals(originalFileBytes, decryptedFileBytes)) {
                System.out.print("Original:  ");
                for (int i = 0; i < originalFileBytes.length; i++) {
                System.out.print(originalFileBytes[i]);
                }
                System.out.println(" ");
                System.out.print("Decrypted: ");
                for (int i = 0; i < decryptedFileBytes.length; i++) {
                    System.out.print(decryptedFileBytes[i]);
                }
                System.out.println(" ");

                System.out.println("Decrypted file is the same as original file.");
            } else {
                System.out.println("Original file is not the same as the decrypted file. Test failed");
                return;
            }
            // remove original file
            if (rename.delete()) {
                System.out.println("Original file successfully deleted.");
            } else {
                System.out.println("Failed to delete original file. Test failed.");
            }
            // remove decrypted file
            if (originalFile.delete()) {
                System.out.println("Original file successfully deleted.");
            } else {
                System.out.println("Failed to delete original file. Test failed.");
            }
            // remove decrypted file
            File encryptedFile = new File(testFilePath + ".aes");
            if (encryptedFile.delete()) {
                System.out.println("Original file successfully deleted.");
            } else {
                System.out.println("Failed to delete original file. Test failed.");
            }
            // remove keystore
            if (storeFile.exists()) {
                if (storeFile.delete()) {
                    System.out.println("Successfully deleted the existing keystore");
                } else {
                    System.out.println("File wasn't deleted. Test failed...");
                    return;
                }
            }
            // remove password from memory
            User.removePassword();
        });
    }
}
