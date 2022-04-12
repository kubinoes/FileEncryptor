import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class DecryptButton extends Button {
    DecryptButton() {
        setText("Decrypt");
        FileChooser fileChooser = new FileChooser();
        setOnAction(e -> {
            if (KeyStoreManager.getPassword() == null) {
                // prompt user for password
                PasswordInputDialog inputDialog = new PasswordInputDialog("Type in your password");
                inputDialog.showAndWait();
                KeyStoreManager.setPassword(inputDialog.getEditor().getText().toCharArray());
            }
            File file = fileChooser.showOpenDialog(new Stage());
            String fileName = file.getAbsolutePath();
            DecryptFile.decrypt(fileName);
        });
    }
}
