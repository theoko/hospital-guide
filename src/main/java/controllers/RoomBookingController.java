package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import database.Database;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class RoomBookingController {
    public JFXDatePicker datStartDay;
    public JFXDatePicker datEndDay;
    public JFXTimePicker datStartTime;
    public JFXTimePicker datEndTime;
    public JFXButton btnDisplay;

    public void btnDisplay_OnClick(MouseEvent mouseEvent) {
        LocalDate startDay = datStartDay.getValue();
        LocalDate endDay = datEndDay.getValue();
        LocalTime startTime = datStartTime.getValue();
        LocalTime endTime = datEndTime.getValue();

        Date startDate = new Date();
        Date endDate = new Date();

        Database.get
    }
}
