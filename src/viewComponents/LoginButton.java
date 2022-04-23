package viewComponents;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utilities.KeyStoreManager;

import java.io.File;

public class LoginButton extends Button {
    private Integer counter = 3;

    public LoginButton(Stage stage) {
        setText("Login");
        setOnAction(e -> {
            // check for keystore.bks file
            File keyStoreFile = new File(KeyStoreManager.storeFilePath);
            if (!keyStoreFile.exists()) {
                new ErrorDialog("You do not have a secure key for encryption saved on your machine, please create your key first.").showAndWait();
            } else {
                PasswordInputDialog inputDialog = new PasswordInputDialog("Type in your password to start encrypting or decrypting your files.");
                inputDialog.showAndWait();
                char[] pswInput = inputDialog.getEditor().getText().toCharArray();
                if (User.verifyPassword(pswInput)) {
                    // change scene to display encrypt and decrypt buttons
                    User.setPassword(pswInput);
                    stage.setScene(new Scene(new CipherBox(stage), 300, 200));
                } else {
                    // let user have 3 password attempts per session
                    counter -= 1;
                    if (counter == 0) {
                        stage.setScene(new Scene(new ErrorLabel("You have attempted to guess the password too many times, restart the application and try again!"), 700, 100));
                    }
                }
            }
        });
    }
}
