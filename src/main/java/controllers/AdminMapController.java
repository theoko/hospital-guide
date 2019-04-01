package controllers;

import com.jfoenix.controls.JFXButton;
import com.querydsl.core.types.Constant;
import helpers.Constants;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import map.MapDisplay;

public class AdminMapController extends MapController {
    public JFXButton btnDownload;
    public JFXButton btnBooking;

    public void initialize() {
        // Set tooltip
        toolTip();

        // Set button text
        btnDownload.setText(Constants.DOWNLOAD_BUTTON_TEXT);
        btnBooking.setText(Constants.BOOKING_BUTTON_TEXT);

        // Display floor map
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

    public void displayBooking(MouseEvent event) throws Exception {
        event.consume();
        ScreenController.deactivate();
        ScreenController.activate("book-room");
    }
}
