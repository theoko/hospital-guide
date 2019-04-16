package controllers.booking;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.view.CalendarView;
import de.jensd.fx.glyphs.fontawesome.utils.FontAwesomeIconFactory;
import javafx.application.Platform;
import javafx.scene.layout.BorderPane;

import java.time.LocalDate;
import java.time.LocalTime;

public class DisplayCalendarController {

    public BorderPane panCalendar;

    public void initialize() {

        CalendarView calendarView = new CalendarView();
        FontAwesomeIconFactory.get();

        Calendar birthdays = new com.calendarfx.model.Calendar("Birthdays");
        Calendar holidays = new com.calendarfx.model.Calendar("Holidays");

        birthdays.setStyle(com.calendarfx.model.Calendar.Style.STYLE1);
        holidays.setStyle(com.calendarfx.model.Calendar.Style.STYLE2);

        CalendarSource myCalendarSource = new CalendarSource("My Calendars");
        myCalendarSource.getCalendars().addAll(birthdays, holidays);

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

        panCalendar.setPrefWidth(1200);
        panCalendar.setPrefHeight(656);
    }

}