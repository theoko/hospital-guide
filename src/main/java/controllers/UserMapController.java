package controllers;

import com.jfoenix.controls.JFXButton;
import helpers.Constants;
import javafx.scene.control.Tooltip;
import map.MapDisplay;

public class UserMapController extends MapController {
    public JFXButton btnSettings;

    public void initialize() {
        toolTip();
        MapDisplay.displayUser(panMap, "Tower", "1");
    }

    void toolTip() {
        btnSettings.setTooltip(new Tooltip(Constants.SETTINGS_BUTTON_TOOLTIP));
        btnReturn.setTooltip(new Tooltip(Constants.EXIT_BUTTON_TOOLTIP));
    }
}
