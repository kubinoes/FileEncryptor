package viewComponents;

import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import security.FileCipher;

import java.io.File;
import java.util.List;

public class DecryptButton extends Button {
    public DecryptButton() {
        setText("Decrypt");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All files", "*.aes"));
        setOnAction(e -> {
            if (User.getPassword() == null) {
                // prompt user for password
                PasswordInputDialog inputDialog = new PasswordInputDialog("Type in your password");
                inputDialog.showAndWait();
                User.setPassword(inputDialog.getEditor().getText().toCharArray());
            }
            List<File> files = fileChooser.showOpenMultipleDialog(new Stage());
            for (int i = 0; i < files.size(); i++) {
                String fileName = files.get(i).getAbsolutePath();
                FileCipher.decrypt(fileName);
            }
        });
    }
}
