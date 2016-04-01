package com.cutsquash.coloursorter.model;

public class DistanceMetricRGB implements DistanceMetric {

  public DistanceMetricRGB() {}
  
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
  
}