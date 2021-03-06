import controllers.ScreenController;
import controllers.booking.DisplayCalendarController;
import controllers.settings.SettingsController;
import database.Database;
import database.SanitationTable;
import database.UserTable;
import google.FirebaseAPI;
import helpers.FileHelpers;
import messaging.TextMessenger;
import images.ImageFactory;
import javafx.application.Application;
import javafx.stage.Stage;
import database.CSVParser;
import map.AStar;
import map.PathContext;
import map.PathFinder;
import models.search.SearchKeywords;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {
    static ScreenController screenController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ImageFactory.load();

        // Initialize database
        if(!Database.getDatabase().databaseExists()) {
            CSVParser.parse(FileHelpers.getNodesCSV(), FileHelpers.getEdgesCSV());

            // Seed user table
            UserTable.seed();

            // Seed sanitation table
            SanitationTable.seed();
        }

        // Initialize keywords for search engine
        SearchKeywords.initialize();

        // Initialize firebase API
        FirebaseAPI firebaseAPI = new FirebaseAPI();

        // Initialize screen controller
        screenController = new ScreenController(primaryStage);

        PathFinder.setDefLocation("FEXIT00201");
        SettingsController.setAlgType(new PathContext(new AStar()));
        (new TextMessenger()).sendMessage();
    }

}