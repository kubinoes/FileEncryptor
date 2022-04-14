import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;


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
    public static void write(String plaintextFileName, byte[] output) {
        // write out
        try {
            Files.write(Paths.get(plaintextFileName), output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void write(String transformation, String plaintextFileName, byte[] output) {
        // if transformation is "AES/ECB/.../ this indicates file to be read is encrypted
        // then add suffix "aes"
        String outFile = "";
        String[] parts = transformation.split("/");
        // TODO else to throw an error
        if (parts.length == 3 && parts[0].equals("AES")){
            outFile = plaintextFileName + ".aes";
            System.out.println(outFile);
        } else {

        }
        try {
            Files.write(Paths.get(outFile), output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
