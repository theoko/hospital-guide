package controllers;

import com.jfoenix.controls.JFXButton;
import helpers.Constants;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import map.MapDisplay;

public class EmployeeMapController extends MapController {
    public JFXButton btnSettings;

    public void initialize() {
        toolTip();
        MapDisplay.displayEmployee(new AnchorPane[] {panFloorL2, panFloorL1, panFloor1, panFloor2, panFloor3});

    }

    void toolTip() {
        btnSettings.setTooltip(new Tooltip(Constants.SETTINGS_BUTTON_TOOLTIP));
        btnReturn.setTooltip(new Tooltip(Constants.LOGOUT_BUTTON_TOOLTIP));
    }
}
