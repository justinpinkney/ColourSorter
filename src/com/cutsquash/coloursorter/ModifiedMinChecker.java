package com.cutsquash.coloursorter;

/**
 * Created by Justin on 30/04/2016.
 */
public class ModifiedMinChecker implements Checker{

    int currentMin = Integer.MAX_VALUE;
    int totalValues = 0;

    @Override
    public void addValue(int value) {
        totalValues++;
        if (value < currentMin) {
            currentMin = value;
        }
    }

    @Override
    public int getResult() {
        return currentMin-totalValues;
    }

    @Override
    public void reset() {
        currentMin = Integer.MAX_VALUE;
        totalValues = 0;
    }


}
