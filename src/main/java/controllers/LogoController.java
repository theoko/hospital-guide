package controllers;

import helpers.Constants;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.WindowEvent;
import java.io.File;

public class LogoController {

    @FXML
    private MediaView logoMv;
    private MediaPlayer mP;
    private Media logoMe;

    public void playVid(MouseEvent event) {
        event.consume();
        String path = new File("src/main/resources/images/movie.mp4").getAbsolutePath();
        logoMe = new Media(new File(path).toURI().toString());
        mP = new MediaPlayer(logoMe);
        logoMv.setMediaPlayer(mP);
        mP.setAutoPlay(true);
        /*DoubleProperty width = logoMv.fitWidthProperty();
        DoubleProperty height = logoMv.fitHeightProperty();
        width.bind(Bindings.selectDouble(logoMv.sceneProperty(),"width"));
        height.bind(Bindings.selectDouble(logoMv.sceneProperty(),"height"));*/
        mP.setOnEndOfMedia(() -> {
            try {
                ScreenController.deactivate();
                ScreenController.activate(Constants.Routes.WELCOME);
            }
            catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        });
    }
}
