import java.util.ArrayList;

class Pixel {

  int[] location = new int[2];
  int c;
  int r, g, b;
  ArrayList<Pixel> neighbours;
  DistanceMetric metric;
  PixelManager manager;
  State state;


  public Pixel(PixelManager manager,
                DistanceMetric metric, 
                int i, int j) {
    // Create a new empty Pixel object
    this.state = State.EMPTY;
    this.location[0] = i;
    this.location[1] = j;
    this.manager = manager;
    this.metric = metric;
  }

  public int getDistance(int otherColor) {
    int minDist = 1000000;
    for (Pixel neighbour : neighbours ){
      if (neighbour.state == State.FILLED) {
        int thisDist = compareColour(neighbour.c, otherColor);
        if (thisDist < minDist) { minDist = thisDist; }
      }
    }
    return minDist;
  }
    
  public int compareColour(int c1, int c2) {
    int r1 = (c1>>16 & 0xFF);
    int g1 = (c1>>8 & 0xFF);
    int b1 = (c1 & 0xFF);
    
    int r2 = (c2>>16 & 0xFF);
    int g2 = (c2>>8 & 0xFF);
    int b2 = (c2 & 0xFF);

    int deltaR = r2 - r1;
    int deltaG = g2 - g1;
    int deltaB = b2 - b1;
    
    return deltaR*deltaR
                      + deltaG*deltaG
                      + deltaB*deltaB;

  }

  public void fillPixel(int c) {
    this.c = c;
    this.r = c>>16 & 0xFF;
    this.g = c>>8 & 0xFF;
    this.b = c & 0xFF;
    setState(State.FILLED);
    updateNeighbours();
  }

  public void setNeighbours(ArrayList neighbours) {
    this.neighbours = neighbours;
  }

  public void setState(State newState) {
    switch (newState) {
      case AVAILABLE:
        if (state == State.EMPTY) {
          state = newState;
          manager.addAvailable(this);
        }
        break;
  
      case FILLED:
        if (state == State.AVAILABLE) {
          state = newState;
          manager.removeAvailable(this);
        }
        break;
      }
  }

  private void updateNeighbours() {
    for (Pixel pix : neighbours) {
      pix.setState(State.AVAILABLE);
    }
  }
  
  public boolean equals(Object obj) {
    //if (obj == null) return false;
    //if (obj == this) return true;
    //if (!(obj instanceof Pixel)) return false;
    Pixel o = (Pixel) obj;
    return o == this;
  }
}