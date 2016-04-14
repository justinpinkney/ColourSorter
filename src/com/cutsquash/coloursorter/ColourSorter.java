package com.cutsquash.coloursorter;

import com.cutsquash.coloursorter.model.*;
import org.docopt.Docopt;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import static java.lang.Math.round;

/**
 * Created by jnp1 on 05/04/2016.
 */
public class ColourSorter {

    PixelManager manager;
    ColourManager cManager;

    private static final String doc =
        "Colour Sorter.\n"
                + "\n"
                + "Usage:\n"
                + "  ColourSorter <x> <y> <file>"
                    // Options
                    + " [ --output=<outputFile>"
                    +   " --sort=<sortMethod> ]\n"
                + "  ColourSorter (-h | --help)\n"
                + "  ColourSorter --version\n"
                + "\n"
                + "Options:\n"
                + "  -h --help     Show this screen.\n"
                + "  --version     Show version.\n"
                + "  --output=<outputFile>  Output file location.\n"
                + "  --sort=<sortMethod>  Original pixel colour sort method.\n"
                + "\n";

    public static void main(String[] args) {
        Map<String, Object> opts =
            new Docopt(doc).withVersion("Colour Sorter 1.0").parse(args);
        System.out.println(opts);
        int x = Integer.parseInt((String) opts.get("<x>"));
        int y = Integer.parseInt((String) opts.get("<y>"));
        String inputFile = (String) opts.get("<file>");
        ColourSorter sorter = new ColourSorter(x, y, inputFile);
        sorter.run();
    }

    public ColourSorter(int width,
                        int height,
                        String filename,
                        DistanceMetric distanceMetric,
                        ColourShuffleStrategy shuffler) {

        manager = new PixelManager(width, height, distanceMetric);
        cManager = new ColourManager(filename, width, height, shuffler);
    }


    public ColourSorter(int width,
                        int height,
                        String filename) {

        this(width, height, filename,
                new DistanceMetricHSB(),
                new ShuffleStrategies.Randomiser(
                        new ShuffleStrategies.BrightnessSorter(),
                        0.1
                )
            );
    }

    public void run() {

//        for (int i = 0; i < manager.w; i++) {
//            manager.setAvailable(manager.w - i - 1, 0);
//        }

//        manager.setAvailable(round(manager.w/2), round(manager.w/2));
        manager.setAvailable(0, 0);

        for (int c : cManager) manager.placeColour(c);

        BufferedImage img = manager.render();

        try {
            File outputfile = new File("saved.png");
            ImageIO.write(img, "png", outputfile);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}
