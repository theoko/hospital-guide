package controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import database.UserTable;
import helpers.Constants;
import helpers.UserHelpers;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import models.user.User;

import java.net.URL;
import java.util.ResourceBundle;

public class EditUserPopUpController extends PopUpControllerUser implements Initializable {

    public JFXComboBox cmbUserType;

    public String EMPLOYEE;
    public String ADMIN;
    public String CUSTODIAN;

    public JFXTextField UserID;
    public JFXTextField UserName;
    public JFXTextField Password;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
      /*  UserID.setText(String.valueOf(this.userM.getUserID()));
        UserName.setText(this.userM.getUsername());
        Password.setText(this.userM.getPassword());*/
    }
    
    public void setUser(User user){
        this.userM = user;
    }

    public void updateUser(MouseEvent event) {
        event.consume();
        Constants.Auth uType = Constants.Auth.valueOf(cmbUserType.getValue().toString().toUpperCase());
        userM.setPassword(Password.getText());
        userM.setUsername(UserName.getText());
        userM.setUserType(uType);
        ScreenController.deactivate();
    }
}
