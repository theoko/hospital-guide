package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import helpers.Constants;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import models.map.Location;

import java.net.URL;
import java.util.ResourceBundle;

public class SanitationController extends PopUpController implements Initializable {

    public JFXComboBox cmbPriority;
    public JFXTextField txtDescription;
    public JFXButton submitBTN;
    public String LOW;
    public String MED;
    public String HIGH;
    private Location myLoc;



    public void sendRequest(MouseEvent event) {
        event.consume();
        String description = (String) txtDescription.getText();
        String priority = (String) cmbPriority.getValue();

        //todo add request to database @dan

        //todo niko 1. force user to fill both fields. 2. put controller in class diagram

        ScreenController.deactivate();
    }

    public void goBack(MouseEvent event) throws Exception{
       // ((Stage) (((Node) event.getSource()).getScene().getWindow())).close();
        event.consume();
        ScreenController.deactivate();
    }

    public void keyReleased(){
        String des = txtDescription.getText();
        String pri = (String) cmbPriority.getValue();

        boolean isDisabled = des.isEmpty()||pri.isEmpty();
        submitBTN.setDisable(isDisabled);

    }

    public void setLoc(Location loc){
        this.myLoc=loc;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
