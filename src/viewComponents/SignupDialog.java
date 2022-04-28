/* Signup dialog with multiple input fields
*  Inspired and adapted from: https://code.makery.ch/blog/javafx-dialogs-official/
* */

package viewComponents;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

public class SignupDialog extends Dialog<Pair<char[],char[]>> {
    public SignupDialog() {
        // create buttons for dialog
        ButtonType signupButtonType = new ButtonType("Create the key", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(signupButtonType, ButtonType.CANCEL);

        setTitle("Create your secure key.");
        setHeaderText("Enter your new password and confirm it to create a secure key for your encryption.");

        // create grid
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        // password fields and labels
        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setDisable(true);
        confirmPassword.setPromptText("Confirm Password");

        Label passwordLabel = new Label("Password:");
        Label confirmPasswordLabel = new Label("Confirm Password:");

        // add labels and fields to the grid
        gridPane.add(passwordLabel, 0, 0);
        gridPane.add(password, 1, 0);
        gridPane.add(confirmPasswordLabel, 0, 1);
        gridPane.add(confirmPassword, 1, 1);

        // add gridPane to the dialogPane
        getDialogPane().setContent(gridPane);

        // focus on first field
        Platform.runLater(() -> password.requestFocus());

        // disable signup button
        Node signupButton = getDialogPane().lookupButton(signupButtonType);
        signupButton.setDisable(true);

        // listen for changes in password
        password.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            // enable confirm password field if password is not empty, disable if empty
            confirmPassword.setDisable(newValue.trim().isEmpty());
            if (newValue.trim().isEmpty()) {
                signupButton.setDisable(true);
            }
        }));
        // listen for changes in confirm password
        confirmPassword.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            // enable signup button if confirm password field is not empty, disable if empty
            signupButton.setDisable(newValue.trim().isEmpty());
        }));

        // convert the result to Pair<String, String>
        setResultConverter(dialogButton ->{
            if (dialogButton == signupButtonType) {
                return new Pair<>(password.getText().toCharArray(), confirmPassword.getText().toCharArray());
            }
            return null;
        });
    }
}
