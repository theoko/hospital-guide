package controllers;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import database.Database;
import helpers.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.room.Room;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public class RoomBookingController {

    /**
     * FXML
     */
    public JFXDatePicker datStartDay;
    public JFXDatePicker datEndDay;
    public JFXTimePicker datStartTime;
    public JFXTimePicker datEndTime;

    // Table that displays available rooms
    public TableView tblRooms;

    // Columns
    public TableColumn<Room, String> tblRoomID;
    public TableColumn<Room, String> tblRoomCapacity;

    ObservableList<Room> rooms = FXCollections.observableArrayList();

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


        initBooking();
    }

    private void initBooking() {

        // Initialize table
        tblRoomID.setCellValueFactory(new PropertyValueFactory<>("RoomID"));
        tblRoomCapacity.setCellValueFactory(new PropertyValueFactory<>("Capacity"));
        tblRooms.setItems(rooms);

    }

    public String getDateTime(LocalDate date, LocalTime time) {

        try {

            Date parsedDate = new SimpleDateFormat(Constants.dateFormat).parse(
                    date.toString() + " " + time.toString() + ":00"
            );

            return new SimpleDateFormat(Constants.dateFormat).format(parsedDate);

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

            timePeriodSet = true;

            List<Room> roomsAvailable = Database.checkAvailabilityTime(
                    getDateTime(startDate, startTime),
                    getDateTime(endDate, endTime)
            );

            populateRoomBookingTable(roomsAvailable);

        }
    }

    private void populateRoomBookingTable(List<Room> roomsAvailable) {

        rooms.addAll(roomsAvailable);

        tblRooms.refresh();

    }

}
