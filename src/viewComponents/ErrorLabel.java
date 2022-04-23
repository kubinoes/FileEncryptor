package viewComponents;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class ErrorLabel extends Label {
    public ErrorLabel(String message) {
        setText(message);
        setAlignment(Pos.CENTER);
        setTextFill(Color.web("#FF0000"));
    }
}
