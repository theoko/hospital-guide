package controllers.booking;

import controllers.ScreenController;
import controllers.node.PopUpController;
import database.BookWorkspaceTable;
import helpers.DatabaseHelpers;
import helpers.UserHelpers;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import models.map.Location;
import models.map.Workspace;
import models.room.Book;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

public class WorkspacePopUpController extends PopUpController implements Initializable {

    Circle circle;
    Workspace ws;
    Workspace booking;
    Workspace enter;
    int a = 0;
    LocalDate StartDate;
    LocalTime StartTime;
    LocalDate EndDate;
    LocalTime EndTime;
    private final static double xShift = 20;
    private final static double yShift = -262;
    private final static double scale = 0.44;

    public Label lblNodeID;
    public Label lblLocation;
    public Label lblLongName;


    public void bookWorkspace(MouseEvent event) {
        event.consume();
        Book book = new Book(
                a,
                ws.getNodeID(),
                UserHelpers.getCurrentUser(),
                DatabaseHelpers.getDateTime(StartDate, StartTime),
                DatabaseHelpers.getDateTime(EndDate, EndTime)
        );
//        ZonedDateTime calStartTime = DatabaseHelpers.getCalDateTime(StartDate, StartTime);
//        ZonedDateTime calEndTime = DatabaseHelpers.getCalDateTime(EndDate, EndTime);
//        book.setCalStartDate(calStartTime);
//        book.setCalEndDate(calEndTime);
        BookWorkspaceTable.createBooking(book);
        circle.setFill(Color.ORANGE);
        ScreenController.deactivate();
    }

    public void removeBooking(MouseEvent event) {
        event.consume();
        Book remove = BookWorkspaceTable.getBookByRoomID(ws.getNodeID());
        BookWorkspaceTable.deleteWorkspaceBook(remove);
        circle.setFill(Color.YELLOW);
        ScreenController.deactivate();
    }

    public void goBack(MouseEvent event) {
        event.consume();
        ScreenController.deactivate();
    }

    public void setLoc(Location loc) {
    }

    public void setWorkspace(Workspace ws) {
        this.ws = ws;
        lblNodeID.setText(ws.getNodeID());
        lblLocation.setText("(" + ws.getxCord() + ", " + ws.getyCord() + ")");
        lblLongName.setText(ws.getLongName());
    }

    public void setCircle(Circle circle) {
        this.circle = circle;

    }

    public void setStartDate(LocalDate StartDate) {
        this.StartDate = StartDate;
    }

    public void setStartTime(LocalTime StartTime) {
        this.StartTime = StartTime;
    }

    public void setEndDate(LocalDate EndDate) {
        this.EndDate = EndDate;
    }

    public void setEndTime(LocalTime EndTime) {
        this.EndTime = EndTime;
    }

    public static double scaleX(double x) {
        return (x - xShift) * scale;
    }

    public static double scaleY(double y) {
        return (y - yShift) * scale;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
