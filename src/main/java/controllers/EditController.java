package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import database.Database;
import helpers.Constants;
import helpers.UIHelpers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import models.map.Location;
import org.omg.DynamicAny.DynArray;

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

//    public JFXButton bookingButton;

    public void updateNode(MouseEvent event) {
        event.consume();
        String value = (String) cmbNodeType.getValue();
        String nType = value.substring(0, value.indexOf(':'));
        loc.setNodeType(Constants.NodeType.valueOf(nType));
        if (loc.getNodeID() == null) {
            loc.setNodeID(Database.addNewLocation(loc));
            System.out.println(loc.getNodeID());
        }
        VisualRealtimeController.updateCircle(loc.getNodeCircle(),
                UIHelpers.updateCircleForNodeType(loc));
        ScreenController.deactivate();
    }

    /**
     * Displays a new window for the user to make booking requests
     * for the currently selected node
     * @param event
     */
    public void displayBooking(MouseEvent event) {
        String conferenceRoomName = loc.getLongName();


        ScreenController.deactivate();
    }

    public void goBack(MouseEvent event) throws Exception{
       // ((Stage) (((Node) event.getSource()).getScene().getWindow())).close();
        event.consume();
        //if(loc.getNodeID() == null) Database.addNewLocation(loc);
        ScreenController.deactivate();
    }

    public void deleteNode(MouseEvent event) {
        event.consume();
        loc.deleteCurrNode();
        ScreenController.deactivate();
    }

    /**
     * Sets the value of the location variable to the selected location type
     * @param loc
     */
    public void setLoc(Location loc) {
        this.loc = loc;
        switch (loc.getNodeType()) {
            case BATH:
                cmbNodeType.setValue(BATH);
                break;
            case SERV:
                cmbNodeType.setValue(SERV);
                break;
            case RETL:
                cmbNodeType.setValue(RETL);
                break;
            case REST:
                cmbNodeType.setValue(REST);
                break;
            case LABS:
                cmbNodeType.setValue(LABS);
                break;
            case INFO:
                cmbNodeType.setValue(INFO);
                break;
            case HALL:
                cmbNodeType.setValue(HALL);
                break;
            case EXIT:
                cmbNodeType.setValue(EXIT);
                break;
            case ELEV:
                cmbNodeType.setValue(ELEV);
                break;
            case DEPT:
                cmbNodeType.setValue(DEPT);
                break;
            case CONF:
                cmbNodeType.setValue(CONF);

                // Set button visibility to true since a conference room node
                // is selected and the room can be booked
//                bookingButton.setVisible(true);

                break;
            default:
                cmbNodeType.setValue(STAI);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        bookingButton.setText(Constants.BOOKING_BUTTON_TEXT);
    }
}
