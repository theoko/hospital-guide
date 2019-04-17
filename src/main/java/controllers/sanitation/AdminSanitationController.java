package controllers.sanitation;

import com.jfoenix.controls.JFXButton;
import database.SanitationTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.services.SanitationRequest;

import java.util.List;

public class AdminSanitationController extends SanitationController {
    public JFXButton btnDelete;
    public TableView<SanitationRequest> tblData;

    public void tblClick(){

        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();
        if(selected!=null)
            btnDelete.setDisable(false);
    }

    public void deleteSanitationRequest(MouseEvent mouseEvent) {
        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();
        super.deleteSanitationRequest(selected);
    }


}
