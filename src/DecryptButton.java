import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class DecryptButton extends Button {
    DecryptButton() {
        setText("Decrypt");
        FileChooser fileChooser = new FileChooser();
        setOnAction(e -> {
            File file = fileChooser.showOpenDialog(new Stage());
            String fileName = file.getAbsolutePath();
            DecryptFile.decrypt(fileName);
        });
    }
}
