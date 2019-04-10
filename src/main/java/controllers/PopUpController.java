package controllers;

import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import map.PathFinder;
import models.map.Location;
import models.map.Map;
import models.map.Workspace;
import models.user.User;

import javax.jws.soap.SOAPBinding;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public abstract class PopUpController implements Initializable {

    protected Location loc;
    protected Map map;
    protected AnchorPane[] panes;
    protected Location kiosk;
    protected Circle circle;
    protected List<Workspace> ws;
    protected LocalDate StartDate;
    protected LocalTime StartTime;
    protected LocalDate EndDate;
    protected LocalTime EndTime;

    public abstract void setLoc(Location loc);

    public final void setMap(Map map) {
        this.map = map;
        kiosk = map.getLocation(MapController.getTempStart());
    }

    public void setPanes(AnchorPane[] panes) {
        this.panes = panes;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public void setWorkspace(List<Workspace> ws) {
        this.ws = ws;
    }

    public void setStartDate(LocalDate StartDate) { this.StartDate = StartDate; }

    public void setStartTime(LocalTime StartTime) { this.StartTime = StartTime; }

    public void setEndDate(LocalDate EndDate) { this.EndDate = EndDate; }

    public void setEndTime(LocalTime EndTime) { this.EndTime = EndTime; }
}
