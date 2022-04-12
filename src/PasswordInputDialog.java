import javafx.scene.control.TextInputDialog;

public class PasswordInputDialog extends TextInputDialog {
    // dialog for password input
    PasswordInputDialog(String headerText) {
        setContentText("Password: ");
        setHeaderText(headerText);
    }
}
