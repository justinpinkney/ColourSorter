package com.cutsquash.coloursorter;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class PixelManager {

    // Use a list because we will be doing lots of iteration
    ArrayList<Pixel> availables = new ArrayList<Pixel>();
    Pixel[][] pixelGrid;
    public int w, h;
    BufferedImage img;

    public PixelManager(int w, int h, DistanceMetric metric, Checker checker){
        // Initialise grid of empty pixels
        pixelGrid = new Pixel[w][h];
        img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        this.w = w;
        this.h = h;
        for (int i=0; i<w; i++) {
            for (int j=0; j<h; j++) {
                pixelGrid[i][j] = new Pixel(this, metric, checker, i, j);
            }
        }

        // Set the neighbours of all the pixels
        for (int i=0; i<w; i++) {
            for (int j=0; j<h; j++) {

                ArrayList<Pixel> neighbours = new ArrayList<Pixel>();

                // Assemble the list of neighbours
                for (int di = -1; di <= 1; di++) {
                    if (i + di == -1 || i + di == w) {
                        continue;
                    }
                    for (int dj = -1; dj <= 1; dj++) {
                        if (j + dj == -1 || j + dj == h
                        || (di==0 && dj==0) // Moore neighbourhood (i.e. 8)
                        ) {
                            continue;
                        }
                        neighbours.add(pixelGrid[i+di][j+dj]);
                    }
                }

                pixelGrid[i][j].setNeighbours(neighbours);
            }
        }
    }
  
    public BufferedImage render() {
        for (int i=0; i<w; i++) {
            for (int j=0; j<h; j++) {
                int c = pixelGrid[i][j].getColour();
                img.setRGB(i, j, c);
            }
        }
        return img;
    }

    public void placeColour(int c) {
        // TODO make performance test for this method, as it's always going to be the most called thing
        // check the min diff for each available space
        int factor = 10; // skip factor for approximation
        int besti = 0;
        int bestDiff = Integer.MAX_VALUE;
        // Should we actually check each filled pixel with available neighbours instead?
        for (int i=0; i<availables.size(); i+=factor) {
            Pixel p = availables.get(i);
            int mindiff = p.getCost(c);
            // check the index of the lowest mindiff
            // What about ties?
            if (mindiff < bestDiff) {
                besti = i;
                bestDiff = mindiff;
            }
        }

        // Set the best index to the current colour
        Pixel p = availables.get(besti);
        p.fillPixel(c);
    }
  
    public void addAvailable(Pixel p){
    // Check if the pixel is already in the list
        if (availables.indexOf(p) == -1) {
            // if not then add it
            availables.add(p);
        }
    }

    public void setAvailable(int i, int j) {
        pixelGrid[i][j].setState(State.AVAILABLE);
}

    public void setAvailableLine(int starti, int startj, int endi, int endj) {
        double lineLength = Math.sqrt(Math.pow(endi - starti, 2) + Math.pow(endj - startj, 2));
        for (int i = 0; i < lineLength; i++) {
            double t = i/lineLength;
            setAvailable((int) Math.floor(starti + (endi-starti)*t),
                            (int) Math.floor(startj + (endj-startj)*t));
        }
    }

    public void setAvailableRandom(int number) {
        for (int i = 0; i < number; i++) {
            setAvailable(new Random().nextInt(w),
                            new Random().nextInt(h));
        }
    }

    public void removeAvailable(Pixel p){
    availables.remove(p);
  }
}