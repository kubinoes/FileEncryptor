import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SelectFileForEncryption extends Application {
    public void start(Stage stage) {
        // main scene buttons
        EncryptButton encryptButton = new EncryptButton();
        DecryptButton decryptButton = new DecryptButton();
        Button logoutButton = new Button("Log out");

        HBox hBox = new HBox(20, encryptButton, decryptButton, logoutButton);
        hBox.setAlignment(Pos.CENTER);
        Scene mainScene = new Scene(hBox, 300, 200);

        // too many attempts scene
        Label label = new Label("You have attempted to guess the password too many times, restart the application and try again");
        label.setAlignment(Pos.CENTER);
        Scene failScene = new Scene(label, 700, 100);

        LoginButton loginButton = new LoginButton(stage, mainScene, failScene);
        SignupButton signupButton = new SignupButton();
        HBox authBox = new HBox(20, loginButton, signupButton);
        authBox.setAlignment(Pos.CENTER);

        stage.setTitle("File Encryptor");
        Scene authScene = new Scene(authBox, 300, 200);
        stage.setScene(authScene);

        logoutButton.setOnAction(e -> {
            stage.setScene(authScene);
        });
        stage.show();
    }
}
