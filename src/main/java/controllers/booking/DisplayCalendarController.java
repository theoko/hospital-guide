package controllers.booking;

import javafx.scene.layout.BorderPane;
import jfxtras.icalendarfx.VCalendar;
import jfxtras.scene.control.agenda.icalendar.ICalendarAgenda;

public class DisplayCalendarController {

    public BorderPane panCalendar;
//    private Calendar workspaces;
//    private Calendar rooms;

//    public DisplayCalendarController(Calendar workspaces, Calendar rooms) {
//        this.workspaces = workspaces;
//        this.rooms = rooms;
//    }

    public void initialize() {
        System.out.println("initializing............");

        VCalendar vCalendar = new VCalendar();
        ICalendarAgenda agenda = new ICalendarAgenda(vCalendar);

        panCalendar.setCenter(agenda);

//        CalendarView calendarView = new CalendarView();
//        workspaces = new Calendar("Workspaces");
//        rooms = new Calendar("Rooms");

//        workspaces.setStyle(Style.STYLE1);
//        rooms.setStyle(Style.STYLE2);

//        CalendarSource calendarSource = new CalendarSource("Calendars");
//        calendarSource.getCalendars().addAll(workspaces, rooms);
//
//        calendarView.getCalendarSources().addAll(calendarSource);
//        calendarView.setRequestedTime(LocalTime.now());

//        Thread updateTimeThread = new Thread("Calendar: Update Time Thread"){
//            @Override
//            public void run(){
//                while (true){
//                    Platform.runLater(() -> {
//                        calendarView.setToday(LocalDate.now());
//                        calendarView.setTime(LocalTime.now());
//                    });
//
//                    try {
//                        sleep(10000);
//                    } catch (InterruptedException e){
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };

//        updateTimeThread.setPriority(Thread.MAX_PRIORITY);
//        updateTimeThread.setDaemon(true);
//        updateTimeThread.start();


//        setEntries();

//        panCalendar.getChildren().add(calendarView);
    }

//    public Calendar getWorkspaces() {
//        return workspaces;
//    }
//
//    public void setWorkspaces(Calendar workspaces) {
//        this.workspaces = workspaces;
//    }
//
//    public Calendar getRooms() {
//        return rooms;
//    }
//
//    public void setRooms(Calendar rooms) {
//        this.rooms = rooms;
//    }
//
//    private void setEntries(){
//        List<Book> bookingsLocation = BookLocationTable.getBookingsForUser(UserHelpers.getCurrentUser());
//        List<Book> bookingsWorkspace = BookWorkspaceTable.getBookingsForUser(UserHelpers.getCurrentUser());
//        for (Book l: bookingsLocation) {
//            String roomID = l.getRoomID();
//            Location location = LocationTable.getLocationByID(roomID);
//            String name = location.getLongName();
//            Entry<String> newEntry = new Entry<>(name);
//            getRooms().addEntry(newEntry);
//        }
//        for (Book w: bookingsWorkspace) {
//            String roomID = w.getRoomID();
//            Location location = LocationTable.getLocationByID(roomID);
//            String name = location.getLongName();
//            Entry<String> newEntry = new Entry<>(name);
//            getWorkspaces().addEntry(newEntry);
//        }
//    }
}
