package controllers.welcome;

import com.jfoenix.controls.JFXButton;
import controllers.ScreenController;
import helpers.Constants;
import helpers.UIHelpers;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {

    public JFXButton getStButton, loginButton;

    public void welcomeLogin() throws Exception{
//        ScreenController.deactivate();
        ScreenController.activate(Constants.Routes.USER_MAP);
    }

    public void employeeLogin() throws Exception{
//        ScreenController.deactivate();
        ScreenController.activate(Constants.Routes.LOGIN);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UIHelpers.mouseHover(getStButton);
        UIHelpers.mouseHover(loginButton);
    }
}
