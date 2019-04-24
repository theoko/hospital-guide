package controllers.welcome;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import helpers.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.requests.Security;
import controllers.ScreenController;
import sun.rmi.runtime.Log;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public abstract class LogoutController implements Initializable {

    public JFXTextField txtTime;
    public int theTime = 5;

    public int getTime(){
        return Integer.parseInt(txtTime.getText());
    }

    public void btnSubmit_Clicked(MouseEvent mouseEvent) {
        mouseEvent.consume();
        theTime = getTime();
        ScreenController.deactivate();
        try {
            ScreenController.activate(Constants.Routes.WELCOME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}