package com.cutsquash.coloursorter.model;

import java.util.Collections;
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

}
