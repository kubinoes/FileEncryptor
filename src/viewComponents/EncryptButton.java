package viewComponents;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import security.FileCipher;
import utilities.PasswordUtil;

import java.io.File;
import java.util.List;

public class EncryptButton extends Button {
    public EncryptButton() {
        setText("Encrypt");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
        setOnAction(e -> {
            Stage stage = (Stage) getScene().getWindow();
            if (PasswordUtil.getPassword() == null) {
                // navigate to auth window
                stage.setScene(new Scene(new AuthBox(stage), 300, 200));
            }
            List<File> files = fileChooser.showOpenMultipleDialog(new Stage());
            for (File file : files) {
                String fileName = file.getAbsolutePath();
                try {
                    FileCipher.encrypt(fileName);
                } catch (Exception e1) {
                    if (e1.toString().equals("java.lang.Exception: Keystore missing")) {
                        stage.setScene(new Scene(new AuthBox(stage), 300, 200));
                    }
                    e1.printStackTrace();
                }
            }
        });
    }
}
