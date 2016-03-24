import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.Collections;

import static processing.core.PApplet.println;

public class ColourManager {
  // TODO implement iterable

  PApplet parent;
  Image img;
  PixelReader reader;
  ArrayList colours;
  
  public ColourManager(String filename, int w, int h) {
    this.parent = parent;
    img = new Image(filename, w, h, false, false);
    reader = img.getPixelReader();

    colours = new ArrayList();
    
    for (int i=0; i<w; i++) {
      for (int j=0; j<h; j++) {
        int c = reader.getArgb(i, j);
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