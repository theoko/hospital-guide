package controllers;

import javafx.fxml.Initializable;
import javafx.scene.shape.Line;
import models.map.Location;
import models.map.Map;

import java.util.HashMap;

public abstract class PopUpController implements Initializable {

    protected Location loc;
    protected Map map;
    protected HashMap<String, Line> lstLines;

    public abstract void setLoc(Location loc);

    public final void setMap(Map map) {
        this.map = map;
    };

    public final void setLines(HashMap<String, Line> lstLines) {
        this.lstLines = lstLines;
    }
}
