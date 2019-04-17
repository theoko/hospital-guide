package controllers.booking;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import database.BookLocationTable;
import database.BookWorkspaceTable;
import database.LocationTable;
import helpers.UserHelpers;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import models.map.Location;
import models.room.Book;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class DisplayCalendarController {

    public BorderPane primaryStage;
    public Calendar rooms;
    public Calendar workspaces;

    public Calendar getRooms() {
        return rooms;
    }

    public void setRooms(Calendar rooms) {
        this.rooms = rooms;
    }

    public Calendar getWorkspaces() {
        return workspaces;
    }

    public void setWorkspaces(Calendar workspaces) {
        this.workspaces = workspaces;
    }

    public void initialize() {

        CalendarView calendarView = new CalendarView();

        rooms = new Calendar("Rooms");
        workspaces = new Calendar("Workspaces");

        CalendarSource myCalendarSource = new CalendarSource("My Calendars");
        myCalendarSource.getCalendars().addAll(workspaces, rooms);

        calendarView.getCalendarSources().addAll(myCalendarSource);
        calendarView.setRequestedTime(LocalTime.now());

        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        calendarView.setToday(LocalDate.now());
                        calendarView.setTime(LocalTime.now());
                    });

                    try {
                        // update every 10 seconds
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            };
        };

        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();
        primaryStage.setCenter(calendarView);
        setWorkspaces();
        setRooms();
    }

    public void removeWorkspacesEntry(Entry workspaces){
        getWorkspaces().removeEntry(workspaces);
        LocalDateTime startTime = workspaces.getStartAsLocalDateTime();
        LocalDateTime endTime = workspaces.getEndAsLocalDateTime();
        Book book = BookWorkspaceTable.getBookByTimes(startTime, endTime);
        BookWorkspaceTable.deleteWorkspaceBook(book);

    }

    public void removeRoomsEntry(Entry rooms){
        getRooms().removeEntry(rooms);
        LocalDateTime startTime = rooms.getStartAsLocalDateTime();
        LocalDateTime endTime = rooms.getEndAsLocalDateTime();
        Book book = BookWorkspaceTable.getBookByTimes(startTime, endTime);
        BookLocationTable.deleteLocationeBook(book);

    }


    public void setWorkspaces(){
        List<Book> bookingsForUser = BookWorkspaceTable.getBookingsForUser(UserHelpers.getCurrentUser());

        if(bookingsForUser != null) {
            for (Book book : bookingsForUser) {
                String roomID = book.getRoomID();
                Location room = LocationTable.getLocationByID(roomID);

                if(room != null) {
                    String name = room.getLongName();
                    Entry<String> newEntry = new Entry<>(name);
                    workspaces.addEntry(newEntry);
                }
            }
        }
    }

    public void setRooms(){
        List<Book> bookingsForUser = BookLocationTable.getBookingsForUser(UserHelpers.getCurrentUser());

        if(bookingsForUser != null) {
            for (Book book : bookingsForUser) {
                String roomID = book.getRoomID();
                Location room = LocationTable.getLocationByID(roomID);

                if(room != null) {
                    String name = room.getLongName();
                    Entry<String> newEntry = new Entry<>(name);
                    rooms.addEntry(newEntry);
                }
            }
        }
    }
}