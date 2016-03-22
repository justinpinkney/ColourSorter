import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.Collections;

import static processing.core.PApplet.println;

public class ColourManager {
  // TODO implement iterable

  PApplet parent;
  PImage img;
  ArrayList colours;
  
  public ColourManager(PApplet parent, String filename, int w, int h) {
    this.parent = parent;
    img = parent.loadImage(filename);
    img.resize(w, h);
    
    colours = new ArrayList();
    
    for (int i=0; i<w; i++) {
      for (int j=0; j<h; j++) {
        int c = img.get(i, j);
        colours.add(c);
      }
    }
    println(colours.size());
    Collections.shuffle(colours);
  }
  
  public boolean hasNextColour() {
    if (colours.size() == 0) {
      return false;
    } else {
      return true;
    }
  }
  
  public int getNextColour() {
      int c = (Integer) colours.get(0);
      colours.remove(0);
      return c;
  }

}