package controllers.welcome;

import controllers.ScreenController;
import helpers.Constants;
import helpers.FileHelpers;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogoController implements Initializable {

    @FXML
    private MediaView logoMv;
    private MediaPlayer mP;
    private Media logoMe;

    public void playVid() {

        String pathToZIP = "/images/movie.zip";
        String video = null;
        try {
            video = FileHelpers.extractFolder(pathToZIP, "movie.zip");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String videoPath = FileHelpers.unzipFile(video, "movie.mp4");
        if (videoPath == null) {
            try {
                ScreenController.activate(Constants.Routes.WELCOME);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logoMe = new Media(new File(videoPath).toURI().toString());
        mP = new MediaPlayer(logoMe);
        logoMv.setMediaPlayer(mP);
        mP.setAutoPlay(true);
    /*DoubleProperty width = logoMv.fitWidthProperty();
    DoubleProperty height = logoMv.fitHeightProperty();
    width.bind(Bindings.selectDouble(logoMv.sceneProperty(),"width"));
    height.bind(Bindings.selectDouble(logoMv.sceneProperty(),"height"));*/
        mP.setOnEndOfMedia(() -> {
            try {
                ScreenController.activate(Constants.Routes.WELCOME);
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        });

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            if (FileHelpers.checkJar()) {
                playVid();
            } else {
                try {
                    ScreenController.activate(Constants.Routes.WELCOME);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
