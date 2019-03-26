package controllers;

public class WelcomeController {

    public void welcomeLogin() throws Exception{
        ScreenController.deactivate();
        ScreenController.moveTo("login");
    }
}
