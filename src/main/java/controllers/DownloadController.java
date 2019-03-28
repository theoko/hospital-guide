package controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import database.CSVParser;


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
        CSVParser.export("/data/eNodes.csv", "/data/eEdges.csv");
        ScreenController.deactivate();
        ScreenController.activate("downloaded");
    }

}
