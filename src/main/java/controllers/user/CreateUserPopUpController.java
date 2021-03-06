package controllers.user;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controllers.ScreenController;
import database.UserTable;
import helpers.Constants;
import javafx.scene.input.MouseEvent;
import models.user.User;

public class CreateUserPopUpController {

    public JFXComboBox cmbUserType;

    public String EMPLOYEE;
    public String ADMIN;
    public String CUSTODIAN;

    public JFXTextField UserID;
    public JFXTextField UserName;
    public JFXTextField Password;

    public void updateUser(MouseEvent event) {
        event.consume();
        Constants.Auth uType = Constants.Auth.valueOf(cmbUserType.getValue().toString().toUpperCase());
        User user = new User(0,UserName.getText(),Password.getText(), uType);
        UserTable.createUser(user);
        user.setUserID(UserTable.getUserByUsername(user.getUsername()).getUserID());
        ScreenController.deactivate();
    }

    public void cancelScreen(MouseEvent event) {
        event.consume();
        ScreenController.deactivate();
    }
}