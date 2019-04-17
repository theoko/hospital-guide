package controllers.sanitation;

import database.SanitationTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.services.SanitationRequest;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public abstract class SanitationController implements Initializable {
    public TableView<SanitationRequest> tblData;
    public TableColumn<SanitationRequest,String> tblLocation;
    public TableColumn<SanitationRequest,String> tblPriority;
    public TableColumn<SanitationRequest,String> tblStatus;
    public TableColumn<SanitationRequest,String> tblDescription;
    public TableColumn<SanitationRequest,String> tblRequester;
    public TableColumn<SanitationRequest,String> tblClaimTime;
    public TableColumn<SanitationRequest,String> tblServicer;
    public TableColumn<SanitationRequest,String> tblServiceTime;

    ObservableList<SanitationRequest> spills = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initSanitation();
        updateSanitation();
    }

    private void initSanitation(){
        tblLocation.setCellValueFactory(new PropertyValueFactory<>("LocationShortName"));
        tblPriority.setCellValueFactory(new PropertyValueFactory<>("Priority"));
        tblStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
        tblDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        tblRequester.setCellValueFactory(new PropertyValueFactory<>("RequesterUserName"));
        tblClaimTime.setCellValueFactory(new PropertyValueFactory<>("ClaimedTime"));
        tblServiceTime.setCellValueFactory(new PropertyValueFactory<>("CompletedTime"));
        tblServicer.setCellValueFactory(new PropertyValueFactory<>("ServicerUserName"));
        tblData.setItems(spills);
    }

    protected List updateSanitation() {
        List<SanitationRequest> lstReqs = SanitationTable.getSanitationRequests();
        if (lstReqs != null) {
            spills.addAll(lstReqs);
        }
        return lstReqs;
    }
    public void deleteSanitationRequest(SanitationRequest selected) {
        SanitationTable.deleteSanitationRequest(selected);
        spills.setAll(updateSanitation());
        tblData.refresh();
    }
}
