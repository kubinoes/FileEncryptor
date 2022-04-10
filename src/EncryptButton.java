import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class EncryptButton extends Button {
    EncryptButton() {
        setText("Encrypt");
        FileChooser fileChooser = new FileChooser();
        setOnAction(e ->{
            File file = fileChooser.showOpenDialog(new Stage());
            String fileName = file.getAbsolutePath();
            EncryptFile.encrypt(fileName);
        });
    }
}
