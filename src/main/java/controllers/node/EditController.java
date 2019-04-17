package controllers.node;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controllers.ScreenController;
import controllers.maps.AdminMapController;
import controllers.maps.MapController;
import database.LocationTable;
import helpers.Constants;
import helpers.UIHelpers;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import map.MapDisplay;
import map.PathFinder;
import models.map.Location;
import models.map.SubPath;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

public class EditController extends PopUpController {

    public JFXComboBox cmbNodeType, cmbBuilding;
    public JFXTextField txtName;
    public String BATH, CONF, DEPT, ELEV, EXIT, HALL, INFO, LABS, REST, RETL, SERV, STAI;
    public JFXButton btnDefault, btnDelete;
    public JFXButton btnAddEdge;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
    }

    public void updateNode(MouseEvent event) {
        String strType = (String) cmbNodeType.getValue();
        Constants.NodeType nodeType = Constants.NodeType.valueOf((strType).substring(0, strType.indexOf(":")));
        loc.setNodeType(nodeType);
        String building = (String) cmbBuilding.getValue();
        loc.setBuilding(building);
        String shortName = txtName.getText();
        loc.setShortName(shortName);
        LocationTable.updateLocation(loc);
        ScreenController.deactivate();
    }

    public void goBack(MouseEvent event) throws Exception{
        ScreenController.deactivate();
    }

    public void deleteNode(MouseEvent event) {
        String locID = loc.getNodeID();
        List<Node> lstNodes = new ArrayList<>();
        for (Node n : mc.panMap.getChildren()) {
            if (n instanceof Circle && n.getId() != null && n.getId().equals(locID)) {
                lstNodes.add(n);
            }
            for (SubPath sp : loc.getSubPaths()) {
                if (n instanceof Line && n.getId().equals(sp.getEdgeID())) {
                    lstNodes.add(n);
                }
            }
        }
        mc.panMap.getChildren().removeAll(lstNodes);
        LocationTable.deleteLocation(loc);
        ScreenController.deactivate();
    }

    public void setLoc(Location loc) {
        this.loc = loc;
        btnDefault.setDisable(loc.getNodeID().equals(PathFinder.getDefLocation()));
        btnDelete.setDisable(loc.getNodeID().equals(PathFinder.getDefLocation()));
        cmbBuilding.setValue(loc.getBuilding());
        txtName.setText(loc.getShortName());
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
            default:
                cmbNodeType.setValue(STAI);
        }
    }

    public void btnDefault_Click(MouseEvent mouseEvent) {
        for (Node n : mc.panMap.getChildren()) {
            if (n instanceof Circle) {
                Circle old = (Circle) n;
                if (old.getFill().equals(MapDisplay.nodeStart)) {
                    old.setFill(MapDisplay.nodeFill);
                }
            }
        }
        PathFinder.setDefLocation(loc.getNodeID());
        loc.getNodeCircle().setFill(MapDisplay.nodeStart);
        ScreenController.deactivate();
    }

    public void btnAddEdge_Click(MouseEvent mouseEvent) {
        AdminMapController.setEdgLoc(loc);
        loc.getNodeCircle().setFill(MapDisplay.nodeEnd);
        ScreenController.deactivate();
    }
}
