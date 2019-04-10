package controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import database.LocationTable;
import helpers.Constants;
import helpers.UIHelpers;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import models.map.Location;

import java.net.URL;
import java.util.ResourceBundle;

public class WorkspacePopUpController implements Initializable {

 /*   public JFXComboBox cmbNodeType, cmbNodeType1;
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
  /*  public void displayBooking(MouseEvent event) {
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
   /* public void setLoc(Location loc) {
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


    } */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        bookingButton.setText(Constants.BOOKING_BUTTON_TEXT);
    }
}