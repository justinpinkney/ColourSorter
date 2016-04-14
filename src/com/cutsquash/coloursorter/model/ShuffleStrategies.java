package com.cutsquash.coloursorter.model;

import com.cutsquash.coloursorter.Utils;

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

                return (int) Math.signum(Utils.rgb2hsb(c1)[0] - Utils.rgb2hsb(c2)[0]);
            }
        };

    }

}
