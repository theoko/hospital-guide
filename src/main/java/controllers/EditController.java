package controllers;

import com.jfoenix.controls.JFXComboBox;
import helpers.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.map.Location;

import java.net.URL;
import java.util.ResourceBundle;

public class EditController extends PopUpController implements Initializable {

    public JFXComboBox cmbNodeType;
    public String BATH;
    public String CONF;
    public String DEPT;
    public String ELEV;
    public String EXIT;
    public String HALL;
    public String INFO;
    public String LABS;
    public String REST;
    public String RETL;
    public String SERV;
    public String STAI;

    public void updateNode(MouseEvent event) {
        event.consume();
        String value = (String) cmbNodeType.getValue();
        String nType = value.substring(0, value.indexOf(':'));
        loc.setNodeType(Constants.NodeType.valueOf(nType));
        ScreenController.deactivate();
    }

    public void goBack(MouseEvent event) throws Exception{
       // ((Stage) (((Node) event.getSource()).getScene().getWindow())).close();
        event.consume();
        ScreenController.deactivate();
    }

    public void setLoc(Location loc) {
        this.loc = loc;
        switch (loc.getNodeType()) {
            case BATH:
                cmbNodeType.setValue(BATH);
                break;
            case SERV:
                cmbNodeType.setValue("");
                break;
            case RETL:
                cmbNodeType.setValue("");
                break;
            case REST:
                cmbNodeType.setValue("");
                break;
            case LABS:
                cmbNodeType.setValue("");
                break;
            case INFO:
                cmbNodeType.setValue("");
                break;
            case HALL:
                cmbNodeType.setValue("");
                break;
            case EXIT:
                cmbNodeType.setValue("");
                break;
            case ELEV:
                cmbNodeType.setValue("");
                break;
            case DEPT:
                cmbNodeType.setValue("");
                break;
            case CONF:
                cmbNodeType.setValue("");
                break;
            default:
                cmbNodeType.setValue(STAI);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
