package viewComponents;

import javafx.scene.control.Button;
import utilities.FileUtil;
import utilities.KeyStoreManager;

import java.io.File;
import java.security.KeyStore;

public class SignupButton extends Button {
    public SignupButton() {
        setText("Create your key");
        setOnAction(e ->{
            // check for keystore.bks file in home directory
            File keyStoreFile = new File(KeyStoreManager.storeFilePath);
            if (keyStoreFile.exists()){
                // TODO display a message that user already created a password and needs to login
                return;
            } else {
                // create a folder
                FileUtil.createSupportDirectory();
                PasswordInputDialog inputDialog = new PasswordInputDialog("Create your password");
                inputDialog.showAndWait();
                char[] pswInput = inputDialog.getEditor().getText().toCharArray();
                User.setPassword(pswInput);
                KeyStore keyStore = KeyStoreManager.createKeyStore();
                KeyStoreManager.generateAndAddKey(keyStore);
                KeyStoreManager.storeKeyStore(keyStore);
            }
        });
    }
}
