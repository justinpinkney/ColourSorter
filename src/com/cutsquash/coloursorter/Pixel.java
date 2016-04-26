package com.cutsquash.coloursorter;

import java.util.ArrayList;

class Pixel {

    int[] location = new int[2];
    int c;
    int r, g, b;
    ArrayList<Pixel> neighbours;
    DistanceMetric metric;
    PixelManager manager;
    State state;


    public Pixel(PixelManager manager,
                    DistanceMetric metric,
                    int i, int j) {
        // Create a new empty com.cutsquash.coloursorter.model.Pixel object
        this.state = State.EMPTY;
        this.location[0] = i;
        this.location[1] = j;
        this.manager = manager;
        this.metric = metric;
    }

    public int getDistance(int otherColor) {
        int minDist = Integer.MAX_VALUE;
        for (Pixel neighbour : neighbours ){
            if (neighbour.state == State.FILLED) {
                int thisDist = metric.compareColour(neighbour.c, otherColor);
                if (thisDist < minDist) { minDist = thisDist; }
            }
        }
        return minDist;
    }

    public void fillPixel(int c) {
        this.c = c;
        this.r = c>>16 & 0xFF;
        this.g = c>>8 & 0xFF;
        this.b = c & 0xFF;
        setState(State.FILLED);
        updateNeighbours();
    }

    public void setNeighbours(ArrayList neighbours) {
    this.neighbours = neighbours;
    }

    public void setState(State newState) {
        switch (newState) {
            case AVAILABLE:
                if (state == State.EMPTY) {
                    state = newState;
                    manager.addAvailable(this);
                }
                break;

            case FILLED:
                if (state == State.AVAILABLE) {
                    state = newState;
                    manager.removeAvailable(this);
                }
                break;
        }
    }

    private void updateNeighbours() {
        for (Pixel pix : neighbours) {
            pix.setState(State.AVAILABLE);
        }
    }

    public boolean equals(Object obj) {
        Pixel o = (Pixel) obj;
        return o == this;
    }
}