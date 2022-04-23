import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import viewComponents.*;

public class Main extends Application {
    public void start(Stage stage) {
        stage.setTitle("File Encryptor");
        Scene authScene = new Scene(new AuthBox(stage), 300, 200);
        stage.setScene(authScene);
        stage.show();
    }
}
