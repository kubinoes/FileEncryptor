package viewComponents;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.util.Pair;
import utilities.FileUtil;
import utilities.KeyStoreManager;
import utilities.PasswordUtil;

import java.io.File;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.Optional;

public class SignupButton extends Button {
    public SignupButton(Stage stage) {
        setText("Create your key");
        setOnAction(e ->{
            // check for keystore.bks file in home directory
            File keyStoreFile = new File(KeyStoreManager.storeFilePath);
            if (keyStoreFile.exists()){
                new ErrorDialog("You already have a secure key on your machine. Click on 'Login' to enter your password that protects your key.").showAndWait();
            } else {
                // create a folder
                FileUtil.createSupportDirectory();
                SignupDialog signupDialog = new SignupDialog();
                Optional<Pair<char[], char[]>> result = signupDialog.showAndWait();
                result.ifPresent(password -> {
                    // validate password
                    char[] psw = password.getKey();
                    char[] confirmPsw = password.getValue();

                    if (Arrays.equals(psw, confirmPsw)){
                        PasswordUtil.setPassword(psw);
                        KeyStore keyStore = KeyStoreManager.createKeyStore();
                        KeyStoreManager.generateAndAddKey(keyStore);
                        KeyStoreManager.storeKeyStore(keyStore);
                        stage.setScene(new Scene(new CipherBox(stage), 300, 200));
                    } else {
                        new ErrorDialog("Passwords do not match, try again.").showAndWait();
                    }
                });
            }
        });
    }
}
