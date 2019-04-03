package controllers;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import models.room.Book;
import models.room.Room;
import models.user.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RoomBookingController extends EmployeeMapController{

    /**
     * FXML
     */
    public JFXDatePicker datStartDay;
    public JFXDatePicker datEndDay;
    public JFXTimePicker datStartTime;
    public JFXTimePicker datEndTime;

    public JFXButton btnBookSelected;

    // Table that displays available rooms
    public TableView<Room> tblRooms;
    public TableView<Room> tblRoomsBooked;

    // Columns
    public TableColumn<Room, String> tblRoomID;
    public TableColumn<Room, String> tblRoomCapacity;

    public TableColumn<Room, String> tblRoomIDBooked;
    public TableColumn<Room, String> tblRoomCapacityBooked;

    ObservableList<Room> rooms = FXCollections.observableArrayList();
    ObservableList<Room> roomsBooked = FXCollections.observableArrayList();

    LocalDate startDate;
    LocalDate endDate;

    LocalTime startTime;
    LocalTime endTime;

    boolean timePeriodSet = false;

    private int totalBookings = 0;

    public void initialize() {

        btnBookSelected.setVisible(false);

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
        initBooked();

    }

    private void initBooking() {

        // Initialize table
        // Column names SHOULD be EXACTLY EQUAL to the ObservableList attributes
        tblRoomID.setCellValueFactory(new PropertyValueFactory<>("RoomID"));
        tblRoomCapacity.setCellValueFactory(new PropertyValueFactory<>("Capacity"));
        tblRooms.setItems(rooms);

        tblRoomIDBooked.setCellValueFactory(new PropertyValueFactory<>("RoomID"));
        tblRoomCapacityBooked.setCellValueFactory(new PropertyValueFactory<>("Capacity"));
        tblRoomsBooked.setItems(roomsBooked);

    }

    List<Room> roomDetails = new ArrayList<>();
    private void initBooked() {
        User currentUser = UserHelpers.getCurrentUser();

        List<Book> roomsBooked = Database.getBookingsForUser(currentUser);

        for(Book booking : roomsBooked) {
            Room currRoom = Database.getRoomByID(booking.getRoomID());

            roomDetails.add(currRoom);
        }

        populateRoomBookedTable(roomDetails);
    }

    /**
     * Checks if start date/time and end date/time are set
     */
    List<Room> roomsAvailable;
    public void checkDateAndTime() {
        if (startDate != null
                && endDate != null
                && startTime != null
                && endTime != null) {

            timePeriodSet = true;

            roomsAvailable = Database.checkAvailabilityTime(
                    DatabaseHelpers.getDateTime(startDate, startTime),
                    DatabaseHelpers.getDateTime(endDate, endTime)
            );

            populateRoomBookingTable(roomsAvailable);

        }
    }

    /**
     * Populates the rooms available table
     * @param roomsAvailable
     */
    private void populateRoomBookingTable(List<Room> roomsAvailable) {

        rooms.addAll(roomsAvailable);

        tblRooms.refresh();

    }

    /**
     * Populates the booked rooms table
     * @param roomsBooked
     */
    private void populateRoomBookedTable(List<Room> roomsBooked) {

        roomsBooked.addAll(roomsBooked);

        tblRoomsBooked.refresh();

    }

    /**
     * Called on when user clicks on the book button
     */
    private void reserveRoom() {

        assert startDate != null;
        assert startTime != null;
        assert endDate != null;
        assert endTime != null;

        // Get selected room
        Room selected = tblRooms.getSelectionModel().getSelectedItem();

        // Get currently authenticated user
        User currentUser = UserHelpers.getCurrentUser();

        // Add booking to the database
        Book book = new Book(totalBookings,
                selected.getRoomID(),
                currentUser,
                DatabaseHelpers.getDateTime(startDate, startTime),
                DatabaseHelpers.getDateTime(endDate, endTime)
        );

        boolean booked = Database.createBooking(book);

        if(booked) {

            // Add to rooms booked table
            Room roomD = Database.getRoomByID(book.getRoomID());
            roomDetails.add(roomD);

            populateRoomBookedTable(roomDetails);

        } else {

            // Show error
            System.out.println("failed!!");

        }

    }

    /**
     * Method to handle selected room
     * @param event
     */
    public void handleBookingSelection(MouseEvent event) {

        if(event.getButton().equals(MouseButton.PRIMARY)) {

            // Check if we have available rooms
            if(roomsAvailable != null) {
                if(roomsAvailable.size() > 0) {
                    btnBookSelected.setVisible(true);
                }
            }

        }

    }

    public void handleBooking(MouseEvent event) {

        event.consume();
        assert tblRooms.getSelectionModel().getSelectedItem() != null;
        reserveRoom();

    }

}
