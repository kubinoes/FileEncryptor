import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtil {
    public static byte[] readAllBytes(String plaintextFileName) {
        byte[] bytesRead = {};
        try {
            bytesRead = Files.readAllBytes(Paths.get(plaintextFileName));
        } catch (Exception e) {
        }
        return bytesRead; // returns {} if file does not exist
    }
    public static byte[] readAllBytes(String transformation, String filePath) {
        // if transformation is "AES/ECB/.../ this indicates file to be read is encrypted
        // look for at file with suffix "aes"
        byte[] bytesRead = {};
        String[] parts = transformation.split("/");
        // TODO else to throw an error since the file is not encrypted
        if (parts.length == 3 && parts[0].equals("AES")) {
            try {
                bytesRead = Files.readAllBytes(Paths.get(filePath));
            } catch (Exception e) {
            }
        }
        return bytesRead;
    }
    public static void write(String plaintextFileName, byte[] output) {
        // write out
        try {
            Files.write(Paths.get(plaintextFileName), output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void write(String transformation, String plaintextFileName, byte[] output, String ivString) {
        // if transformation is "AES/ECB/.../ this indicates file to be read is encrypted
        // then add suffix "aes"
        String outFile = "";
        String[] parts = transformation.split("/");
        // TODO else to throw an error
        if (parts.length == 3 && parts[0].equals("AES")){
            outFile = plaintextFileName + "." + ivString + ".aes";
            System.out.println(outFile);
        } else {

        }
        try {
            Files.write(Paths.get(outFile), output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getIV(String transformation, String filePath) {
        String[] partsTransformation = transformation.split("/");

        String[] splitPathResult = filePath.split("/");
        String[] splitFileName = splitPathResult[splitPathResult.length - 1].split("\\.");

        String iv = "";

        if (partsTransformation.length == 3 && partsTransformation[0].equals("AES") && splitFileName.length == 4) {
            System.out.println(splitFileName[2]);
            iv = splitFileName[2];
        }

        return iv;
    }
}
