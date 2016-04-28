package com.cutsquash.coloursorter;

/**
 * Created by JNP1 on 28/04/2016.
 */
public class MaxChecker implements Checker {

    int currentMax = 0;

    @Override
    public void addValue(int value) {
        if (value > currentMax) {
            currentMax = value;
        }
    }

    @Override
    public int getResult() {
            return currentMax;
        }

    @Override
    public void reset() {
        currentMax = 0;
    }
}
