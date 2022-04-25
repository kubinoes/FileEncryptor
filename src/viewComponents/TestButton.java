package viewComponents;

import javafx.scene.control.Button;
import security.FileCipher;
import utilities.FileUtil;
import utilities.KeyStoreManager;
import utilities.PasswordUtil;

import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.util.Arrays;

public class TestButton extends Button {

    private static final String originalFilePath = System.getProperty("user.home") + "/testFileEncryptor.txt";
    private static final String renamedOriginalFilePath = System.getProperty("user.home") + "/test.testFileEncryptor.txt";
    private static final String encryptedFilePath = originalFilePath + ".aes";
    private static final String keyStorePath = System.getProperty("user.home") + "/FileEncryptor/keystore.bks";

    private static final File originalFile = new File(originalFilePath);
    private static final File renamedOriginalFile = new File(renamedOriginalFilePath);
    private static final File decryptedFile = new File(originalFilePath);
    private static final File encryptedFile = new File(encryptedFilePath);

    private static final String password = "superSecretPassword";

    public TestButton() {
        setText("Run Test");
        setOnAction(a ->{
            System.out.println("Running test.");
            // Step 1 create an empty file in user directory
            try {
                createEmptyFile();
                printSuccess("SUCCESS: Empty file created.");
            } catch (Exception e) {
                printFail(e.toString());
                return;
            }
            // Step 2 write a string to the original file
            try {
                modifyFile();
                printSuccess("SUCCESS: File successfully edited.");
            } catch (Exception e) {
                printFail(e.toString());
                return;
            }
            // Step 3 set password to the memory
            PasswordUtil.setPassword(password.toCharArray());
            printSuccess("SUCCESS: Password saved in memory.");
            // Step 4 delete keystore if existed
            try {
                deleteKeyStore();
                printSuccess("SUCCESS: Keystore either doesn't exist or was successfully deleted.");
            } catch (Exception e) {
                printFail(e.toString());
                return;
            }
            // Step 5 create keystore
            try {
                createKeyStoreWithKey();
                printSuccess("KeyStore and a key successfully created.");
            } catch (Exception e) {
                printFail(e.toString());
                return;
            }
            // Step 6 encrypt original file
            try {
                FileCipher.encrypt(originalFilePath);
                printSuccess("SUCCESS: File encrypted");
            } catch (Exception e) {
                e.printStackTrace();
                printFail("FAIL: Encryption failed!");
                return;
            }
            // Step 7 rename the original file to test file
            if (originalFile.renameTo(renamedOriginalFile)) {
                printSuccess("SUCCESS: Original file renamed.");
            } else {
                printFail("FAIL: Original file not renamed!");
                return;
            }
            // Step 8 decrypt
            try {
                FileCipher.decrypt(encryptedFilePath);
                printSuccess("SUCCESS: File decrypted");
            } catch (Exception e) {
                e.printStackTrace();
                printFail("FAIL: Decryption failed!");
                return;
            }
            // Step 9 compare cipher text to original
            try {
                compareEncryptedBytes();
                printSuccess("SUCCESS: Encrypted file doesn't match the original.");
            } catch (Exception e) {
                printFail(e.toString());
                return;
            }
            // Step 10 compare original with decrypted file
            try {
                compareDecryptedBytes();
                printSuccess("SUCCESS: Decrypted file is the same as original file.");
            } catch (Exception e) {
                printFail(e.toString());
            }
            // Step 11 remove original file
            if (renamedOriginalFile.delete()) {
                printSuccess("SUCCESS: Original file successfully deleted.");
            } else {
                printFail("FAIL: Original file wasn't deleted!");
            }
            // Step 12 remove decrypted file
            if (decryptedFile.delete()) {
                printSuccess("SUCCESS: Decrypted file successfully deleted.");
            } else {
                printFail("FAIL: Decrypted file wasn't deleted!");
            }
            // Step 12 remove decrypted file
            if (encryptedFile.delete()) {
                printSuccess("SUCCESS: Decrypted file successfully deleted.");
            } else {
                printFail("FAIL: Decrypted file wasn't deleted!");
            }
            // Step 13 remove keystore
            try {
                deleteKeyStore();
                printSuccess("SUCCESS: Keystore was successfully deleted.");
            } catch (Exception e) {
                printFail(e.toString());
                return;
            }
            // Step 14 remove password from memory
            PasswordUtil.removePassword();
            printSuccess("SUCCESS: Password removed from memory.");
            // test done
            System.out.println("Test finished successfully");
        });
    }

    private void printSuccess(String message) {
        String ANSI_GREEN = "\u001B[32m";
        System.out.println(ANSI_GREEN + message + "\u001B[0m");
    }
    private void printFail(String message) {
        String ANSI_RED = "\u001B[31m";
        System.out.println(ANSI_RED + message + "\u001B[0m");
    }

    private void createEmptyFile() throws Exception {
        try {
            if (!originalFile.createNewFile()) {
                throw new Exception("FAIL: File already exists!");
            }
        } catch(IOException e) {
            e.printStackTrace();
            throw new Exception("FAIL: Failed to create a file.");
        }
    }

    private void modifyFile() throws Exception {
        try {
            FileWriter fileWriter = new FileWriter(originalFilePath);
            fileWriter.write("Very secret message...");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("Failed to edit the file.");
        }
    }

    private void compareEncryptedBytes() throws Exception {
        byte[] originalFileBytes = FileUtil.readAllBytes(originalFilePath);
        byte[] encryptedFileBytesWithIV = FileUtil.readAllBytes(encryptedFilePath);
        byte[] encryptedFileBytes = Arrays.copyOfRange(encryptedFileBytesWithIV, 16, encryptedFileBytesWithIV.length);
        if (Arrays.equals(originalFileBytes, encryptedFileBytes)) {
            throw new Exception("FAIL: Original file matches the encrypted file!");
        } else {
            System.out.print("Original file bytes:  ");
            for (byte originalFileByte : originalFileBytes) {
                System.out.print(originalFileByte);
            }
            System.out.println(" ");
            System.out.print("Encrypted file bytes: ");
            for (byte cipherFileByte : encryptedFileBytes) {
                System.out.print(cipherFileByte);
            }
            System.out.println(" ");
        }
    }

    private void compareDecryptedBytes() throws Exception {
        byte[] originalFileBytes = FileUtil.readAllBytes(originalFilePath);
        byte[] decryptedFileBytes = FileUtil.readAllBytes(originalFilePath);
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
        } else {
            throw new Exception("FAIL: Original file is not the same as the decrypted file!");
        }
    }

    private void deleteKeyStore() throws Exception {
        File storeFile = new File(keyStorePath);
        if (storeFile.exists()) {
            if (!storeFile.delete()) {
                throw new Exception("FAIL: Existing keystore wasn't deleted!");
            }
        }
    }

    private void createKeyStoreWithKey() throws Exception {
        try {
            KeyStore keyStore = KeyStoreManager.createKeyStore();
            KeyStoreManager.generateAndAddKey(keyStore);
            KeyStoreManager.storeKeyStore(keyStore);
            printSuccess("SUCCESS: New keystore created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("FAIL: New keystore wasn't created.");
        }
    }
}
