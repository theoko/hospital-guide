package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import database.Database;
import database.LocationTable;
import helpers.Constants;
import helpers.UIHelpers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import map.MapDisplay;
import map.PathFinder;
import models.map.Location;
import org.omg.DynamicAny.DynArray;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EditController extends PopUpController implements Initializable {

    public JFXComboBox cmbNodeType, cmbNodeType1;
    public JFXTextField NameField;

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
    public JFXButton btnDefault;
    //public String WORK;
   // public String WRKT;

//    public JFXButton bookingButton;

    public void updateNode(MouseEvent event) {
        event.consume();
        try {
            String name = (String) NameField.getText();
            loc.setLongName(name);
            loc.setShortName(name);
        } catch(Exception e) {
            e.printStackTrace();
        }
        String value = (String) cmbNodeType.getValue();
        String building = (String) cmbNodeType1.getValue();
        loc.setBuilding(building);
        String nType = value.substring(0, value.indexOf(':'));
        loc.setNodeType(Constants.NodeType.valueOf(nType));
        if (loc.getNodeID() == null) {
            loc.setNodeID(LocationTable.uniqueID(loc));
            System.out.println(loc.getNodeID());
        }
//        VisualRealtimeController.updateCircle(loc.getNodeCircle(),
//                UIHelpers.updateCircleForNodeType(loc));
        loc.setNodeCircle(UIHelpers.updateCircleForNodeType(loc));
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
        if(AdminMapController.isEnableAddNode()) {
            loc.deleteCurrNode();
        }
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
        cmbNodeType1.setValue(loc.getBuilding());
        NameField.setText(loc.getShortName());
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
                break;
          //  case WORK:
            //    cmbNodeType.setValue(WORK);
           //     break;
         //  case WRKT:
         //       cmbNodeType.setValue(WRKT);

                // Set button visibility to true since a conference room node
                // is selected and the room can be booked
//                bookingButton.setVisible(true);

               // break;
            default:
                cmbNodeType.setValue(STAI);
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        bookingButton.setText(Constants.BOOKING_BUTTON_TEXT);
    }

    public void btnDefault_Click(MouseEvent mouseEvent) {
        PathFinder.setDefLocation(loc.getNodeID());
        for (AnchorPane pane : panes) {
            List<Node> lstNodes1 = new ArrayList<>();
            for (Node n : pane.getChildren()) {
                if (n instanceof Circle) {
                    Circle circle = (Circle) n;
                    if (circle.getFill().equals(MapDisplay.nodeStart)) {
                        circle.setFill(MapDisplay.nodeFill);
                    }
                }
            }
        }
        circle.setFill(MapDisplay.nodeStart);
        ScreenController.deactivate();
    }
}
