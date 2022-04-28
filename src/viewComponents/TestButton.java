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
                System.out.println("Creating an empty file.");
                createEmptyFile();
                printSuccess("SUCCESS: Empty file created.");
            } catch (Exception e) {
                printFailure(e.toString());
                return;
            }
            // Step 2 write a string to the original file
            try {
                System.out.println("Modifying the file.");
                modifyFile();
                printSuccess("SUCCESS: File successfully edited.");
            } catch (Exception e) {
                printFailure(e.toString());
                return;
            }
            // Step 3 set password to the memory
            System.out.println("Saving password to the memory.");
            PasswordUtil.setPassword(password.toCharArray());
            printSuccess("SUCCESS: Password saved in memory.");
            // Step 4 delete keystore if existed
            try {
                System.out.println("Deleting existing keystore.");
                deleteKeyStore();
                printSuccess("SUCCESS: Keystore either doesn't exist or was successfully deleted.");
            } catch (Exception e) {
                printFailure(e.toString());
                return;
            }
            // Step 5 create keystore with a key
            try {
                System.out.println("Creating new keystore and secure key.");
                createKeyStoreWithKey();
                printSuccess("SUCCESS: KeyStore and a key successfully created.");
            } catch (Exception e) {
                printFailure(e.toString());
                return;
            }
            // Step 6 encrypt original file
            System.out.println("Encrypting.");
            try {
                FileCipher.encrypt(originalFilePath);
                printSuccess("SUCCESS: File encrypted");
            } catch (Exception e) {
                e.printStackTrace();
                printFailure("FAIL: Encryption failed!");
                return;
            }
            // Step 7 rename the original file to test file
            System.out.println("Renaming the original file.");
            if (originalFile.renameTo(renamedOriginalFile)) {
                printSuccess("SUCCESS: Original file renamed.");
            } else {
                printFailure("FAIL: Original file not renamed!");
                return;
            }
            // Step 8 decrypt
            try {
                System.out.println("Decrypting.");
                FileCipher.decrypt(encryptedFilePath);
                printSuccess("SUCCESS: File decrypted");
            } catch (Exception e) {
                e.printStackTrace();
                printFailure("FAIL: Decryption failed!");
                return;
            }
            // Step 9 compare cipher text to original
            try {
                System.out.println("Comparing cipher text with original plaintext.");
                compareEncryptedBytes();
                printSuccess("SUCCESS: Encrypted file doesn't match the original.");
            } catch (Exception e) {
                printFailure(e.toString());
                return;
            }
            // Step 10 compare original with decrypted file
            try {
                System.out.println("Comparing decrypted plaintext with original plaintext.");
                compareDecryptedBytes();
                printSuccess("SUCCESS: Decrypted file is the same as original file.");
            } catch (Exception e) {
                printFailure(e.toString());
            }
            // Step 11 remove original file
            System.out.println("Deleting original file.");
            if (renamedOriginalFile.delete()) {
                printSuccess("SUCCESS: Original file successfully deleted.");
            } else {
                printFailure("FAIL: Original file wasn't deleted!");
            }
            // Step 12 remove decrypted file
            System.out.println("Deleting decrypted file.");
            if (decryptedFile.delete()) {
                printSuccess("SUCCESS: Decrypted file successfully deleted.");
            } else {
                printFailure("FAIL: Decrypted file wasn't deleted!");
            }
            // Step 12 remove encrypted file
            System.out.println("Deleting encrypted file.");
            if (encryptedFile.delete()) {
                printSuccess("SUCCESS: Decrypted file successfully deleted.");
            } else {
                printFailure("FAIL: Decrypted file wasn't deleted!");
            }
            // Step 13 remove keystore
            try {
                System.out.println("Deleting keystore.");
                deleteKeyStore();
                printSuccess("SUCCESS: Keystore was successfully deleted.");
            } catch (Exception e) {
                printFailure(e.toString());
                return;
            }
            // Step 14 remove password from memory
            System.out.println("Deleting password from memory.");
            PasswordUtil.removePassword();
            printSuccess("SUCCESS: Password removed from memory.");
            // test done
            System.out.println("Test finished successfully");
        });
    }

    // print out green success message
    private void printSuccess(String message) {
        String ANSI_GREEN = "\u001B[32m";
        System.out.println(ANSI_GREEN + message + "\u001B[0m");
    }
    // print out red failure message
    private void printFailure(String message) {
        String ANSI_RED = "\u001B[31m";
        System.out.println(ANSI_RED + message + "\u001B[0m");
    }

    private void createEmptyFile() throws Exception {
        try {
            if (!originalFile.createNewFile()) {
                throw new Exception("FAILURE: File already exists!");
            }
        } catch(IOException e) {
            e.printStackTrace();
            throw new Exception("FAILURE: Failed to create a file.");
        }
    }

    private void modifyFile() throws Exception {
        try {
            FileWriter fileWriter = new FileWriter(originalFilePath);
            fileWriter.write("Very secret message...");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("FAILURE: Failed to edit the file.");
        }
    }

    private void compareEncryptedBytes() throws Exception {
        byte[] originalFileBytes = FileUtil.readAllBytes(originalFilePath);
        byte[] encryptedFileBytesWithIV = FileUtil.readAllBytes(encryptedFilePath);
        byte[] encryptedFileBytes = Arrays.copyOfRange(encryptedFileBytesWithIV, 16, encryptedFileBytesWithIV.length);
        if (Arrays.equals(originalFileBytes, encryptedFileBytes)) {
            throw new Exception("FAILURE: Original file matches the encrypted file!");
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
            throw new Exception("FAILURE: Original file is not the same as the decrypted file!");
        }
    }

    private void deleteKeyStore() throws Exception {
        File storeFile = new File(keyStorePath);
        if (storeFile.exists()) {
            if (!storeFile.delete()) {
                throw new Exception("FAILURE: Existing keystore wasn't deleted!");
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
            throw new Exception("FAILURE: New keystore wasn't created.");
        }
    }
}
