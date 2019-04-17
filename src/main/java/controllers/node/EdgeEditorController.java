package controllers.node;

import com.jfoenix.controls.JFXButton;
import controllers.ScreenController;
import controllers.maps.MapController;
import database.EdgeTable;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import models.map.Edge;
import models.map.Map;

import java.net.URL;
import java.util.ResourceBundle;

public class EdgeEditorController implements Initializable {
    public JFXButton btnDelete;
    public JFXButton btnCancel;

    private MapController mc;
    private String edgeId;
    private Map map;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public void setMc(MapController mc) {
        this.mc = mc;
    }

    public void setEdgeId(String edgeId) {
        this.edgeId = edgeId;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void btnDelete_Click(MouseEvent mouseEvent) {
        Edge edge = map.getEdge(edgeId);
        EdgeTable.deleteEdge(edge);
        Node line = null;
        for (Node n : mc.panMap.getChildren()) {
            if (n instanceof Line && n.getId() != null && n.getId().equals(edgeId)) {
                line = n;
            }
        }
        if (line != null) {
            mc.panMap.getChildren().remove(line);
        }
        mc.panMap.getChildren().remove(line);
        ScreenController.deactivate();
    }

    public void btnCancel_Click(MouseEvent mouseEvent) {
        ScreenController.deactivate();
    }
}
