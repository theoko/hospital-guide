package controllers.auth;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import controllers.ScreenController;
import database.UserTable;
import helpers.Constants;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import models.user.User;

public class AuthController {
    public JFXTextField emailField;
    public JFXPasswordField passwordField;
    public Label errorMessage;

    public static User currentUser;
    public JFXToggleButton togScanner;
    public JFXPasswordField cardPassword;
    public Label passText;
    public Label emailText;
    public Label cardScanText;

    private int currentlyAuthenticatedUsers = 0;

    private Constants.Auth authenticate(String username, String password) {

        String TEMP_ADMIN_USERNAME = "root";
        String TEMP_ADMIN_PASSWORD = "";
        String TEMP_EMPLOYEE_USERNAME = "employee";
        String TEMP_EMPLOYEE_PASSWORD = "";
        String TEMP_CUSTODIAN_USERNAME = "custodian";
        String TEMP_CUSTODIAN_PASSWORD = "";

        String STAFF_EMPLOYEE = "staff-employee";
        String STAFF_EMPLOYEE_PASSWORD = "staff";
        String STAFF_ADMIN = "staff-admin";
        String STAFF_ADMIN_PASSWORD = "staff";
        String STAFF_CUSTODIAN = "staff-custodian";
        String STAFF_CUSTODIAN_PASSWORD = "staff";

        String TEMP_USER_USERNAME = "user";
        String TEMP_USER_PASSWORD = "";


        // Fixed users
        if (username.equals(TEMP_ADMIN_USERNAME) && password.equals(TEMP_ADMIN_PASSWORD)) {
            return Constants.Auth.ADMIN;
        } else if (username.equals(TEMP_EMPLOYEE_USERNAME) && password.equals(TEMP_EMPLOYEE_PASSWORD)) {
            return Constants.Auth.EMPLOYEE;
        } else if (username.equals(TEMP_CUSTODIAN_USERNAME) && password.equals(TEMP_CUSTODIAN_PASSWORD)) {
            return Constants.Auth.CUSTODIAN;
        } else if (username.equals(TEMP_USER_USERNAME) && password.equals(TEMP_USER_PASSWORD)) {
            return Constants.Auth.USER;
        } else if (username.equals(STAFF_EMPLOYEE) && password.equals(STAFF_EMPLOYEE_PASSWORD)) {
            return Constants.Auth.EMPLOYEE;
        } else if (username.equals(STAFF_ADMIN) && password.equals(STAFF_ADMIN_PASSWORD)) {
            return Constants.Auth.ADMIN;
        } else if (username.equals(STAFF_CUSTODIAN) && password.equals(STAFF_CUSTODIAN_PASSWORD)) {
            return Constants.Auth.CUSTODIAN;
        }

        // Created users
        User authUser = UserTable.getUserByUsername(username);

        if(authUser != null) {
            if(password.equals(authUser.getPassword())) {
                return authUser.getUserType();
            } else {
                return null;
            }
        }

        return null;

    }

    public void handleLogin(ActionEvent actionEvent) throws Exception {
        Constants.Auth authType;
        System.out.println(togScanner.isSelected());
        if (!togScanner.isSelected()) {
            authType = authenticate(emailField.getText(), passwordField.getText());
        } else {
            authType = authenticate(cardPassword.getText(), "");
        }

        if(authType == Constants.Auth.ADMIN) {

            createUserOrFail(authType);

            ScreenController.activate(Constants.Routes.ADMIN_MAP);
        } else if (authType == Constants.Auth.EMPLOYEE) {

            createUserOrFail(authType);

            ScreenController.activate(Constants.Routes.EMPLOYEE_MAP);
        } else if (authType == Constants.Auth.CUSTODIAN) {

            createUserOrFail(authType);

            ScreenController.activate(Constants.Routes.CUSTODIAN_MAP);

        } else if (authType == Constants.Auth.USER) {

            currentUser = new User(currentlyAuthenticatedUsers, "user", "", authType);

            currentlyAuthenticatedUsers++;

            currentUser.create();

            currentUser.setUserID(UserTable.getUserByUsername(currentUser.getUsername()).getUserID());

        } else {
            errorMessage.setText("Invalid credentials");
            errorMessage.setManaged(true);
            errorMessage.setVisible(true);
        }

    }

    private void createUserOrFail(Constants.Auth authType) {

        errorMessage.setVisible(false);
        errorMessage.setManaged(false);

        currentUser = new User(currentlyAuthenticatedUsers, emailField.getText(), passwordField.getText(), authType);

        currentlyAuthenticatedUsers++;

        currentUser.create();

        currentUser.setUserID(UserTable.getUserByUsername(currentUser.getUsername()).getUserID());


//        ScreenController.deactivate();

    }

    public void goBack(ActionEvent actionEvent) throws Exception {
        actionEvent.consume();
//        ScreenController.deactivate();
        ScreenController.activate(Constants.Routes.WELCOME);
    }

    public void cardScanner(ActionEvent actionEvent) {
        actionEvent.consume();
        if(togScanner.isSelected()){
            cardPassword.setVisible(true);
            emailField.setVisible(false);
            passwordField.setVisible(false);
            passText.setVisible(false);
            cardScanText.setVisible(true);
            emailText.setVisible(false);
            cardPassword.requestFocus();

        } else{
            cardPassword.setVisible(false);
            emailField.setVisible(true);
            passwordField.setVisible(true);
            passText.setVisible(true);
            cardScanText.setVisible(false);
            emailText.setVisible(true);
            emailField.requestFocus();
        }
    }
}
