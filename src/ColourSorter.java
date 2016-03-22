import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;

public class ColourSorter extends PApplet {

    PixelManager manager;
    ColourManager cManager;
    PImage img;
    ArrayList colours;
    int startT;

    public static void main(String[] args) {
        String[] a = {"MAIN"};
        PApplet.runSketch( a, new ColourSorter());
    }

    public void settings() {
        size(300, 300);
    }

    public void setup() {
        frameRate(1000);
        manager = new PixelManager(this, width, height, new DistanceMetricRGB());
        for (int i = 0; i < width; i++) {
            manager.setAvailable(0, 0);
        }
        cManager = new ColourManager(this, "im (5).JPG", width, height);
        startT = millis();
    }

    public void draw() {
        while (true) {
            if (cManager.hasNextColour()) {
                int c = cManager.getNextColour();
                manager.placeColour(c);
            } else {
                println(millis() - startT);
                manager.renderDebug();
                noLoop();
                break;
            }

        }

    }
}