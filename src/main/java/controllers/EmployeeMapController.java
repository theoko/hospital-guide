package controllers;

import com.jfoenix.controls.JFXButton;
import helpers.Constants;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import map.MapDisplay;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeMapController extends MapController {
    public JFXButton btnSettings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        MapDisplay.displayEmployee(panes);
    }
}
