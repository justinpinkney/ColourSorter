package com.cutsquash.coloursorter.model;

import java.awt.*;

/**
 * Created by jnp1 on 08/04/2016.
 */
public class DistanceMetricHSB implements DistanceMetric {
    @Override
    public int compareColour(int c1, int c2) {
        int r1 = (c1>>16 & 0xFF);
        int g1 = (c1>>8 & 0xFF);
        int b1 = (c1 & 0xFF);

        int r2 = (c2>>16 & 0xFF);
        int g2 = (c2>>8 & 0xFF);
        int b2 = (c2 & 0xFF);

        float[] hsb1 = Color.RGBtoHSB(r1, g1, b1, null);
        float[] hsb2 = Color.RGBtoHSB(r2, g2, b2, null);

        float deltaH = hsb2[0] - hsb1[0];
        float deltaS = hsb2[1] - hsb1[1];
        float deltaB = hsb2[2] - hsb1[2];

        return (int) (255*255*(deltaH*deltaH
                            + deltaS*deltaS
                            + deltaB*deltaB));
    }
}
