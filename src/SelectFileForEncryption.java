import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SelectFileForEncryption extends Application {

    public void start(Stage stage) {
        stage.setTitle("Select a file for encryption");
        Label label = new Label("Press to select file:");
        EncryptButton encryptButton = new EncryptButton();
        DecryptButton decryptButton = new DecryptButton();
        HBox hBox = new HBox(encryptButton, decryptButton);
        hBox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(hBox, 250, 100);
        stage.setScene(scene);
        stage.show();
    }
}
