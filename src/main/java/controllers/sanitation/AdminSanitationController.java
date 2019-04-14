package controllers.sanitation;

import com.jfoenix.controls.JFXButton;
import database.SanitationTable;
import javafx.scene.input.MouseEvent;
import models.services.SanitationRequest;

public class AdminSanitationController extends SanitationController {
    public JFXButton btnDelete;

    public void deleteSanitationRequest(MouseEvent mouseEvent) {
        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();
        SanitationTable.deleteSanitationRequest(selected);
        updateSanitation();
        tblData.refresh();
    }
}
