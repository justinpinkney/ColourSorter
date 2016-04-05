package com.cutsquash.coloursorter;

import com.cutsquash.coloursorter.model.ColourManager;
import com.cutsquash.coloursorter.model.DistanceMetricRGB;
import com.cutsquash.coloursorter.model.PixelManager;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by jnp1 on 05/04/2016.
 */
public class ColourSorter {

    public static void main(String[] args) {

        // Add an image
        int width = 1000;
        int height = 1000;

        PixelManager manager = new PixelManager(width, height, new DistanceMetricRGB());
        for (int i = 0; i < width; i++) {
            manager.setAvailable(0, 0);
        }
        ColourManager cManager = new ColourManager("C:\\Software\\Github\\ColourSorter\\data\\im (5).JPG", width, height);

        for (int c : cManager) manager.placeColour(c);

        BufferedImage img = manager.render();

        try {
            File outputfile = new File("C:\\Software\\Github\\ColourSorter\\data\\saved.png");
            ImageIO.write((RenderedImage) img, "png", outputfile);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}
