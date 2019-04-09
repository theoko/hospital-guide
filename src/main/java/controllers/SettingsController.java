package controllers;

import com.jfoenix.controls.JFXComboBox;
import helpers.MapHelpers;
import javafx.fxml.Initializable;
import map.AStar;
import map.BreadthSearch;
import map.DepthSearch;
import map.PathContext;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    public JFXComboBox cmbAlgo;
    public String BFS;
    public String DFS;
    public String ASTAR;

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
                default:
                    algType = new PathContext(new DepthSearch());
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
