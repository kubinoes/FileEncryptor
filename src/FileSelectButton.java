import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileSelectButton extends Button {
    FileSelectButton(String dir){
        setText("Press");
        FileChooser fileChooser = new FileChooser();
        setOnAction(e -> {
            File file = fileChooser.showOpenDialog(new Stage());
            String fileName = file.getAbsolutePath();
            EncryptFile.encrypt(fileName);
        });
    }
}
