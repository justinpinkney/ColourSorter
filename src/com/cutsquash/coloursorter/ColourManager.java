package com.cutsquash.coloursorter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


public class ColourManager implements Iterable<Integer>{

    BufferedImage img;
    ArrayList<Integer> colours;
    ColourShuffleStrategy shuffler;

    public ColourManager(String filename, int w, int h, ColourShuffleStrategy shuffler) {
        this.shuffler = shuffler;
        BufferedImage originalImage = null;
        try {
            originalImage = ImageIO.read(new File(filename));
            img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

            Graphics g = img.createGraphics();
            g.drawImage(originalImage, 0, 0, w, h, null);
            g.dispose();
        } catch (IOException e) {
            System.out.println(e.getMessage() + filename);
        }

        colours = new ArrayList<Integer>();

        for (int i=0; i<w; i++) {
            for (int j=0; j<h; j++) {
                int c = img.getRGB(i, j);
                colours.add(c);
            }
        }
        shuffler.shuffle(colours);
    }

    @Override
    public Iterator<Integer> iterator() {
return colours.iterator();
}
}