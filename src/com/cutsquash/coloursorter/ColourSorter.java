package com.cutsquash.coloursorter;

import com.cutsquash.coloursorter.model.ColourManager;
import com.cutsquash.coloursorter.model.DistanceMetricRGB;
import com.cutsquash.coloursorter.model.PixelManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import processing.core.PImage;

import java.util.ArrayList;

public class ColourSorter extends Application {

    PixelManager manager;
    ColourManager cManager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Add an image
        int width = 300;
        int height = 300;
        Image img;

        manager = new PixelManager(width, height, new DistanceMetricRGB());
        for (int i = 0; i < width; i++) {
            manager.setAvailable(0, 0);
        }
        cManager = new ColourManager("file:///C:\\Software\\Github\\ColourSorter\\data\\im (5).JPG", width, height);

        for (int c : cManager) manager.placeColour(c);
        img = manager.render();


        VBox root = new VBox();
        ImageView imView = new ImageView(img);
        root.getChildren().add(imView);

        Scene scene = new Scene(root, 300, 300);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}