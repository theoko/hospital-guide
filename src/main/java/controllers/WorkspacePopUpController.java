package controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import database.BookWorkspaceTable;
import database.BookWorkspaceTable;
import database.LocationTable;
import database.WorkspaceTable;
import helpers.Constants;
import helpers.DatabaseHelpers;
import helpers.UIHelpers;
import helpers.UserHelpers;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import models.map.Location;
import models.map.Workspace;
import models.room.Book;
import models.user.User;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class WorkspacePopUpController extends PopUpController implements Initializable {

    Circle circle;
    List<Workspace> ws;
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
        for(Workspace ws1 : ws) {
            if(scaleX(ws1.getxCord()) == circle.getCenterX() && scaleY(ws1.getyCord()) == circle.getCenterY()) {
                booking = ws1;
                break;
            }
        }
        Book book = new Book(
                a,
                booking.getNodeID(),
                UserHelpers.getCurrentUser(),
                DatabaseHelpers.getDateTime(StartDate, StartTime),
                DatabaseHelpers.getDateTime(EndDate, EndTime)
        );
       BookWorkspaceTable.createBooking(book);
       circle.setFill(Color.RED);
       ScreenController.deactivate();
     }

    public void goBack(MouseEvent event) {
        event.consume();
        circle.setFill(Color.YELLOW);
        ScreenController.deactivate();
    }

    public void setLoc(Location loc) {
    }

    public void setWorkspace(List<Workspace> ws) {
        this.ws = ws;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
        for(Workspace ws1 : ws) {
            if(scaleX(ws1.getxCord()) == circle.getCenterX() && scaleY(ws1.getyCord()) == circle.getCenterY()) {
                enter = ws1;
                break;
            }
        }
        lblNodeID.setText(enter.getNodeID());
        lblLocation.setText("(" + enter.getxCord() + ", " + enter.getyCord() + ")");
        lblLongName.setText(enter.getLongName());
    }

    public void setStartDate(LocalDate StartDate) {
        this.StartDate = StartDate; }

    public void setStartTime(LocalTime StartTime) {
        this.StartTime = StartTime; }

    public void setEndDate(LocalDate EndDate) {
        this.EndDate = EndDate; }

    public void setEndTime(LocalTime EndTime) {
        this.EndTime = EndTime; }

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
