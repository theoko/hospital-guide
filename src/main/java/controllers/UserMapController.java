package controllers;

import com.jfoenix.controls.JFXButton;
import helpers.Constants;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import map.MapDisplay;

public class UserMapController extends MapController {
    public JFXButton btnSettings;

    public void initialize() {
        toolTip();
        MapDisplay.displayUser(new AnchorPane[] {panFloorL2, panFloorL1, panFloor1, panFloor2, panFloor3});
    }

    @Override
    public void logOut(MouseEvent event) throws Exception {
        event.consume();
        ScreenController.logOut(btnReturn);
        ScreenController.activate(Constants.Routes.WELCOME);
    }

    void toolTip() {
        btnSettings.setTooltip(new Tooltip(Constants.SETTINGS_BUTTON_TOOLTIP));
        btnReturn.setTooltip(new Tooltip(Constants.EXIT_BUTTON_TOOLTIP));
    }
}
