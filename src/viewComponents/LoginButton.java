package viewComponents;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utilities.KeyStoreManager;
import utilities.PasswordUtil;

import java.io.File;
import java.util.Optional;

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
                LoginDialog inputDialog = new LoginDialog();
                Optional<char[]> result = inputDialog.showAndWait();
                result.ifPresent(password -> {
                    //char[] pswInput = inputDialog.getEditor().getText().toCharArray();
                    if (PasswordUtil.verifyPassword(password)) {
                        // change scene to display encrypt and decrypt buttons
                        PasswordUtil.setPassword(password);
                        stage.setScene(new Scene(new CipherBox(stage), 300, 200));
                    } else {
                        // let user have 3 password attempts per session
                        counter -= 1;
                        if (counter == 0) {
                            stage.setScene(new Scene(new ErrorLabel("You have attempted to guess the password too many times, restart the application and try again!"), 700, 100));
                        }
                        new ErrorDialog("Provided password is not correct!\nTry again.\nYou have only " + counter + " attempts left!").showAndWait();
                    }
                });

            }
        });
    }
}
