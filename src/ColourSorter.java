import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public class ColourSorter extends Application {

    PixelManager manager;
    ColourManager cManager;
    PImage img;
    ArrayList colours;
    int startT;

    public static void main(String[] args) {
        launch(args);
    }

//    public void settings() {
//        size(300, 300);
//    }
//
//    public void setup() {
//        frameRate(1000);
//        manager = new PixelManager(this, width, height, new DistanceMetricRGB());
//        for (int i = 0; i < width; i++) {
//            manager.setAvailable(0, 0);
//        }
//        cManager = new ColourManager(this, "im (5).JPG", width, height);
//        startT = millis();
//    }
//
//    public void draw() {
//        while (true) {
//            if (cManager.hasNextColour()) {
//                int c = cManager.getNextColour();
//                manager.placeColour(c);
//            } else {
//                println(millis() - startT);
//                manager.renderDebug();
//                noLoop();
//                break;
//            }
//
//        }
//
//    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Add the button
        Button btn = new Button();
        btn.setText("Hello");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello");
            }
        });

        // Add an image
        int width = 300;
        int height = 300;
        Image img;

        manager = new PixelManager(width, height, new DistanceMetricRGB());
        for (int i = 0; i < width; i++) {
            manager.setAvailable(0, 0);
        }
        cManager = new ColourManager("file:///C:\\Software\\Github\\ColourSorter\\data\\im (5).JPG", width, height);

        while (true) {
            if (cManager.hasNextColour()) {
                int c = cManager.getNextColour();
                manager.placeColour(c);
            } else {
                img = manager.render();
                break;
            }

        }


        VBox root = new VBox();
        root.getChildren().add(btn);
        ImageView imView = new ImageView(img);
        root.getChildren().add(imView);

        Scene scene = new Scene(root, 300, 300);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}