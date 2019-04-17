package controllers.settings;

import com.jfoenix.controls.JFXComboBox;
import helpers.MapHelpers;
import javafx.fxml.Initializable;
import map.*;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    public JFXComboBox cmbAlgo;
    public String BFS, DFS, ASTAR, BEST, DIJKSTRA;

    private static PathContext algType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MapHelpers.Algorithm alg = algType.getAlg();
        switch (alg) {
            case ASTAR:
                cmbAlgo.setValue(ASTAR);
                break;
            case DFS:
                cmbAlgo.setValue(DFS);
                break;
            case BFS:
                cmbAlgo.setValue(BFS);
                break;
            case BEST:
                cmbAlgo.setValue(BEST);
                break;
            case DIJKSTRA:
                cmbAlgo.setValue(DIJKSTRA);
                break;
        }

        cmbAlgo.valueProperty().addListener(((observable, oldValue, newValue) -> {
            String str = (String) cmbAlgo.getValue();
            switch (str) {
                case "ASTAR":
                    algType = new PathContext(new AStar());
                    break;
                case "BFS":
                    algType = new PathContext(new BreadthSearch());
                    break;
                case "DFS":
                    algType = new PathContext(new DepthSearch());
                    break;
                case "BEST":
                    algType = new PathContext(new BestFirst());
                    break;
                default:
                    algType = new PathContext(new Dijkstra());
                    break;
            }
        }));
    }

    public static PathContext getAlgType() {
        return algType;
    }

    public static void setAlgType(PathContext algType) {
        SettingsController.algType = algType;
    }
}
