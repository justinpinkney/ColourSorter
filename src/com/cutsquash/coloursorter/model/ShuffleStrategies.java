package com.cutsquash.coloursorter.model;

import com.cutsquash.coloursorter.Utils;

import java.awt.*;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

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

    public static class RSorter implements ColourShuffleStrategy {
        @Override
        public void shuffle(List list) {
            Collections.sort(list, new RgbComparater(0));
        }
    }

    public static class GSorter implements ColourShuffleStrategy {
        @Override
        public void shuffle(List list) {
            Collections.sort(list, new RgbComparater(1));
        }
    }

    public static class BSorter implements ColourShuffleStrategy {
        @Override
        public void shuffle(List list) {
            Collections.sort(list, new RgbComparater(2));
        }
    }

    public static class HueSorter implements ColourShuffleStrategy {
        @Override
        public void shuffle(List list) {
            Collections.sort(list, new HsbComparater(0));
        }
    }

    public static class SaturationSorter implements ColourShuffleStrategy {
        @Override
        public void shuffle(List list) {
            Collections.sort(list, new HsbComparater(1));
        }
    }

    public static class BrightnessSorter implements ColourShuffleStrategy {
        @Override
        public void shuffle(List list) {
            Collections.sort(list, new HsbComparater(2));
        }
    }

    public static class Randomiser implements ColourShuffleStrategy {

        ColourShuffleStrategy initialStrategy;
        double factor; // randomisation factor

        public Randomiser(ColourShuffleStrategy initialStrategy, double factor) {
            this.initialStrategy = initialStrategy;
            this.factor = factor;
        }

        @Override
        public void shuffle(List list) {
            initialStrategy.shuffle(list);

            // add some randomisation
            int randomiseLength = (int) (list.size()*factor);
            for (int i = 0; i < randomiseLength; i++) {
                int firstIndex = new Random().nextInt(list.size());
                int nextIndex = new Random().nextInt(list.size());
                Collections.swap(list, firstIndex, nextIndex);
            }
        }
    }

    public static class Reverser implements ColourShuffleStrategy {

        ColourShuffleStrategy initialStrategy;

        public Reverser(ColourShuffleStrategy initialStrategy) {
            this.initialStrategy = initialStrategy;
        }

        @Override
        public void shuffle(List list) {
            // Apply the original sort method
            initialStrategy.shuffle(list);
            Collections.reverse(list);
        }
    }

    // Comparater //////////////////////////////////////////////////////////////////////////////////////////////////////
    private static class HsbComparater implements Comparator<Integer> {

        int channel;

        HsbComparater(int channel) {
            this.channel = channel;
        }

        @Override
        public int compare(Integer c1, Integer c2) {
            return (int) Math.signum(Utils.rgb2hsb(c1)[channel] - Utils.rgb2hsb(c2)[channel]);
        }
    }

    private static class RgbComparater implements Comparator<Integer> {

        int channel;

        RgbComparater(int channel) {
            this.channel = channel;
        }

        @Override
        public int compare(Integer c1, Integer c2) {
            return (int) Math.signum(Utils.getRgb(c1)[channel] - Utils.getRgb(c2)[channel]);
        }
    }

}
