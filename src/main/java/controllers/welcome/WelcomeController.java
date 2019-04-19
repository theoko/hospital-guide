package controllers.welcome;

import com.jfoenix.controls.JFXButton;
import controllers.ScreenController;
import helpers.Constants;
import javafx.scene.layout.AnchorPane;

public class WelcomeController {

    public void welcomeLogin() throws Exception{
//        ScreenController.deactivate();
        ScreenController.activate(Constants.Routes.USER_MAP);
    }

    public void employeeLogin() throws Exception{
//        ScreenController.deactivate();
        ScreenController.activate(Constants.Routes.LOGIN);
    }
}