import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;

public class LoginButton extends Button {
    private static String dir = "/Users/jakub/Desktop";
    private static String storeFileName = dir + "/" + "keystore.bks";
    private Integer counter = 3;

    LoginButton(Stage stage, Scene mainScene, Scene failScene) {
        setText("Login");
        setOnAction(e -> {
            // check for keystore.bks file
            File keyStoreFile = new File(storeFileName);
            if (!keyStoreFile.exists()) {
                // TODO display a message that user needs to create their secure key in order to proceed as it doesn't appear to be saved on their machine
                return;
            } else {
                PasswordInputDialog inputDialog = new PasswordInputDialog("Type in your password to start encrypting or decrypting your files.");
                inputDialog.showAndWait();
                char[] pswInput = inputDialog.getEditor().getText().toCharArray();
                System.out.println(new String(pswInput));
                if (User.verifyPassword(pswInput)) {
                    // change scene to display encrypt and decrypt buttons
                    User.setPassword(pswInput);
                    stage.setScene(mainScene);
                } else {
                    // let user have 3 password attempts per session
                    counter -= 1;
                    if (counter == 0) {
                        stage.setScene(failScene);
                    }
                }
            }
        });
    }
}
