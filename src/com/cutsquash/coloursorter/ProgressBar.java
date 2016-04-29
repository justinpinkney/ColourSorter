package com.cutsquash.coloursorter;

/**
 * Created by jnp1 on 29/04/2016.
 */
public class ProgressBar {

    final static int progressWidth = 60;

    public static void update(int done, int total) {

        int pc = (100*++done)/total;
        String label = String.format(" %d%% complete", pc);

        // Make the progress bar
        String progressBar = "";
        for (int i = 0; i < progressWidth; i++) {
            if (i<(progressWidth*pc)/100) {
                progressBar +=  "=";
            } else {
                progressBar += " ";
            }
        }

        // Assemble the full thing
        String finalBar = "\r|" + progressBar + "|" + label;
        System.out.print(finalBar);
    }
}
