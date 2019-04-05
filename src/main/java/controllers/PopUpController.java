package controllers;

import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import models.map.Location;
import models.map.Map;

public abstract class PopUpController implements Initializable {

    protected Location loc;
    protected Map map;
    protected AnchorPane pane1;
    protected AnchorPane pane2;
    protected AnchorPane pane3;
    protected AnchorPane pane4;
    protected AnchorPane pane5;

    public abstract void setLoc(Location loc);

    public final void setMap(Map map) {
        this.map = map;
    };

    public void setPane1(AnchorPane pane1) {
        this.pane1 = pane1;
    }

    public void setPane2(AnchorPane pane2) {
        this.pane2 = pane2;
    }

    public void setPane3(AnchorPane pane3) {
        this.pane3 = pane3;
    }

    public void setPane4(AnchorPane pane4) {
        this.pane4 = pane4;
    }

    public void setPane5(AnchorPane pane5) {
        this.pane5 = pane5;
    }
}
