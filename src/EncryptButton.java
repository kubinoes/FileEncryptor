import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class EncryptButton extends Button {
    EncryptButton() {
        setText("Encrypt");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
        setOnAction(e -> {
            if (User.getPassword() == null) {
                // we need to show dialog to prompt user to input password
                PasswordInputDialog inputDialog = new PasswordInputDialog("Type in your password");
                inputDialog.showAndWait();
                User.setPassword(inputDialog.getEditor().getText().toCharArray());
            }
            List<File> files = fileChooser.showOpenMultipleDialog(new Stage());
            for (int i = 0; i < files.size(); i++) {
                String fileName = files.get(i).getAbsolutePath();
                EncryptFile.encrypt(fileName);
            }
        });
    }
}
