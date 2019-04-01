package controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class RoomBookingController implements Initializable {
    public JFXDatePicker datStartDay;
    public JFXDatePicker datEndDay;
    public JFXTimePicker datStartTime;
    public JFXTimePicker datEndTime;
    public JFXComboBox cmbRoom;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        datStartDay.valueProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("DATA");
        });
    }
}
