package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.controls.JFXTreeTableView;
import database.Database;
import javafx.scene.input.MouseEvent;
import models.room.Room;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class RoomBookingController {
    public JFXDatePicker datStartDay;
    public JFXDatePicker datEndDay;
    public JFXTimePicker datStartTime;
    public JFXTimePicker datEndTime;
    public JFXButton btnDisplay;
    public JFXTreeTableView tblRooms;

    public void btnDisplay_OnClick(MouseEvent mouseEvent) {
        LocalDate startDay = datStartDay.getValue();
        LocalDate endDay = datEndDay.getValue();
        LocalTime startTime = datStartTime.getValue();
        LocalTime endTime = datEndTime.getValue();
        String startTime =


        List<Room> lstRooms = Database.checkAvailabilityTime(startTime, endTime);
        for (Room room : lstRooms) {

        }
    }
}
