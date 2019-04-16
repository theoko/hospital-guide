package controllers.booking;

import database.BookLocationTable;
import database.BookWorkspaceTable;
import database.LocationTable;
import helpers.UIHelpers;
import helpers.UserHelpers;
import javafx.scene.layout.BorderPane;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;
import models.map.Location;
import models.room.Book;

import java.util.Calendar;
import java.util.List;

public class DisplayCalendarController {

    public BorderPane panCalendar;
    private Calendar workspaces;
    private Calendar rooms;
    private VCalendar vCalendar;
    ICalendarAgenda agenda;

//    public DisplayCalendarController(Calendar workspaces, Calendar rooms) {
//        this.workspaces = workspaces;
//        this.rooms = rooms;
//    }

    public void initialize() {

        vCalendar = new VCalendar();
        agenda = new ICalendarAgenda(vCalendar);
//
//        String publishMessage = "BEGIN:VCALENDAR" + System.lineSeparator() +
//                "METHOD:PUBLISH" + System.lineSeparator() +
//                "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
//                "VERSION:2.0" + System.lineSeparator() +
//                "BEGIN:VEVENT" + System.lineSeparator() +
//                "ORGANIZER:mailto:a@example.com" + System.lineSeparator() +
//                "DTSTART:20190401T200000Z" + System.lineSeparator() +
//                "DTEND:20190401T210000Z" + System.lineSeparator() +
//                "DTSTAMP:20150611T190000Z" + System.lineSeparator() +
//                "RRULE:FREQ=WEEKLY;BYDAY=FR" + System.lineSeparator() +
//                "SUMMARY:Friday meeting with Joe" + System.lineSeparator() +
//                "UID:0981234-1234234-23@example.com" + System.lineSeparator() +
//                "END:VEVENT" + System.lineSeparator() +
//                "END:VCALENDAR";
//
//        vCalendar.processITIPMessage(publishMessage);

        panCalendar.setPrefWidth(1200);
        panCalendar.setPrefHeight(656);


        panCalendar.setCenter(agenda);

        setEntries();
    }

// String nodeID, int xCord, int yCord, String floor, String building, Constants.NodeType nodeType, String longName, String shortName
// String nodeID, int xCord, int yCord, Constants.NodeType nodeType, String longName
// int bookingID, String roomID, User user, String startDate, String endDate (book)
    public void displayWorkspaceEntries(String message){
        List<Book> bookingsWorkspace = BookWorkspaceTable.getBookingsForUser(UserHelpers.getCurrentUser());
        for (Book book: bookingsWorkspace) {
            String startDate = book.getCalStartDate();
            String endDate = book.getCalEndDate();

            String publishMessage = "BEGIN:VCALENDAR" + System.lineSeparator() +
                    "METHOD:PUBLISH" + System.lineSeparator() +
                    "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                    "VERSION:2.0" + System.lineSeparator() +
                    "BEGIN:VEVENT" + System.lineSeparator() +
                    "ORGANIZER:mailto:a@example.com" + System.lineSeparator() +
                    "DTSTART:" + startDate + System.lineSeparator() +
                    "DTEND:" + endDate + System.lineSeparator() +
                    "DTSTAMP:20150611T190000Z" + System.lineSeparator() +
                    "RRULE:FREQ=WEEKLY;BYDAY=FR" + System.lineSeparator() +
                    "SUMMARY:" + message + System.lineSeparator() +
                    "UID:0981234-1234234-23@example.com" + System.lineSeparator() +
                    "END:VEVENT" + System.lineSeparator() +
                    "END:VCALENDAR";

            getvCalendar().processITIPMessage(publishMessage);
        }
    }

    public void displayRoomEntries(String message){
        List<Book> bookingsWorkspace = BookWorkspaceTable.getBookingsForUser(UserHelpers.getCurrentUser());
        for (Book book: bookingsWorkspace) {
            String startDate = book.getCalStartDate();
            String endDate = book.getCalEndDate();

            String publishMessage = "BEGIN:VCALENDAR" + System.lineSeparator() +
                    "METHOD:PUBLISH" + System.lineSeparator() +
                    "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() +
                    "VERSION:2.0" + System.lineSeparator() +
                    "BEGIN:VEVENT" + System.lineSeparator() +
                    "ORGANIZER:mailto:a@example.com" + System.lineSeparator() +
                    "DTSTART:" + startDate + System.lineSeparator() +
                    "DTEND:" + endDate + System.lineSeparator() +
                    "DTSTAMP:20150611T190000Z" + System.lineSeparator() +
                    "RRULE:FREQ=WEEKLY;BYDAY=FR" + System.lineSeparator() +
                    "SUMMARY:" + message + System.lineSeparator() +
                    "UID:0981234-1234234-23@example.com" + System.lineSeparator() +
                    "END:VEVENT" + System.lineSeparator() +
                    "END:VCALENDAR";

            getvCalendar().processITIPMessage(publishMessage);
        }
    }

    public Calendar getWorkspaces() {
        return workspaces;
    }

    public void setWorkspaces(Calendar workspaces) {
        this.workspaces = workspaces;
    }

    public Calendar getRooms() {
        return rooms;
    }

    public void setRooms(Calendar rooms) {
        this.rooms = rooms;
    }

    public VCalendar getvCalendar() {
        return vCalendar;
    }

    public void setvCalendar(VCalendar vCalendar) {
        this.vCalendar = vCalendar;
    }

    public ICalendarAgenda getAgenda() {
        return agenda;
    }

    public void setAgenda(ICalendarAgenda agenda) {
        this.agenda = agenda;
    }

    private void setEntries(){
        List<Book> bookingsLocation = BookLocationTable.getBookingsForUser(UserHelpers.getCurrentUser());
        List<Book> bookingsWorkspace = BookWorkspaceTable.getBookingsForUser(UserHelpers.getCurrentUser());
        for (Book l: bookingsLocation) {
            String roomID = l.getRoomID();
            Location location = LocationTable.getLocationByID(roomID);
            String name = location.getLongName();
        }
//        for (Book w: bookingsWorkspace) {
//            String roomID = w.getRoomID();
//            Location location = LocationTable.getLocationByID(roomID);
//            String name = location.getLongName();
//        }
    }
}