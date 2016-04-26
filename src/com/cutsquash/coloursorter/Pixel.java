package com.cutsquash.coloursorter;

import java.util.ArrayList;

/**
 * Class to represent a single pixel in the output image.
 */
public class Pixel {

    private int[] location; // To be used for spatial biasing
    private int colour;
    private ArrayList<Pixel> neighbours;
    private DistanceMetric metric;
    private PixelManager manager;
    private State state;

    /**
     * Create a Pixel object, initially set to empty
     *
     * @param manager   {@link PixelManager} responsible for this Pixel
     * @param metric    {@link DistanceMetric} for computing colour differences
     * @param i         Pixel x location in the output image
     * @param j         Pixel y location in the output image
     */
    public Pixel(PixelManager manager,
                    DistanceMetric metric,
                    int i, int j) {
        this.state = State.EMPTY;
        this.location = new int[]{i, j};
        this.manager = manager;
        this.metric = metric;
    }

    /**
     * Calculate the cost of placing a colour at this Pixel location.
     * Loop over the neighbours of this Pixel and determine the overall cost of
     * placing a colour.
     *
     * @param otherColor    Colour to be placed
     * @return              Overall cost
     */
    int getCost(int otherColor) {
        // TODO implement other strategies.
        int minDist = Integer.MAX_VALUE;
        int fullNeighbourCount = 0;
        for (Pixel neighbour : neighbours ){
            // Only check filled neighbours
            if (neighbour.state == State.FILLED) {
                fullNeighbourCount++;
                int thisDist = metric.compareColour(neighbour.colour, otherColor);
                if (thisDist < minDist) { minDist = thisDist; }
            }
        }

        // Case if there are no filled neighbours (i.e. this is a starting Pixel
        if (fullNeighbourCount == 0) {
            minDist = 0;
        }
        return minDist;
    }

    /**
     * Set the colour of this pixel, and make neighbours available.
     * @param c Colour to fill Pixel with
     */
    void fillPixel(int c) {
        this.colour = c;
        setState(State.FILLED);
        for (Pixel pix : neighbours) {
            pix.setState(State.AVAILABLE);
        }
    }

    public boolean equals(Object obj) {
        Pixel o = (Pixel) obj;
        return o == this;
    }

    // Getters and Setters /////////////////////////////////////////////////////
    void setNeighbours(ArrayList neighbours) {
        this.neighbours = neighbours;
    }

    /**
     * Update the state of this Pixel,
     * and add or remove from the manager's available list as appropriate.
     *
     * @param newState State to update to
     */
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

    public int getColour() {
        return colour;
    }
}