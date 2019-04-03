package controllers;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import database.Database;
import helpers.DatabaseHelpers;
import helpers.UserHelpers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.room.Book;
import models.room.Room;
import models.user.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class RoomBookingController extends EmployeeMapController{

    /**
     * FXML
     */
    public JFXDatePicker datStartDay;
    public JFXDatePicker datEndDay;
    public JFXTimePicker datStartTime;
    public JFXTimePicker datEndTime;

    // Table that displays available rooms
    public TableView<Room> tblRooms;

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
        // Column names SHOULD be EXACTLY EQUAL to the ObservableList attributes
        tblRoomID.setCellValueFactory(new PropertyValueFactory<>("RoomID"));
        tblRoomCapacity.setCellValueFactory(new PropertyValueFactory<>("Capacity"));
        tblRooms.setItems(rooms);

    }

    public void checkDateAndTime() {
        if (startDate != null
                && endDate != null
                && startTime != null
                && endTime != null) {

            timePeriodSet = true;

            List<Room> roomsAvailable = Database.checkAvailabilityTime(
                    DatabaseHelpers.getDateTime(startDate, startTime),
                    DatabaseHelpers.getDateTime(endDate, endTime)
            );

            populateRoomBookingTable(roomsAvailable);

        }
    }

    private void populateRoomBookingTable(List<Room> roomsAvailable) {

        rooms.addAll(roomsAvailable);

        tblRooms.refresh();

    }

    /**
     * Called on when user clicks on the book button
     */
    private void reserveRoom() {

        // Get selected room
        Room selected = tblRooms.getSelectionModel().getSelectedItem();

        // Get currently authenticated user
        User currentUser = UserHelpers.getCurrentUser();

        // Add booking to the database
//        Book book = new Book()
//        Database.createBooking()


    }

}
