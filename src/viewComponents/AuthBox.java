package viewComponents;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class AuthBox extends HBox {
    public AuthBox(Stage stage) {
        LoginButton loginButton = new LoginButton(stage);
        SignupButton signupButton = new SignupButton();
        TestButton testButton = new TestButton();
        getChildren().addAll(loginButton, signupButton, testButton);
        setSpacing(20);
        setAlignment(Pos.CENTER);
    }
}
