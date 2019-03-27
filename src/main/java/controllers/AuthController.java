package controllers;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AuthController {

    @FXML
    JFXTextField emailField;

    @FXML
    JFXPasswordField passwordField;

    @FXML
    Label errorMessage;


    final String TEMP_USERNAME = "root";
    final String TEMP_PASSWORD = "";

    private boolean authenticate(String username, String password) {
        return username.equals(TEMP_USERNAME) && password.equals(TEMP_PASSWORD);
    }

    public void handleLogin(ActionEvent actionEvent) throws Exception {

        // Button was clicked, check credentials
        // Username: emailField.getText()
        // Password: passwordField.getText()
        if(authenticate(emailField.getText(), passwordField.getText())) {
            errorMessage.setVisible(false);
            errorMessage.setManaged(false);

            // Hide window
            ScreenController.deactivate();

            // Move to another scene
            ScreenController.moveTo("main");

        } else {
            errorMessage.setText("Invalid credentials");
            errorMessage.setManaged(true);
            errorMessage.setVisible(true);
        }

    }

    public void goBack(ActionEvent actionEvent) throws Exception {
        actionEvent.consume();
        ScreenController.deactivate();
        ScreenController.moveTo("welcome");
    }

}
