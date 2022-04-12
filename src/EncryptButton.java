import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class EncryptButton extends Button {
    EncryptButton() {
        setText("Encrypt");
        FileChooser fileChooser = new FileChooser();
        setOnAction(e -> {
            if (User.getPassword() == null) {
                // we need to show dialog to prompt user to input password
                PasswordInputDialog inputDialog = new PasswordInputDialog("Type in your password");
                inputDialog.showAndWait();
                User.setPassword(inputDialog.getEditor().getText().toCharArray());
            }
            File file = fileChooser.showOpenDialog(new Stage());
            String fileName = file.getAbsolutePath();
            EncryptFile.encrypt(fileName);
        });
    }
}
