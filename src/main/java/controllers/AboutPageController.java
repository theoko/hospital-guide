package controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class AboutPageController {

    public ScrollPane scrollPane;

    public void initialize() {

        scrollPane.setPadding(new Insets(50, 50, 50, 50));

        VBox vbox1 = new VBox();
        vbox1.setPadding(new Insets(25, 25, 25, 25));
        vbox1.setSpacing(5);
        vbox1.setPrefHeight(1000);
        vbox1.setPrefWidth(1200);
        scrollPane.setContent(vbox1);

        AnchorPane anchorPane1 = new AnchorPane();
        anchorPane1.setPrefWidth(250);
        anchorPane1.setPrefHeight(150);
        ImageView imgTheo = new ImageView();
        imgTheo.setImage(new Image("images/teamPics/theo.png"));
        imgTheo.setFitHeight(200);
        imgTheo.setFitWidth(250);
        imgTheo.setPreserveRatio(true);
        imgTheo.setPickOnBounds(true);
        //imgTheo.setRotate(-);
        anchorPane1.getChildren().add(imgTheo);

        AnchorPane anchorLbl1 = new AnchorPane();
        anchorLbl1.setPrefWidth(200);
        anchorLbl1.setPrefHeight(60);
        anchorLbl1.setStyle("-fx-background-color: #022D5A;  -fx-background-radius: 25;");

        Label lbl1 = new Label();
        lbl1.setText("Theo Konstantapoulos");
        lbl1.setFont(new Font(18));
        lbl1.setTextFill(Color.WHITE);
        lbl1.setStyle("-fx-background-radius: ;");
        lbl1.setAlignment(Pos.CENTER);

        Label lbl2 = new Label();
        lbl2.setFont(new Font(18));
        lbl2.setTextFill(Color.WHITE);
        lbl2.setStyle("-fx-background-color: #0083ff; -fx-background-radius: 25;");
        lbl2.setText("Lead Software Engineer");
        lbl2.setAlignment(Pos.CENTER);

         AnchorPane.setTopAnchor(lbl1, 5.0);
         AnchorPane.setRightAnchor(lbl1, 0.0);
         AnchorPane.setLeftAnchor(lbl1, 0.0);

         AnchorPane.setBottomAnchor(anchorLbl1, 0.0);
         AnchorPane.setRightAnchor(anchorLbl1, 0.0);
         AnchorPane.setLeftAnchor(anchorLbl1, 0.0);

         AnchorPane.setBottomAnchor(lbl2, 0.0);
         AnchorPane.setRightAnchor(lbl2,0.0);
         AnchorPane.setLeftAnchor(lbl2, 0.0);

        anchorLbl1.getChildren().add(lbl2);
        anchorLbl1.getChildren().add(lbl1);

        anchorLbl1.setVisible(false);

        anchorPane1.setOnMouseEntered(event -> {
            anchorLbl1.setVisible(true);
        });

        anchorPane1.setOnMouseExited(event -> {
            anchorLbl1.setVisible(false);
        });

        anchorPane1.getChildren().add(anchorLbl1);

        HBox hbox1 = new HBox();
        hbox1.setPrefHeight(250);
        hbox1.setPrefWidth(1200);

        hbox1.getChildren().add(anchorPane1);
        vbox1.getChildren().add(hbox1);

       AnchorPane anchorPane2 = new AnchorPane();
       anchorPane2.setPrefWidth(250);
       anchorPane2.setPrefHeight(150);
       ImageView imgMatt = new ImageView();
       imgMatt.setImage(new Image("images/teamPics/matt.JPG"));
       imgMatt.setFitHeight(150);
       imgMatt.setFitWidth(250);
       imgMatt.setPreserveRatio(true);
       imgMatt.setPickOnBounds(true);
       imgMatt.setRotate(-90);
       anchorPane2.getChildren().add(imgMatt);

       AnchorPane anchorLbl2 = new AnchorPane();
       anchorLbl2.setPrefWidth(200);
        anchorLbl2.setPrefHeight(75);
        anchorLbl2.setStyle("-fx-background-color: #022D5A;  -fx-background-radius: 35;");

        Label lbl3 = new Label();
        lbl3.setText("Matt Iandoli");
        lbl3.setFont(new Font(18));
        lbl3.setTextFill(Color.WHITE);
        lbl3.setStyle("-fx-background-radius: ;");
        lbl3.setAlignment(Pos.CENTER);

        Label lbl4 = new Label();
        lbl4.setFont(new Font(18));
        lbl4.setTextFill(Color.WHITE);
        lbl4.setStyle("-fx-background-color: #0083ff; -fx-background-radius: 25;");
        lbl4.setText("Assistant Lead Software Engineer");

        lbl4.setTextAlignment(TextAlignment.CENTER);
        lbl4.setWrapText(true);

        AnchorPane.setTopAnchor(lbl3, 2.5);
        AnchorPane.setRightAnchor(lbl3, 0.0);
        AnchorPane.setLeftAnchor(lbl3, 0.0);

        AnchorPane.setBottomAnchor(anchorLbl2, 0.0);
        AnchorPane.setRightAnchor(anchorLbl2, 0.0);
        AnchorPane.setLeftAnchor(anchorLbl2, 0.0);

         AnchorPane.setBottomAnchor(lbl4, 0.0);
         AnchorPane.setRightAnchor(lbl4,0.0);
         AnchorPane.setLeftAnchor(lbl4, 0.0);

        anchorLbl2.getChildren().add(lbl3);
        anchorLbl2.getChildren().add(lbl4);
        anchorLbl2.setVisible(false);

        anchorPane2.setOnMouseEntered(event -> {
           anchorLbl2.setVisible(true);
      });

       anchorPane2.setOnMouseExited(event -> {
           anchorLbl2.setVisible(false);
       });

        anchorPane2.getChildren().add(anchorLbl2);
        hbox1.getChildren().add(anchorPane2);
        //hbox1.setSpacing(5);
         hbox1.setAlignment(Pos.CENTER);

        AnchorPane anchorPane3 = new AnchorPane();
        anchorPane3.setPrefWidth(250);
        anchorPane3.setPrefHeight(150);
        ImageView imgManas = new ImageView();
        imgManas.setImage(new Image("images/teamPics/Manas.jpg"));
        imgManas.setFitHeight(200);
        imgManas.setFitWidth(250);
        imgManas.setPreserveRatio(true);
        imgManas.setPickOnBounds(true);
        //imgManas.setRotate(-90);
        anchorPane3.getChildren().add(imgManas);


        AnchorPane anchorLbl3 = new AnchorPane();
        anchorLbl3.setPrefWidth(200);
        anchorLbl3.setPrefHeight(75);
        anchorLbl3.setStyle("-fx-background-color: #022D5A;  -fx-background-radius: 25;");

        Label lbl5 = new Label();
        lbl5.setText("Manas Mehta");
        lbl5.setFont(new Font(18));
        lbl5.setTextFill(Color.WHITE);
        lbl5.setStyle("-fx-background-radius: ;");
        lbl5.setAlignment(Pos.CENTER);

        Label lbl6 = new Label();
        lbl6.setFont(new Font(18));
        lbl6.setTextFill(Color.WHITE);
        lbl6.setStyle("-fx-background-color: #0083ff; -fx-background-radius: 25;");
        lbl6.setText("Assistant Lead Software Engineer");
        //lbl6.setAlignment(Pos.CENTER);
        lbl6.setTextAlignment(TextAlignment.CENTER);
        lbl6.setWrapText(true);

        AnchorPane.setTopAnchor(lbl5, 2.5);
        AnchorPane.setRightAnchor(lbl5, 0.0);
        AnchorPane.setLeftAnchor(lbl5, 0.0);

        AnchorPane.setBottomAnchor(anchorLbl3, 0.0);
        AnchorPane.setRightAnchor(anchorLbl3, 0.0);
        AnchorPane.setLeftAnchor(anchorLbl3, 0.0);

        AnchorPane.setBottomAnchor(lbl6, 0.0);
        AnchorPane.setRightAnchor(lbl6,0.0);
        AnchorPane.setLeftAnchor(lbl6, 0.0);

        anchorLbl3.getChildren().add(lbl5);
        anchorLbl3.getChildren().add(lbl6);

        anchorLbl3.setVisible(false);

        anchorPane3.setOnMouseEntered(event -> {
            anchorLbl3.setVisible(true);
        });

        anchorPane3.setOnMouseExited(event -> {
            anchorLbl3.setVisible(false);
        });

        anchorPane3.getChildren().add(anchorLbl3);

        hbox1.getChildren().add(anchorPane3);

        HBox hbox2 = new HBox();
        hbox2.setPrefHeight(250);
        hbox2.setPrefWidth(1200);
        vbox1.getChildren().add(hbox2);

        hbox2.setAlignment(Pos.CENTER);


        AnchorPane anchorPane4 = new AnchorPane();
        anchorPane4.setPrefWidth(250);
        anchorPane4.setPrefHeight(150);
        ImageView imgKatie = new ImageView();
        imgKatie.setImage(new Image("images/teamPics/katie.jpg"));
        imgKatie.setFitHeight(200);
        imgKatie.setFitWidth(250);
        imgKatie.setPreserveRatio(true);
        imgKatie.setPickOnBounds(true);
        //imgManas.setRotate(-90);
        anchorPane4.getChildren().add(imgKatie);

        AnchorPane anchorLbl4 = new AnchorPane();
        anchorLbl4.setPrefWidth(200);
        anchorLbl4.setPrefHeight(60);
        anchorLbl4.setStyle("-fx-background-color: #022D5A;  -fx-background-radius: 25;");

        Label lbl7 = new Label();
        lbl7.setText("Katie Deyette");
        lbl7.setFont(new Font(18));
        lbl7.setTextFill(Color.WHITE);
        lbl7.setStyle("-fx-background-radius: ;");
        lbl7.setAlignment(Pos.CENTER);

        Label lbl8 = new Label();
        lbl8.setFont(new Font(18));
        lbl8.setTextFill(Color.WHITE);
        lbl8.setStyle("-fx-background-color: #0083ff; -fx-background-radius: 25;");
        lbl8.setText("Full Time Software Engineer");
        lbl8.setAlignment(Pos.CENTER);

        AnchorPane.setTopAnchor(lbl7, 5.0);
        AnchorPane.setRightAnchor(lbl7, 0.0);
        AnchorPane.setLeftAnchor(lbl7, 0.0);

        AnchorPane.setBottomAnchor(anchorLbl4, 0.0);
        AnchorPane.setRightAnchor(anchorLbl4, 0.0);
        AnchorPane.setLeftAnchor(anchorLbl4, 0.0);

        AnchorPane.setBottomAnchor(lbl8, 0.0);
        AnchorPane.setRightAnchor(lbl8,0.0);
        AnchorPane.setLeftAnchor(lbl8, 0.0);

        anchorLbl4.getChildren().add(lbl7);
        anchorLbl4.getChildren().add(lbl8);

        anchorLbl4.setVisible(false);

        anchorPane4.setOnMouseEntered(event -> {
            anchorLbl4.setVisible(true);
        });

        anchorPane4.setOnMouseExited(event -> {
            anchorLbl4.setVisible(false);
        });

        anchorPane4.getChildren().add(anchorLbl4);
        hbox2.getChildren().add(anchorPane4);


        AnchorPane anchorPane5 = new AnchorPane();
        anchorPane5.setPrefWidth(250);
        anchorPane5.setPrefHeight(150);
        ImageView imgDanya = new ImageView();
        imgDanya.setImage(new Image("images/teamPics/danya.jpg"));
        imgDanya.setFitHeight(200);
        imgDanya.setFitWidth(250);
        imgDanya.setPreserveRatio(true);
        imgDanya.setPickOnBounds(true);
        //imgDanya.setRotate(-180);
        anchorPane5.getChildren().add(imgDanya);

        AnchorPane anchorLbl5 = new AnchorPane();
        anchorLbl5.setPrefWidth(200);
        anchorLbl5.setPrefHeight(60);
        anchorLbl5.setStyle("-fx-background-color: #022D5A;  -fx-background-radius: 25;");

        Label lbl9 = new Label();
        lbl9.setText("Danya Baron");
        lbl9.setFont(new Font(18));
        lbl9.setTextFill(Color.WHITE);
        lbl9.setStyle("-fx-background-radius: ;");
        lbl9.setAlignment(Pos.CENTER);


        Label lbl10 = new Label();
        lbl10.setFont(new Font(18));
        lbl10.setTextFill(Color.WHITE);
        lbl10.setStyle("-fx-background-color: #0083ff; -fx-background-radius: 25;");
        lbl10.setText("Product Owner");
        lbl10.setAlignment(Pos.CENTER);

        AnchorPane.setTopAnchor(lbl9, 5.0);
        AnchorPane.setRightAnchor(lbl9, 0.0);
        AnchorPane.setLeftAnchor(lbl9, 0.0);

        AnchorPane.setBottomAnchor(anchorLbl5, 0.0);
        AnchorPane.setRightAnchor(anchorLbl5, 0.0);
        AnchorPane.setLeftAnchor(anchorLbl5, 0.0);

        AnchorPane.setBottomAnchor(lbl10, 0.0);
        AnchorPane.setRightAnchor(lbl10,0.0);
        AnchorPane.setLeftAnchor(lbl10, 0.0);

        anchorLbl5.getChildren().add(lbl9);
        anchorLbl5.getChildren().add(lbl10);

        anchorLbl5.setVisible(false);

        anchorPane5.setOnMouseEntered(event -> {
            anchorLbl5.setVisible(true);
        });

        anchorPane5.setOnMouseExited(event -> {
            anchorLbl5.setVisible(false);
        });

        anchorPane5.getChildren().add(anchorLbl5);
        hbox2.getChildren().add(anchorPane5);


        AnchorPane anchorPane6 = new AnchorPane();
        anchorPane6.setPrefWidth(250);
        anchorPane6.setPrefHeight(150);
        ImageView imgGarrett = new ImageView();
        imgGarrett.setImage(new Image("images/teamPics/garrett.jpg"));
        imgGarrett.setFitHeight(200);
        imgGarrett.setFitWidth(250);
        imgGarrett.setPreserveRatio(true);
        imgGarrett.setPickOnBounds(true);
        //imgDanya.setRotate(-180);
        anchorPane6.getChildren().add(imgGarrett);

        AnchorPane anchorLbl6 = new AnchorPane();
        anchorLbl6.setPrefWidth(200);
        anchorLbl6.setPrefHeight(60);
        anchorLbl6.setStyle("-fx-background-color: #022D5A;  -fx-background-radius: 25;");

        Label lbl11 = new Label();
        lbl11.setText("Garrett Smith");
        lbl11.setFont(new Font(18));
        lbl11.setTextFill(Color.WHITE);
        lbl11.setStyle("-fx-background-radius: ;");
        lbl11.setAlignment(Pos.CENTER);

        Label lbl12 = new Label();
        lbl12.setFont(new Font(18));
        lbl12.setTextFill(Color.WHITE);
        lbl12.setStyle("-fx-background-color: #0083ff; -fx-background-radius: 25;");
        lbl12.setText("Full-Time Software Engineer");
        lbl12.setAlignment(Pos.CENTER);

        AnchorPane.setTopAnchor(lbl11, 5.0);
        AnchorPane.setRightAnchor(lbl11, 0.0);
        AnchorPane.setLeftAnchor(lbl11, 0.0);

        AnchorPane.setBottomAnchor(anchorLbl6, 0.0);
        AnchorPane.setRightAnchor(anchorLbl6, 0.0);
        AnchorPane.setLeftAnchor(anchorLbl6, 0.0);

        AnchorPane.setBottomAnchor(lbl12, 0.0);
        AnchorPane.setRightAnchor(lbl12,0.0);
        AnchorPane.setLeftAnchor(lbl12, 0.0);

        anchorLbl6.getChildren().add(lbl11);
        anchorLbl6.getChildren().add(lbl12);

        anchorLbl6.setVisible(false);

        anchorPane6.setOnMouseEntered(event -> {
            anchorLbl6.setVisible(true);
        });

        anchorPane6.setOnMouseExited(event -> {
            anchorLbl6.setVisible(false);
        });

        anchorPane6.getChildren().add(anchorLbl6);
        hbox2.getChildren().add(anchorPane6);

        HBox hbox3 = new HBox();
        hbox3.setPrefHeight(250);
        hbox3.setPrefWidth(1200);
        vbox1.getChildren().add(hbox3);
       //hbox3.setSpacing(3);

        hbox3.setAlignment(Pos.CENTER);

        AnchorPane anchorPane7 = new AnchorPane();
        anchorPane7.setPrefWidth(250);
        anchorPane7.setPrefHeight(150);
        ImageView imgMax = new ImageView();
        imgMax.setImage(new Image("images/teamPics/max.jpg"));
        imgMax.setFitHeight(200);
        imgMax.setFitWidth(250);
        imgMax.setPreserveRatio(true);
        imgMax.setPickOnBounds(true);
        //imgDanya.setRotate(-180);
        anchorPane7.getChildren().add(imgMax);

        AnchorPane anchorLbl7 = new AnchorPane();
        anchorLbl7.setPrefWidth(200);
        anchorLbl7.setPrefHeight(60);
        anchorLbl7.setStyle("-fx-background-color: #022D5A;  -fx-background-radius: 25;");

        Label lbl13 = new Label();
        lbl13.setText("Max Luu");
        lbl13.setFont(new Font(18));
        lbl13.setTextFill(Color.WHITE);
        lbl13.setStyle("-fx-background-radius: ;");
        lbl13.setAlignment(Pos.CENTER);

        Label lbl14 = new Label();
        lbl14.setFont(new Font(18));
        lbl14.setTextFill(Color.WHITE);
        lbl14.setStyle("-fx-background-color: #0083ff; -fx-background-radius: 25;");
        lbl14.setText("Scrum Master");
        lbl14.setAlignment(Pos.CENTER);

        AnchorPane.setTopAnchor(lbl13, 5.0);
        AnchorPane.setRightAnchor(lbl13, 0.0);
        AnchorPane.setLeftAnchor(lbl13, 0.0);

        AnchorPane.setBottomAnchor(anchorLbl7, 0.0);
        AnchorPane.setRightAnchor(anchorLbl7, 0.0);
        AnchorPane.setLeftAnchor(anchorLbl7, 0.0);

        AnchorPane.setBottomAnchor(lbl14, 0.0);
        AnchorPane.setRightAnchor(lbl14,0.0);
        AnchorPane.setLeftAnchor(lbl14, 0.0);

        anchorLbl7.getChildren().add(lbl13);
        anchorLbl7.getChildren().add(lbl14);

        anchorLbl7.setVisible(false);

        anchorPane7.setOnMouseEntered(event -> {
            anchorLbl7.setVisible(true);
        });

        anchorPane7.setOnMouseExited(event -> {
            anchorLbl7.setVisible(false);
        });

        anchorPane7.getChildren().add(anchorLbl7);
        hbox3.getChildren().add(anchorPane7);

        AnchorPane anchorPane8 = new AnchorPane();
        anchorPane8.setPrefWidth(250);
        anchorPane8.setPrefHeight(150);
        ImageView imgDan = new ImageView();
        imgDan.setImage(new Image("images/teamPics/dan.jpg"));
        imgDan.setFitHeight(200);
        imgDan.setFitWidth(250);
        imgDan.setPreserveRatio(true);
        imgDan.setPickOnBounds(true);
        //imgDanya.setRotate(-180);
        anchorPane8.getChildren().add(imgDan);

        AnchorPane anchorLbl8 = new AnchorPane();
        anchorLbl8.setPrefWidth(200);
        anchorLbl8.setPrefHeight(60);
        anchorLbl8.setStyle("-fx-background-color: #022D5A;  -fx-background-radius: 25;");

        Label lbl15 = new Label();
        lbl15.setText("Dan Oates");
        lbl15.setFont(new Font(18));
        lbl15.setTextFill(Color.WHITE);
        lbl15.setStyle("-fx-background-radius: ;");
        lbl15.setAlignment(Pos.CENTER);

        Label lbl16 = new Label();
        lbl16.setFont(new Font(18));
        lbl16.setTextFill(Color.WHITE);
        lbl16.setStyle("-fx-background-color: #0083ff; -fx-background-radius: 25;");
        lbl16.setText("Project Manager");
        lbl16.setAlignment(Pos.CENTER);

        AnchorPane.setTopAnchor(lbl15, 5.0);
        AnchorPane.setRightAnchor(lbl15, 0.0);
        AnchorPane.setLeftAnchor(lbl15, 0.0);

        AnchorPane.setBottomAnchor(anchorLbl8, 0.0);
        AnchorPane.setRightAnchor(anchorLbl8, 0.0);
        AnchorPane.setLeftAnchor(anchorLbl8, 0.0);

        AnchorPane.setBottomAnchor(lbl16, 0.0);
        AnchorPane.setRightAnchor(lbl16,0.0);
        AnchorPane.setLeftAnchor(lbl16, 0.0);

        anchorLbl8.getChildren().add(lbl15);
        anchorLbl8.getChildren().add(lbl16);

        anchorLbl8.setVisible(false);

        anchorPane8.setOnMouseEntered(event -> {
            anchorLbl8.setVisible(true);
        });

        anchorPane8.setOnMouseExited(event -> {
            anchorLbl8.setVisible(false);
        });

        anchorPane8.getChildren().add(anchorLbl8);
        hbox3.getChildren().add(anchorPane8);


        AnchorPane anchorPane9 = new AnchorPane();
        anchorPane9.setPrefWidth(250);
        anchorPane9.setPrefHeight(150);
        ImageView imgNiko = new ImageView();
        imgNiko.setImage(new Image("images/teamPics/niko.jpg"));
        imgNiko.setFitHeight(200);
        imgNiko.setFitWidth(250);
        imgNiko.setPreserveRatio(true);
        imgNiko.setPickOnBounds(true);
        //imgNiko.setRotate(-90);
        anchorPane9.getChildren().add(imgNiko);

        AnchorPane anchorLbl9 = new AnchorPane();
        anchorLbl9.setPrefWidth(200);
        anchorLbl9.setPrefHeight(60);
        anchorLbl9.setStyle("-fx-background-color: #022D5A;  -fx-background-radius: 25;");

        Label lbl17 = new Label();
        lbl17.setText("Niko Gamarra");
        lbl17.setFont(new Font(18));
        lbl17.setTextFill(Color.WHITE);
        lbl17.setStyle("-fx-background-radius: ;");
        lbl17.setAlignment(Pos.CENTER);

        Label lbl18 = new Label();
        lbl18.setFont(new Font(18));
        lbl18.setTextFill(Color.WHITE);
        lbl18.setStyle("-fx-background-color: #0083ff; -fx-background-radius: 25;");
        lbl18.setText("Test Engineer");
        lbl18.setAlignment(Pos.CENTER);

        AnchorPane.setTopAnchor(lbl17, 5.0);
        AnchorPane.setRightAnchor(lbl17, 0.0);
        AnchorPane.setLeftAnchor(lbl17, 0.0);

        AnchorPane.setBottomAnchor(anchorLbl9, 0.0);
        AnchorPane.setRightAnchor(anchorLbl9, 0.0);
        AnchorPane.setLeftAnchor(anchorLbl9, 0.0);

        AnchorPane.setBottomAnchor(lbl18, 0.0);
        AnchorPane.setRightAnchor(lbl18,0.0);
        AnchorPane.setLeftAnchor(lbl18, 0.0);

        anchorLbl9.getChildren().add(lbl17);
        anchorLbl9.getChildren().add(lbl18);

        anchorLbl9.setVisible(false);

        anchorPane9.setOnMouseEntered(event -> {
            anchorLbl9.setVisible(true);
        });

        anchorPane9.setOnMouseExited(event -> {
            anchorLbl9.setVisible(false);
        });

        anchorPane9.getChildren().add(anchorLbl9);
        hbox3.getChildren().add(anchorPane9);

        AnchorPane anchorPane10 = new AnchorPane();
        anchorPane10.setPrefWidth(250);
        anchorPane10.setPrefHeight(150);
        ImageView imgSophie = new ImageView();
        imgSophie.setImage(new Image("images/teamPics/sophie.jpg"));
        imgSophie.setFitHeight(200);
        imgSophie.setFitWidth(250);
        imgSophie.setPreserveRatio(true);
        imgSophie.setPickOnBounds(true);
        //imgNiko.setRotate(-90);
        anchorPane10.getChildren().add(imgSophie);

        AnchorPane anchorLbl10 = new AnchorPane();
        anchorLbl10.setPrefWidth(200);
        anchorLbl10.setPrefHeight(60);
        anchorLbl10.setStyle("-fx-background-color: #022D5A;  -fx-background-radius: 25;");

        Label lbl19 = new Label();
        lbl19.setText("Sophie Antoniou");
        lbl19.setFont(new Font(18));
        lbl19.setTextFill(Color.WHITE);
        lbl19.setStyle("-fx-background-radius: ;");
        lbl19.setAlignment(Pos.CENTER);
//
        Label lbl20 = new Label();
        lbl20.setFont(new Font(18));
        lbl20.setTextFill(Color.WHITE);
        lbl20.setStyle("-fx-background-color: #0083ff; -fx-background-radius: 25;");
        lbl20.setText("Documentation Analyst");
        lbl20.setAlignment(Pos.CENTER);

        AnchorPane.setTopAnchor(lbl19, 5.0);
        AnchorPane.setRightAnchor(lbl19, 0.0);
        AnchorPane.setLeftAnchor(lbl19, 0.0);
//
        AnchorPane.setBottomAnchor(anchorLbl10, 0.0);
        AnchorPane.setRightAnchor(anchorLbl10, 0.0);
        AnchorPane.setLeftAnchor(anchorLbl10, 0.0);

        AnchorPane.setBottomAnchor(lbl20, 0.0);
        AnchorPane.setRightAnchor(lbl20,0.0);
        AnchorPane.setLeftAnchor(lbl20, 0.0);

        anchorLbl10.getChildren().add(lbl19);
        anchorLbl10.getChildren().add(lbl20);
//
        anchorLbl10.setVisible(false);

        anchorPane10.setOnMouseEntered(event -> {
            anchorLbl10.setVisible(true);
        });

        anchorPane10.setOnMouseExited(event -> {
            anchorLbl10.setVisible(false);
        });
//
        anchorPane10.getChildren().add(anchorLbl10);
        hbox3.getChildren().add(anchorPane10);


        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(vbox1);
//        scrollPane.setContent(anchorPane2);
        scrollPane.setVisible(true);


    }



}
