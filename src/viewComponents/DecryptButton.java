package viewComponents;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import security.FileCipher;
import utilities.User;

import java.io.File;
import java.util.List;

public class DecryptButton extends Button {

    public DecryptButton() {
        setText("Decrypt");
        FileChooser fileChooser = new FileChooser();
        // filter only files ending with .aes
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All files", "*.aes"));
        setOnAction(e -> {
            Stage stage = (Stage) getScene().getWindow();
            if (User.getPassword() == null) {
                // navigate to auth window
                stage.setScene(new Scene(new AuthBox(stage), 300, 200));
            }
            List<File> files = fileChooser.showOpenMultipleDialog(new Stage());
            for (File file : files) {
                String fileName = file.getAbsolutePath();
                try {
                    FileCipher.decrypt(fileName);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    if (e1.toString().equals("java.lang.Exception: Keystore missing")) {
                        stage.setScene(new Scene(new AuthBox(stage), 300, 200));
                    } else if (e1.toString().equals("java.lang.Exception: Corrupted file, failed to decrypt!")) {
                        new ErrorDialog("File couldn't be decrypted because it was tampered with!").showAndWait();
                    }
                    e1.printStackTrace();
                }
            }
        });
    }
}
