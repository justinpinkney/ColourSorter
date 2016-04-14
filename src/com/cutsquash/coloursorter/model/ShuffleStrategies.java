package com.cutsquash.coloursorter.model;

import java.awt.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jnp1 on 06/04/2016.
 */
public class ShuffleStrategies {

    public static class Shuffler implements ColourShuffleStrategy {
        @Override
        public void shuffle(List list) {
            Collections.shuffle(list);
        }
    }

    public static class Sorter implements ColourShuffleStrategy {
        @Override
        public void shuffle(List list) {
            Collections.sort(list);
        }
    }

    public static class HsbSorter implements ColourShuffleStrategy {
        @Override
        public void shuffle(List list) {
            Collections.sort(list, Comparators.HsbComparater);
        }
    }

    public static class Comparators {

        public static Comparator<Integer> HsbComparater = new Comparator<Integer>() {
            @Override
            public int compare(Integer c1, Integer c2) {
                int r1 = (c1>>16 & 0xFF);
                int g1 = (c1>>8 & 0xFF);
                int b1 = (c1 & 0xFF);

                int r2 = (c2>>16 & 0xFF);
                int g2 = (c2>>8 & 0xFF);
                int b2 = (c2 & 0xFF);

                float[] hsb1 = Color.RGBtoHSB(r1, g1, b1, null);
                float[] hsb2 = Color.RGBtoHSB(r2, g2, b2, null);

                int deltaHue = (int) Math.signum(hsb1[0] - hsb2[0]);

                return deltaHue;
            }
        };

    }

}
