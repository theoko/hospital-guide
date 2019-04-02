package controllers;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.controls.JFXTreeTableView;
import database.Database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class RoomBookingController {

    public JFXDatePicker datStartDay;
    public JFXDatePicker datEndDay;
    public JFXTimePicker datStartTime;
    public JFXTimePicker datEndTime;
    public JFXTreeTableView tblRooms;

    LocalDate startDate;
    LocalDate endDate;

    LocalTime startTime;
    LocalTime endTime;

    boolean timePeriodSet = false;

    public void initialize() {

        // Add listeners for date and time pickers
        datStartDay.valueProperty().addListener((observable, oldValue, newValue) -> {
            startDate = newValue;
            checkDateAndTime();
        });

        datEndDay.valueProperty().addListener((observable, oldValue, newValue) -> {
            endDate = newValue;
            checkDateAndTime();
        });

        datStartTime.valueProperty().addListener((observable, oldValue, newValue) -> {
           startTime = newValue;
            checkDateAndTime();
        });

        datEndTime.valueProperty().addListener((observable, oldValue, newValue) -> {
            endTime = newValue;
            checkDateAndTime();
        });

    }

    public String getDateTime(LocalDate date, LocalTime time) {

        try {

            Date parsedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(
                    date.toString() + " " + time.toString()
            );

            return parsedDate.toString();

        } catch (ParseException e) {
            e.printStackTrace();

            return null;
        }

    }

    public void checkDateAndTime() {
        if (startDate != null
                && endDate != null
                && startTime != null
                && endTime != null) {

            Database.checkAvailabilityTime(
                    getDateTime(startDate, startTime),
                    getDateTime(endDate, endTime)
            );

            timePeriodSet = true;

        }
    }



}
