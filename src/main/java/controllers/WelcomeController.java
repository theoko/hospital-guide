package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController {

    public void welcomeLogin() throws Exception{
        ScreenController.deactivate();
        ScreenController.activate("regMain");
    }

    public void employeeLogin() throws Exception{
        ScreenController.deactivate();
        ScreenController.activate("login");
    }
}
