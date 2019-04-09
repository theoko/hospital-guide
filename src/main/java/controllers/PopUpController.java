package controllers;

import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import map.PathFinder;
import models.map.Location;
import models.map.Map;
import models.user.User;

import javax.jws.soap.SOAPBinding;

public abstract class PopUpController implements Initializable {

    protected Location loc;
    protected Map map;
    protected AnchorPane[] panes;
    protected Location kiosk;

    public abstract void setLoc(Location loc);

    public final void setMap(Map map) {
        this.map = map;
        kiosk = map.getLocation(MapController.getTempStart());
    };

    public void setPanes(AnchorPane[] panes) {
        this.panes = panes;
    }
}
