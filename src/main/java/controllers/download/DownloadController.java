package controllers.download;

import com.jfoenix.controls.JFXButton;
import controllers.ScreenController;
import helpers.Constants;
import helpers.FileHelpers;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import database.CSVParser;

import java.io.File;


public class DownloadController {

    @FXML
    JFXButton buttonYes;

    @FXML
    JFXButton buttonNo;

    public void clickButtonNo(MouseEvent event) throws Exception {
        event.consume();
        ScreenController.deactivate();
        ScreenController.activate(Constants.Routes.ADMIN_MAP);

    }

    public void clickButtonYes(MouseEvent event) throws Exception {
        event.consume();
//
//        File data = new File("data");
//        data.mkdir();

//        if(FileHelpers.checkJar()) {
            CSVParser.export("eNodes.csv", "eEdges.csv");
//        } else {
//            CSVParser.export("data/eNodes.csv", "data/eEdges.csv");
//        }

        ScreenController.deactivate();
        ScreenController.activate(Constants.Routes.DOWNLOADED);
    }

}
