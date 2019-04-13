package controllers.user;

import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import models.map.Location;
import models.map.Map;
import models.user.User;

public abstract class PopUpControllerUser implements Initializable {

    protected User userM;

    public abstract void setUser(User user);
}
