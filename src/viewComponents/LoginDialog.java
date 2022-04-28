/* Login dialog with password input field
 *  Inspired and adapted from: https://code.makery.ch/blog/javafx-dialogs-official/
 * */

package viewComponents;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class LoginDialog extends Dialog<char[]> {
    // dialog for password input
    public LoginDialog() {
        // create buttons for dialog
        ButtonType loginButtonType = new ButtonType("Login", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        setTitle("Login");
        setHeaderText("Type in your password to start encrypting or decrypting your files.");

        // label and field
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        gridPane.add(new Label("Password:"), 0, 0);
        gridPane.add(password, 1, 0);

        // add gridPane to dialogPane
        getDialogPane().setContent(gridPane);

        // focus password field
        Platform.runLater(() -> password.requestFocus());

        // disable login button
        Node loginButton = getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // listen for changes in password
        password.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            // enable login button if password field is not empty, disable if empty
            loginButton.setDisable(newValue.trim().isEmpty());
        } ));

        // convert the result to char[]
        setResultConverter(dialogButton ->{
            if (dialogButton == loginButtonType) {
                return password.getText().toCharArray();
            }
            return null;
        });
    }
}
