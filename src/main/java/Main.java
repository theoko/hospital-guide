import controllers.ScreenController;
import controllers.SettingsController;
import database.Database;
import helpers.FileHelpers;
import javafx.application.Application;
import javafx.stage.Stage;
import database.CSVParser;
import map.AStar;
import map.PathContext;
import map.PathFinder;
import models.search.SearchKeywords;

public class Main extends Application {
    static ScreenController screenController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // Initialize database
        if(!Database.getDatabase().databaseExists()) {
            CSVParser.parse(FileHelpers.getNodesCSV(), FileHelpers.getEdgesCSV(), FileHelpers.getWorkspacesCSV());
        }

        // Initialize keywords for search engine
        SearchKeywords.initialize();

        // Initialize screen controller
        screenController = new ScreenController(primaryStage);

        PathFinder.setDefLocation("HLABS00103");
        SettingsController.setAlgType(new PathContext(new AStar()));
    }

}
