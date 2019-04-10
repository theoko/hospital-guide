package controllers;

import com.jfoenix.controls.JFXButton;
import helpers.Constants;
import helpers.DatabaseHelpers;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import map.PathFinder;
import models.map.Location;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeInfoController extends InfoController {
    public JFXButton btnRequest;

    public void btnReportSpill_OnClick(MouseEvent event) {
        event.consume();
        ScreenController.deactivate();
        try {
            ScreenController.popUp(Constants.Routes.SANITATION_REQUEST , loc);
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }
}
