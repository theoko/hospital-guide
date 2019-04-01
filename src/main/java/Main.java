import controllers.ScreenController;
import helpers.Constants;
import helpers.FileHelpers;
import javafx.application.Application;
import javafx.stage.Stage;
import database.CSVParser;

import java.io.File;

public class Main extends Application {
    static ScreenController screenController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        File dbDirectory = new File(Constants.DB_NAME.replaceAll("%20", " "));

        if(dbDirectory.exists()) {
            System.out.println("Exists!");
        } else {
            System.out.println("Need to create the database!");
        }

        CSVParser.parse(FileHelpers.getNodesCSV(), FileHelpers.getEdgesCSV());

        screenController = new ScreenController(primaryStage);

    }

}
