import controllers.ScreenController;
import database.Database;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    Database db;
    static ScreenController screenController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        db = new Database();

        screenController = new ScreenController(primaryStage);

    }

}
