import controllers.ScreenController;
import controllers.SettingsController;
import database.Database;
import helpers.FileHelpers;
import images.ImageFactory;
import javafx.application.Application;
import javafx.stage.Stage;
import database.CSVParser;
import map.AStar;
import map.DepthSearch;
import map.PathContext;
import map.PathFinder;

public class Main extends Application {
    static ScreenController screenController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ImageFactory.load();

        if(!Database.getDatabase().databaseExists()) {
            CSVParser.parse(FileHelpers.getNodesCSV(), FileHelpers.getEdgesCSV(), FileHelpers.getWorkspacesCSV());
        }

        screenController = new ScreenController(primaryStage);

        PathFinder.setDefLocation("HLABS00103");
        SettingsController.setAlgType(new PathContext(new AStar()));
    }

}
