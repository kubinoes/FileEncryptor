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
                // show login input dialog
                LoginDialog inputDialog = new LoginDialog();

                Optional<char[]> result = inputDialog.showAndWait();
                result.ifPresent(password -> {
                    // verify password
                    if (PasswordUtil.verifyPassword(password)) {
                        // change scene to display cipherBox with encrypt and decrypt buttons
                        PasswordUtil.setPassword(password);
                        stage.setScene(new Scene(new CipherBox(stage), 300, 200));
                    } else {
                        // let user have 3 password attempts per session
                        counter -= 1;
                        if (counter == 0) {
                            stage.setScene(new Scene(new ErrorLabel("You have attempted to guess the password too many times,\nrestart the application and try again!"), 500, 200));
                        }
                        // show how many attempts are still available
                        new ErrorDialog("Provided password is not correct!\nTry again.\nYou have only " + counter + " attempts left!").showAndWait();
                    }
                });
            }
        });
    }
}
