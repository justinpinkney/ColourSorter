package com.cutsquash.coloursorter;

/**
 * Created by jnp1 on 08/04/2016.
 */
public class DistanceMetricHSB implements DistanceMetric {
    @Override
    public int compareColour(int c1, int c2) {
        float[] hsb1 = Utils.rgb2hsb(c1);
        float[] hsb2 = Utils.rgb2hsb(c2);

        float deltaH = hsb2[0] - hsb1[0];
        float deltaS = hsb2[1] - hsb1[1];
        float deltaB = hsb2[2] - hsb1[2];

        return (int) (255*255*(deltaH*deltaH
                            + deltaS*deltaS
                            + deltaB*deltaB));
    }
}
