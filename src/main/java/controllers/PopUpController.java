package controllers;

import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import models.map.Location;
import models.map.Map;

public abstract class PopUpController implements Initializable {

    protected Location loc;
    protected Map map;
    protected AnchorPane[] panes;
    protected Circle circle;
    protected Location kiosk;

    public abstract void setLoc(Location loc);

    public final void setMap(Map map) {
        this.map = map;
        kiosk = map.getLocation(MapController.getTempStart());
    };

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public void setPanes(AnchorPane[] panes) {
        this.panes = panes;
    }
}
