package com.cutsquash.coloursorter;

/**
 * Created by JNP1 on 28/04/2016.
 */
public class MeanChecker implements Checker {

    int currentMean = 0;
    int totalValues = 0;

    @Override
    public void addValue(int value) {
        totalValues++;
        currentMean = ((totalValues - 1)*currentMean + value)/totalValues;
    }

    @Override
    public int getResult() {
        return currentMean/(1+totalValues)^2;
    }

    @Override
    public void reset() {
        currentMean = 0;
        totalValues = 0;
    }
}
