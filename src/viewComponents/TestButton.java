package viewComponents;

import javafx.scene.control.Button;
import security.FileCipher;
import utilities.FileUtil;
import utilities.KeyStoreManager;

import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.util.Arrays;

public class TestButton extends Button {
    private void printSuccess(String message) {
        String ANSI_GREEN = "\u001B[32m";
        System.out.println(ANSI_GREEN + message + "\u001B[0m");
    }
    private void printFail(String message) {
        String ANSI_RED = "\u001B[31m";
        System.out.println(ANSI_RED + message + "\u001B[0m");
    }
    public TestButton() {
        setText("Run Test");
        setOnAction(a ->{
            // Step 1 create a file in user directory
            String testFilePath = System.getProperty("user.home") + "/testFileEncryptor.txt";
            try {
                File fileToEncrypt = new File(testFilePath);
                if (fileToEncrypt.createNewFile()) {
                    printSuccess("1/15--->SUCCESS: Empty file created.");
                } else {
                    printFail("1/15--->FAIL: File already exists!");
                    return;
                }
            } catch(IOException e) {
                printFail("1/15--->FAIL:");
                e.printStackTrace();
                return;
            }
            // Step 2 write a string to the test file
            try {
                FileWriter fileWriter = new FileWriter(testFilePath);
                fileWriter.write("Very secret message");
                fileWriter.close();
                printSuccess("2/15--->SUCCESS: File edited.");
            } catch (IOException e) {
                printFail("2/15--->FAIL:");
                e.printStackTrace();
                return;
            }
            // Step 3 set password to the memory
            String password = "superSecretPassword";
            User.setPassword(password.toCharArray());
            printSuccess("3/15--->SUCCESS: Password saved in memory.");
            // Step 4 delete existing keystore
            File storeFile = new File(System.getProperty("user.home") + "/FileEncryptor/keystore.bks");
            if (storeFile.exists()) {
                if (storeFile.delete()) {
                    printSuccess("4/15--->SUCCESS: The existing keystore deleted.");
                } else {
                    printFail("4/15--->FAIL: Existing keystore wasn't deleted!");
                    return;
                }
            } else {
                printSuccess("4/15--->SUCCESS: Keystore doesn't exist.");
            }
            // Step 5 create test keystore
            try {
                KeyStore keyStore = KeyStoreManager.createKeyStore();
                KeyStoreManager.generateAndAddKey(keyStore);
                KeyStoreManager.storeKeyStore(keyStore);
                printSuccess("5/15--->SUCCESS: New keystore created successfully.");
            } catch (Exception e) {
                printFail("5/15--->FAIL:");
                e.printStackTrace();
                return;
            }
            // Step 6 encrypt it
            FileCipher.encrypt(testFilePath);
            printSuccess("6/15--->SUCCESS: File encrypted");
            // Step 7 rename the original file to test file
            File originalFile = new File(testFilePath);
            File rename = new File(System.getProperty("user.home") + "/test.testFileEncryptor.txt");
            if (originalFile.renameTo(rename)) {
                printSuccess("7/15--->SUCCESS: Original file renamed.");
            } else {
                printFail("7/15--->FAIL: Original file not renamed!");
                return;
            }
            // Step 8 decrypt
            FileCipher.decrypt(testFilePath + ".aes");
            printSuccess("8/15--->SUCCESS: File decrypted");
            // Step 9 compare cipher text to original
            byte[] originalFileBytes = FileUtil.readAllBytes(System.getProperty("user.home") + "/test.testFileEncryptor.txt");
            byte[] cipherFileBytesWithIV = FileUtil.readAllBytes(testFilePath + ".aes");
            byte[] cipherFileBytes = Arrays.copyOfRange(cipherFileBytesWithIV, 16, cipherFileBytesWithIV.length);
            if (Arrays.equals(originalFileBytes, cipherFileBytes)) {
                printFail("9/15--->FAIL: Original file matches the encrypted file!");
                return;
            } else {
                System.out.print("Original file bytes:  ");
                for (byte originalFileByte : originalFileBytes) {
                    System.out.print(originalFileByte);
                }
                System.out.println(" ");
                System.out.print("Encrypted file bytes: ");
                for (byte cipherFileByte : cipherFileBytes) {
                    System.out.print(cipherFileByte);
                }
                System.out.println(" ");
                printSuccess("9/15--->SUCCESS: Encrypted file doesn't match the original.");
            }
            // Step 10 compare original with decrypted file
            byte[] decryptedFileBytes = FileUtil.readAllBytes(testFilePath);
            if (Arrays.equals(originalFileBytes, decryptedFileBytes)) {
                System.out.print("Original:  ");
                for (byte originalFileByte : originalFileBytes) {
                    System.out.print(originalFileByte);
                }
                System.out.println(" ");
                System.out.print("Decrypted: ");
                for (byte decryptedFileByte : decryptedFileBytes) {
                    System.out.print(decryptedFileByte);
                }
                System.out.println(" ");

                printSuccess("11/15--->SUCCESS: Decrypted file is the same as original file.");
            } else {
                printFail("10/15--->FAIL: Original file is not the same as the decrypted file!");
                return;
            }
            // Step 11 remove original file
            if (rename.delete()) {
                printSuccess("11/15--->SUCCESS: Original file successfully deleted.");
            } else {
                printFail("11/15--->FAIL: Original file wasn't deleted!");
            }
            // Step 12 remove decrypted file
            if (originalFile.delete()) {
                printSuccess("12/15--->SUCCESS: Decrypted file successfully deleted.");
            } else {
                printFail("12/15--->FAIL: Decrypted file wasn't deleted!");
            }
            // Step 12 remove decrypted file
            File encryptedFile = new File(testFilePath + ".aes");
            if (encryptedFile.delete()) {
                printSuccess("13/15--->SUCCESS: Decrypted file successfully deleted.");
            } else {
                printFail("13/15--->FAIL: Decrypted file wasn't deleted!");
            }
            // Step 13 remove keystore
            if (storeFile.exists()) {
                if (storeFile.delete()) {
                    printSuccess("14/15--->SUCCESS: Test keystore deleted.");
                } else {
                    printFail("14/15--->FAIL: Test keystore wasn't deleted!");
                    return;
                }
            }
            // Step 14 remove password from memory
            User.removePassword();
            printSuccess("15/15--->SUCCESS: Password removed from memory.");
        });
    }
}
