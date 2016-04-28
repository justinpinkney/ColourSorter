package com.cutsquash.coloursorter;

/**
 * Created by JNP1 on 28/04/2016.
 */
public class MinChecker implements Checker {

    int currentMin = Integer.MAX_VALUE;

    @Override
    public void addValue(int value) {
        if (value < currentMin) {
            currentMin = value;
        }
    }

    @Override
    public int getResult() {
        return currentMin;
    }

    @Override
    public void reset() {
        currentMin = Integer.MAX_VALUE;
    }

}
