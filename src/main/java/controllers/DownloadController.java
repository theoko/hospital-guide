package controllers;

import com.jfoenix.controls.JFXButton;
import database.Database;
import helpers.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tooltip;
import com.jfoenix.controls.JFXButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


public class DownloadController {

    @FXML
    JFXButton buttonYes;

    @FXML
    JFXButton buttonNo;

    public void clickButtonNo(MouseEvent event) throws Exception {
        event.consume();
        ScreenController.deactivate();
        ScreenController.activate("main");

    }

    public void clickButtonYes(MouseEvent event) throws Exception {
        event.consume();
        ScreenController.deactivate();
        ScreenController.activate("downloaded");
    }

}
