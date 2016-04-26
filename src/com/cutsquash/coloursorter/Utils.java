package com.cutsquash.coloursorter;

import java.awt.*;

/**
 * Created by jnp1 on 14/04/2016.
 */
public class Utils {

    public static float[] rgb2hsb(int c) {
        int r1 = (c>>16 & 0xFF);
        int g1 = (c>>8 & 0xFF);
        int b1 = (c & 0xFF);

        return Color.RGBtoHSB(r1, g1, b1, null);
    }

    public static int[] getRgb(int c) {
        int r1 = (c>>16 & 0xFF);
        int g1 = (c>>8 & 0xFF);
        int b1 = (c & 0xFF);

        return new int[]{r1, g1, b1};
    }

}
