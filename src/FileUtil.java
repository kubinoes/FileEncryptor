import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class FileUtil {
    // create a FileEncryptor folder in users home directory
    public static void createSupportDirectory() {
        try {
            File file = new File(System.getProperty("user.home") + "/FileEncryptor");
            // if the directory doesn't exist already it will create it
            file.mkdir();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] readAllBytes(String plaintextFileName) {
        byte[] bytesRead = {};
        try {
            bytesRead = Files.readAllBytes(Paths.get(plaintextFileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytesRead; // returns {} if file does not exist
    }

    public static void write(String filePathName, byte[] output) {
        String outFile = "";
        String[] parts = filePathName.split("\\.");
        // if the file doesn't end with .aes, append it because the file to be written is encrypted
        if (!parts[parts.length - 1].equals("aes")){
            outFile = filePathName + ".aes";
        } else {
            String[] outFileArray = Arrays.copyOfRange(parts, 0, parts.length - 1);
            outFile = String.join(".", outFileArray);
        }
        try {
            Files.write(Paths.get(outFile), output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
