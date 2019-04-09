package controllers.requests;

import com.jfoenix.controls.JFXTextField;
import controllers.ScreenController;
import helpers.UserHelpers;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.requests.InterpreterRequest;

import java.util.ArrayList;
import java.util.List;


public class InterpreterController {

    private ObservableList<InterpreterRequest> tableList = FXCollections.observableArrayList();
    @FXML
    TableView<InterpreterRequest> tableRequest;
    @FXML
    TableColumn<InterpreterRequest, String> tableLoc, tableLang, tableComp;
    @FXML
    JFXTextField Loc, Lang;

    public void initialize() {
        tableLoc.setCellValueFactory(
                new PropertyValueFactory<>("Loc")
        );
        tableLang.setCellValueFactory(
                new PropertyValueFactory<>("Lang")
        );
        tableComp.setCellValueFactory(
                new PropertyValueFactory<>("Complete")
        );
        tableRequest.setItems(tableList);
//        if()

    }
    List<InterpreterRequest> localReq = new ArrayList<>();
    public void submitRequest() {
        String loc = Loc.getText(), lang = Lang.getText();
        if(loc != null && lang != null) {
            InterpreterRequest ir = new InterpreterRequest(loc, lang);
            localReq.add(ir);
            tableList.add(ir);
            UserHelpers.getCurrentUser();
            tableRequest.refresh();
//            tableRequest.setItems(tableList);

        }
    }
    public void setCompleted() {
        try {
            tableRequest.getSelectionModel().getSelectedItem().setComplete(UserHelpers.getCurrentUser().getUsername());
        } catch(Exception e) {
            System.out.println("Nothing selected");
        }
        tableRequest.refresh();
    }
    public void endScreen() {
        ScreenController.deactivate();
    }
}
