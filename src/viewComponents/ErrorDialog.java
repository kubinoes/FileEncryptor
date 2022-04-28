package viewComponents;

import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class ErrorDialog extends Dialog<String> {
    public ErrorDialog(String message) {
        // create OK button
        ButtonType buttonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);

        setTitle("Error!");
        // message is passed from parent component
        setContentText(message);
        getDialogPane().getButtonTypes().add(buttonType);
    }
}
