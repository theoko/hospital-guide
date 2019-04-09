package controllers.requests;

import com.jfoenix.controls.*;
import controllers.ScreenController;
import helpers.UserHelpers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.requests.Florist;
import models.requests.IT;
import models.user.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FlouristController {

    public Label lblRecipientName;
    public JFXTextField txtRecipientName;
    public Label lblFlowerType;
    public JFXComboBox cmbFlowerType;
    public Label lblMessage;
    public JFXTextField txtMessage;
    public JFXComboBox cmbTime;
    public Label lblTime;
    public JFXTimePicker datTime;
    public Label lblDeliveryDate;
    public JFXDatePicker datDeliveryDate;
    public JFXButton btnSendRequest;
    public JFXButton btnCancel;

    public TableView<Florist> tblFlorist;

    public TableColumn<Florist, String> tblRecipientName;
    public TableColumn<Florist, String> tblRFlowerType;
    public TableColumn<Florist, String> tblMessage;
    public TableColumn<Florist, String> tblDeliveryDate;
    public TableColumn<Florist, String> tblTime;
    public JFXButton btnReturn;
    public String reqTime;
    public String reqDate;

    LocalDate Deliverydate;

    LocalTime time;

    ObservableList<Florist> Floristreqs = FXCollections.observableArrayList();

    public void initialize() {
        initIT();
    }

    public void laterTime() {

        // Add listeners for date and time pickers
        datTime.valueProperty().addListener((observable, oldValue, newValue) -> {
            time = newValue;
            reqTime = time.toString();
        });

        datDeliveryDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            Deliverydate = newValue;
            reqDate = Deliverydate.toString();
        });

    }


    List<Florist> FloristDetails = new ArrayList<>();

    private void initIT() {
        tblRecipientName.setCellValueFactory(new PropertyValueFactory<>("RecipientName"));
        tblRFlowerType.setCellValueFactory(new PropertyValueFactory<>("FlowerType"));
        tblMessage.setCellValueFactory(new PropertyValueFactory<>("Message"));
        tblDeliveryDate.setCellValueFactory(new PropertyValueFactory<>("Delivery Date"));
        tblTime.setCellValueFactory(new PropertyValueFactory<>("Time"));
        tblFlorist.setItems(Floristreqs);
        if (FloristDetails != null) {
            Floristreqs.clear();
            Floristreqs.addAll(FloristDetails);
            tblFlorist.refresh();
        }
    }

    public void sendRequest(MouseEvent event) {
        event.consume();

        if (txtRecipientName != null && cmbFlowerType.getValue() != null && txtMessage != null && reqDate !=null && reqTime !=null) {
            laterTime();
            String RecipientNametxt = txtRecipientName.getText();
            String FlowerTypecmb = (String) cmbFlowerType.getValue();
            String Messagetxt = txtMessage.getText();
            String currUser = UserHelpers.getCurrentUser().toString();
            Florist req = new Florist(RecipientNametxt, Florist.FlowerType.valueOf(FlowerTypecmb), Messagetxt, reqTime, reqDate, currUser);
            FloristDetails.add(req);
            //  ITreqs.clear();
            Floristreqs.addAll(FloristDetails);
            tblFlorist.refresh();
        }

    }

    public void cancelScr(MouseEvent event) {
        event.consume();
        ScreenController.deactivate();
    }

}

