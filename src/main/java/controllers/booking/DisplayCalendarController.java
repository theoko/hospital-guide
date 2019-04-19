package controllers.booking;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.DateControl;
import database.BookLocationTable;
import database.BookWorkspaceTable;
import database.LocationTable;
import database.WorkspaceTable;
import helpers.DatabaseHelpers;
import helpers.UserHelpers;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;
import models.map.Location;
import models.map.Workspace;
import models.room.Book;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;

public class DisplayCalendarController extends DateControl {

    public BorderPane primaryStage;
    public Calendar locations;
    public Calendar workspaces;

    public Calendar getLocations() {
        return locations;
    }

    public void setLocations(Calendar rooms) {
        this.locations = rooms;
    }

    public Calendar getWorkspaces() {
        return workspaces;
    }

    public void setWorkspaces(Calendar workspaces) {
        this.workspaces = workspaces;
    }

    public void initialize() {

        CalendarView calendarView = new CalendarView();

        locations = new Calendar("Locations");
        workspaces = new Calendar("Workspaces");

        locations.setStyle(Calendar.Style.STYLE1);
        workspaces.setStyle(Calendar.Style.STYLE2);

        CalendarSource myCalendarSource = new CalendarSource("My Calendars");
        myCalendarSource.getCalendars().addAll(workspaces, locations);

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
                        sleep(10000);
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
        setLocations();
    }

    public void removeWorkspacesEntry(Entry workspaces){
        getWorkspaces().removeEntry(workspaces);
        LocalDateTime startTime = workspaces.getStartAsLocalDateTime();
        LocalDateTime endTime = workspaces.getEndAsLocalDateTime();
        Book book = BookWorkspaceTable.getBookByTimes(startTime, endTime);
        BookWorkspaceTable.deleteWorkspaceBook(book);

    }

    public void removeLocationsEntry(Entry location){
        getLocations().removeEntry(location);
        LocalDateTime startTime = location.getStartAsLocalDateTime();
        LocalDateTime endTime = location.getEndAsLocalDateTime();
        Book book = BookWorkspaceTable.getBookByTimes(startTime, endTime);
        BookLocationTable.deleteLocationeBook(book);

    }


    public void setWorkspaces(){
        List<Book> bookingsForUser = BookWorkspaceTable.getBookingsForUser(UserHelpers.getCurrentUser());
        for (Book bk : bookingsForUser) {
            String[] start = bk.getStartDate().split(" ");
            String[] end = bk.getEndDate().split(" ");
            String startDay = start[0];
            String startTime = start[1].substring(0, start[1].indexOf("."));
            String endDay = end[0];
            String endTime = end[1].substring(0, end[1].indexOf("."));
            ZonedDateTime calStartTime = DatabaseHelpers.getCalDateTime(startDay, startTime);
            ZonedDateTime calEndTime = DatabaseHelpers.getCalDateTime(endDay, endTime);
            bk.setCalStartDate(calStartTime);
            bk.setCalEndDate(calEndTime);
        }
        if (bookingsForUser != null) {
            for (Book book : bookingsForUser) {
                String roomID = book.getRoomID();
                Workspace room = WorkspaceTable.getWorkspaceByID(roomID);

                if (room != null && book.getCalStartDate() != null) {
                    String name = room.getLongName();
                    Entry<String> newEntry = new Entry<>(name);
                    createEntryAt(book.getCalStartDate(), workspaces);
                    getWorkspaces().addEntry(newEntry);
                }
            }
        }
    }

    public void setLocations() {
        List<Book> bookingsForUser = BookLocationTable.getBookingsForUser(UserHelpers.getCurrentUser());
        for (Book bk : bookingsForUser) {
            String[] start = bk.getStartDate().split(" ");
            String[] end = bk.getEndDate().split(" ");
            String startDay = start[0];
            String startTime = start[1].substring(0, start[1].indexOf("."));
            String endDay = end[0];
            String endTime = end[1].substring(0, end[1].indexOf("."));
            ZonedDateTime calStartTime = DatabaseHelpers.getCalDateTime(startDay, startTime);
            ZonedDateTime calEndTime = DatabaseHelpers.getCalDateTime(endDay, endTime);
            bk.setCalStartDate(calStartTime);
            bk.setCalEndDate(calEndTime);
        }
        if (bookingsForUser != null) {
            for (Book book : bookingsForUser) {
                String roomID = book.getRoomID();
                Location room = LocationTable.getLocationByID(roomID);

                if (room != null && book.getCalStartDate() != null) {
                    String name = room.getLongName();
                    Entry<String> newEntry = new Entry<>(name);
                    createEntryAt(book.getCalStartDate(), locations);
                    getLocations().addEntry(newEntry);
                }
            }
        }
    }
}