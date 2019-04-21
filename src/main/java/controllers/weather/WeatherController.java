package controllers.weather;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

public class WeatherController {

    public Label lblTemp;
    public ImageView imgType;
    private int minute;
    private int hour;
    public Label lblTime;

    private final String weatherURL = "https://api.openweathermap.org/data/2.5/weather?q=Boston&appid=0021f7b296482eecb5c71c4f138cf484";

    public void initialize() {
        try {
            StringBuilder result = new StringBuilder();
            URL url = new URL(weatherURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();

            String type = this.getKey(this.getKeyFromArray(this.getKey(result.toString(), "weather"), 0), "main");
            System.out.println(type);
            switch (type) {
                case "\"Clear\"":
                    imgType.setImage(new Image("images/Weather/sun.png"));
                    break;
                case "\"Clouds\"":
                    imgType.setImage(new Image("images/Weather/cloud.png"));
                    break;
                case "\"Drizzle\"":
                    imgType.setImage(new Image("images/Weather/drizzle.png"));
                    break;
                case "\"Rain\"":
                    imgType.setImage(new Image("images/Weather/rain.png"));
                    break;
                case "\"Thunderstorm\"":
                    imgType.setImage(new Image("images/Weather/thunder.png"));
                    break;
                case "\"Snow\"":
                    imgType.setImage(new Image("images/Weather/snow.png"));
                    break;
                default:
                    imgType.setImage(new Image("images/Weather/haze.png"));
            }

            double tempK = Double.valueOf(this.getKey(this.getKey(result.toString(), "main"), "temp"));
            int tempF = (int)((tempK - 273) * 1.8 + 32);
            lblTemp.setText(String.valueOf(tempF));


            Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
                minute = LocalDateTime.now().getMinute();
                hour = LocalDateTime.now().getHour();
                if(hour < 10 && minute < 10)
                    lblTime.setText("0" + hour + ":" + "0" + (minute));
                else if(hour >= 10 && minute < 10)
                    lblTime.setText(hour + ":" + "0" + (minute));
                else if(hour < 10)
                    lblTime.setText("0" + hour + ":" + (minute));
                else
                    lblTime.setText(hour + ":" + (minute));
            }),
                    new KeyFrame(Duration.seconds(1))
            );
            clock.setCycleCount(Animation.INDEFINITE);
            clock.play();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getKey(String jsonString, String key) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, JsonObject.class).get(key).toString();
    }

    private String getKeyFromArray(String jsonString, int index) {
        Gson gson = new Gson();
        JsonArray jsonObject = gson.fromJson(jsonString, JsonArray.class);
        return jsonObject.get(index).toString();
    }

}