import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SelectFileForEncryption extends Application {
    private static final ThreadLocal<char[]> password = new ThreadLocal<>();
    public void start(Stage stage) {
        // buttons
        EncryptButton encryptButton = new EncryptButton();
        DecryptButton decryptButton = new DecryptButton();

        // container for buttons
        HBox hBox = new HBox(encryptButton, decryptButton);
        hBox.setAlignment(Pos.CENTER);

        stage.setTitle("File Encryptor");
        Scene scene = new Scene(hBox, 300, 200);
        stage.setScene(scene);
        stage.show();
    }
}
