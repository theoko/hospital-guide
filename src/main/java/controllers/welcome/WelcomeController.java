package controllers.welcome;

import controllers.ScreenController;
import helpers.Constants;

public class WelcomeController {

    public void welcomeLogin() throws Exception{
        ScreenController.deactivate();
        ScreenController.activate(Constants.Routes.USER_MAP);
    }

    public void employeeLogin() throws Exception{
        ScreenController.deactivate();
        ScreenController.activate(Constants.Routes.LOGIN);
    }
}
