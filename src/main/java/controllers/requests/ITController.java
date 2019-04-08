package controllers.requests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import controllers.ScreenController;
import database.UserTable;
import helpers.UserHelpers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import models.requests.IT;
import models.user.User;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ITController {


    public JFXComboBox cmbType;
    public JFXComboBox cmbPriority;
    public JFXTextField txtDesc;
    public JFXComboBox cmbBuilding;
    public JFXComboBox cmbRoom;
    public JFXComboBox cmbTime;
    public Label lblTime;
    public JFXTimePicker datTime;
    public JFXButton btnSendRequest;
    public JFXButton btnCancel;

    public TableView<IT> tblIT;

    public TableColumn<IT, String> tblRequestID;
    public TableColumn<IT, String> tblRequestPrior;
    public TableColumn<IT, String> tblRequestType;
    public TableColumn<IT, String> tblRequestDesc;
    public TableColumn<IT, String> tblRequestBuild;
    public TableColumn<IT, String> tblRequestRoom;
    public TableColumn<IT, String> tblRequestTime;
    public TableColumn<IT, String> tblRequestUser;
    public JFXButton btnComplete;
    public JFXButton btnReturn;

    public int requestID = 1;
    public String ASAP;
    public String LATER;
    public String reqTime = "ASAP";

    LocalTime time;

    ObservableList<IT> ITreqs = FXCollections.observableArrayList();

    public void initialize() {
        lblTime.setVisible(false);
        datTime.setVisible(false);
        initIT();
    }

    public void laterTime() {
        lblTime.setVisible(true);
        datTime.setVisible(true);

        // Add listeners for date and time pickers
        datTime.valueProperty().addListener((observable, oldValue, newValue) -> {
            time = newValue;
            reqTime = time.toString();
        });

    }

    public void setReqTime(ActionEvent event) {
        event.consume();
        if(cmbTime.getValue() == LATER)
            laterTime();
        else {
            lblTime.setVisible(false);
            datTime.setVisible(false);
            reqTime = ASAP;
        }
    }

    List<IT> ITDetails = new ArrayList<>();
    private void initIT() {
        tblRequestID.setCellValueFactory(new PropertyValueFactory<>("RequestID"));
        tblRequestPrior.setCellValueFactory(new PropertyValueFactory<>("Priority"));
        tblRequestType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        tblRequestDesc.setCellValueFactory(new PropertyValueFactory<>("Description"));
        tblRequestBuild.setCellValueFactory(new PropertyValueFactory<>("Building"));
        tblRequestRoom.setCellValueFactory(new PropertyValueFactory<>("Room"));
        tblRequestTime.setCellValueFactory(new PropertyValueFactory<>("Time"));
        tblRequestUser.setCellValueFactory(new PropertyValueFactory<>("Requester"));
        tblIT.setItems(ITreqs);
        if(ITDetails != null) {
            ITreqs.clear();
            ITreqs.addAll(ITDetails);
            tblIT.refresh();
        }
    }

    public void sendRequest(MouseEvent event) {
        event.consume();
        String desc = txtDesc.getText();
        String priority = (String) cmbPriority.getValue();
        String type = (String) cmbType.getValue();
        String buildcmb = (String) cmbBuilding.getValue();
        String build = "";
        switch (buildcmb) {
            case "BTM":
                build = "BTM";
                break;
            case "45 Francis":
                build = "FRAN45";
                break;
            case "Tower":
                build = "TOWER";
                break;
            case "15 Francis":
                build = "FRAN15";
                break;
            case "Shapiro":
                build = "SHAPIRO";
                break;
        }
        String roomcmb = (String) cmbRoom.getValue();
        String room = "";
        switch (roomcmb) {
            case "Conference Room A":
                room = "CONFA";
                break;
            case "Classroom A":
                room = "CLASSA";
                break;
            case "Work Zone A":
                room = "WZA";
                break;
            case "Conference Room B":
                room = "CONFB";
                break;
            case "Classroom B":
                room = "CLASSB";
                break;
            case "Work Zone B":
                room = "WZB";
                break;
            case "Conference Room C":
                room = "CONFC";
                break;
            case "Classroom C":
                room = "CLASSC";
                break;
            case "Work Zone C":
                room = "WZC";
                break;
        }
        IT req = new IT(requestID++, IT.Priority.valueOf(priority), IT.Type.valueOf(type), desc, IT.Building.valueOf(build), IT.Room.valueOf(room), reqTime, UserHelpers.getCurrentUser().toString());
        ITDetails.add(req);
        ITreqs.clear();
        ITreqs.addAll(ITDetails);
        tblIT.refresh();
    }

    public void completeReq(MouseEvent event) {
        event.consume();
        event.consume();
        IT selected = tblIT.getSelectionModel().getSelectedItem();
        System.out.println(selected.toString());
        ITDetails.remove(selected);
        ITreqs.clear();
        ITreqs.addAll(ITDetails);
        tblIT.refresh();
    }

    public void cancelScr(MouseEvent event) {
        event.consume();
        ScreenController.deactivate();
    }

}
