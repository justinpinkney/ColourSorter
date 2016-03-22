import processing.core.PApplet;

import java.util.ArrayList;

import static javafx.scene.paint.Color.color;

public class PixelManager {

  PApplet parent;

  // Use a list because we will be doing lots of iteration
  ArrayList<Pixel> availables = new ArrayList<Pixel>();
  Pixel[][] pixelGrid;
  int w, h;
  
  public PixelManager(PApplet parent, int w, int h, DistanceMetric metric){
    this.parent = parent;
    // Initialise grid of empty pixels
    pixelGrid = new Pixel[w][h];
    this.w = w;
    this.h = h;
    for (int i=0; i<w; i++) {
      for (int j=0; j<h; j++) {
        pixelGrid[i][j] = new Pixel(this, metric, i, j);
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
                  || (di==0 && dj==0)
                  //|| (di==dj) 
                  ) {
              continue;
            }
            neighbours.add(pixelGrid[i+di][j+dj]);
          }
        } // Finished making neighbour list
        
        pixelGrid[i][j].setNeighbours(neighbours);
      }
    }
  }
  
  public void render() {
    for (int i=0; i<w; i++) {
      for (int j=0; j<h; j++) {
        int c = pixelGrid[i][j].c;
        parent.stroke(c);
        parent.point(i,j);
      }
    }
  }
  
  public void renderDebug() {
    for (int i=0; i<w; i++) {
      for (int j=0; j<h; j++) {
        switch (pixelGrid[i][j].state) {
          case EMPTY:
            parent.stroke(parent.color(0));
            break;
          case AVAILABLE:
            parent.stroke(parent.color(255,0,0));
            break;
          case FILLED:
            parent.stroke((int) pixelGrid[i][j].c);
            break;
        }
        parent.point(i,j);
      }
    }
  }
  
  public void placeColour(int c) {
        // check the min diff for each available space
        int besti = 0;
        int bestDiff = 500000;
        for (int i=0; i<availables.size(); i++) {
          Pixel p = availables.get(i);
          int mindiff = p.getDistance(c);
          // check the index of the lowest mindiff
          if (mindiff < bestDiff) {
            besti = i;
            bestDiff = mindiff;
          }
        }
        
        // Set the best index to the current colour
        Pixel p = availables.get(besti);
        p.fillPixel(c);
  }
  
  public void setAvailable(int i, int j) {
    pixelGrid[i][j].setState(State.AVAILABLE);
  }
  
  public void addAvailable(Pixel p){
    // Check if the pixel is already in the list
    if (availables.indexOf(p) == -1) {
      // if not then add it  
      availables.add(p);
    }
  }
  
  public void removeAvailable(Pixel p){
    availables.remove(p);
  }
}