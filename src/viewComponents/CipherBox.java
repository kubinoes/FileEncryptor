package viewComponents;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class CipherBox extends HBox {
    public CipherBox(Stage stage) {
        // add buttons
        EncryptButton encryptButton = new EncryptButton();
        DecryptButton decryptButton = new DecryptButton();
        Button logoutButton = new Button("Log out");

        getChildren().addAll(encryptButton, decryptButton, logoutButton);

        // navigate to authentication window on logout button press
        logoutButton.setOnAction(e -> {
            stage.setScene(new Scene(new AuthBox(stage), 300, 200));
        });

        setSpacing(20);
        setAlignment(Pos.CENTER);
    }
}
