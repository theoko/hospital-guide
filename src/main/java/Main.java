import controllers.ScreenController;
import database.Database;
import helpers.Constants;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    Database db;
    static ScreenController screenController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        File dbDirectory = new File(Constants.DB_NAME.replaceAll("20%", " s"));
        if(dbDirectory.exists()){
            System.out.println("Exists!");
        } else{
            System.out.println("Need to create Database");
        }


        db = new Database();

        screenController = new ScreenController(primaryStage);

    }

}
