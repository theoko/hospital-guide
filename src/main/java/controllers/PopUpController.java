package controllers;

import javafx.fxml.Initializable;
import models.map.Location;

public abstract class PopUpController implements Initializable {

    protected Location loc;

    public abstract void setLoc(Location loc);

}
