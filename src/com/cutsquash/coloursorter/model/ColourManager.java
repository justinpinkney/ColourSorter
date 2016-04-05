package com.cutsquash.coloursorter.model;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import static processing.core.PApplet.println;

public class ColourManager implements Iterable<Integer>{

  Image img;
  PixelReader reader;
  ArrayList colours;
  
  public ColourManager(String filename, int w, int h) {
    img = new Image(filename, w, h, false, false);
    reader = img.getPixelReader();

    colours = new ArrayList();
    
    for (int i=0; i<w; i++) {
      for (int j=0; j<h; j++) {
        int c = reader.getArgb(i, j);
        colours.add(c);
      }
    }
    Collections.shuffle(colours);
  }

  @Override
  public Iterator<Integer> iterator() {
    return colours.iterator();
  }
}