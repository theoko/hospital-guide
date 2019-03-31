package controllers;

import com.jfoenix.controls.JFXButton;
import helpers.Constants;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import map.MapDisplay;

public class AdminMapController extends MapController {
    public JFXButton btnDownload;

    public void initialize() {
        toolTip();
        MapDisplay.displayAdmin(panMap, "Tower", "1");
    }

    void toolTip() {
        btnDownload.setTooltip(new Tooltip(Constants.DOWNLOAD_BUTTON_TOOLTIP));
        btnReturn.setTooltip(new Tooltip(Constants.LOGOUT_BUTTON_TOOLTIP));
    }

    public void clickDownload(MouseEvent event) throws Exception {
        event.consume();
        ScreenController.deactivate();
        ScreenController.activate("download");
    }
}
