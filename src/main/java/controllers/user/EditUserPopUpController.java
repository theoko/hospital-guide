package controllers.user;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controllers.ScreenController;
import helpers.Constants;
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
        if(cmbUserType.getValue()!=null) {
            Constants.Auth uType = Constants.Auth.valueOf(cmbUserType.getValue().toString().toUpperCase());
            userM.setUserType(uType);
        }
        if(Password.getText() != null)
            userM.setPassword(Password.getText());
        if(UserName.getText() != null)
            userM.setUsername(UserName.getText());

        ScreenController.deactivate();
    }

    public void cancelScreen(MouseEvent event) {
        event.consume();
        ScreenController.deactivate();
    }
}