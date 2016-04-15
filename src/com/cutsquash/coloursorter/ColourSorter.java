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
                    + " ["
                    +   " --output <outputFile>"
                    +   " --sort <sortMethod>"
                    +   " --random <randFactor>"
                    +   " --distance <metric>"
                    +   " --preset <presetName>"
                    + "]\n"
                + "  ColourSorter (-h | --help)\n"
                + "  ColourSorter --version\n"
                + "\n"
                + "Options:\n"
                + "  -h --help              Show this screen.\n"
                + "  --version              Show version.\n"
                + "  --output <outputFile>  Output file location.                       [default: output.png]\n"
                + "  --sort <sortMethod>    (R|G|B|Hue|Saturation|Brightness|Shuffle)   [default: Shuffle]\n"
                + "  --random <randFactor>  Fraction of ordered pixels to randomise.    [default: 0]\n"
                + "  --distance <metric>    (RGB|HSB)                                   [default: RGB]\n"
                + "  --preset <presetName>  (Centre|Corner|Border|Diagonal|Random)      [default: Centre]\n"
                + "\n";

    public static void main(String[] args) {
        Map<String, Object> opts =
            new Docopt(doc).withVersion("Colour Sorter 1.0").parse(args);

        // Parse arguments
        int x = Integer.parseInt((String) opts.get("<x>"));
        int y = Integer.parseInt((String) opts.get("<y>"));
        String inputFile = (String) opts.get("<file>");

        // Options
        String outputFile = (String) opts.get("--output");
        double randFactor = Double.parseDouble((String) opts.get("--random"));
        ColourShuffleStrategy shuffler = parseShuffler((String) opts.get("--sort"));
        DistanceMetric metric = parseMetric((String) opts.get("--distance"));

        // Set up and run
        ColourSorter sorter = new ColourSorter(x, y, inputFile, metric, shuffler);
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
                        new ShuffleStrategies.BrightnessSorter()
                        ,0.2
                )
            );
    }

    public void run() {

//        for (int i = 0; i < manager.w; i++) {
//            manager.setAvailable(manager.w - i - 1, 0);
//        }

//        manager.setAvailable(round(manager.w/2), round(manager.w/2));
//        manager.setAvailableLine(0, 0, manager.w, manager.h);

//        manager.setAvailableRandom(100);

        availableBorder(manager);

        for (int c : cManager) manager.placeColour(c);

        BufferedImage img = manager.render();

        try {
            File outputfile = new File("saved.png");
            ImageIO.write(img, "png", outputfile);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }


    private static ColourShuffleStrategy parseShuffler(String optionString) {
        ColourShuffleStrategy shuffler = new ShuffleStrategies.Shuffler();
        switch (optionString) {
            case "R":
                shuffler = new ShuffleStrategies.RSorter();
                break;
            case "G":
                shuffler = new ShuffleStrategies.GSorter();
                break;
            case "B":
                shuffler = new ShuffleStrategies.BSorter();
                break;
            case "Hue":
                shuffler = new ShuffleStrategies.HueSorter();
                break;
            case "Saturation":
                shuffler = new ShuffleStrategies.BrightnessSorter();
                break;
            case "Brightness":
                shuffler = new ShuffleStrategies.SaturationSorter();
                break;
            case "Shuffle":
                shuffler = new ShuffleStrategies.Shuffler();
                break;
            default:
                System.out.println("Unrecognised strategy");
                break;
        }
        return shuffler;
    }

    private static DistanceMetric parseMetric(String optionString) {
        DistanceMetric metric = new DistanceMetricRGB();
        switch (optionString) {
            case "RGB":
                metric = new DistanceMetricRGB();
                break;
            case "HSB":
                metric = new DistanceMetricHSB();
                break;
            default:
                System.out.println("Unrecognised metric");
                break;
        }
        return metric;
    }

    // Availability presets ////////////////////////////////////////////////////////////////////////////////////////////
    public static void availableBorder(PixelManager m) {
        m.setAvailableLine(0, 0, 0, m.h - 1);
        m.setAvailableLine(0, m.h - 1, m.w - 1, m.h - 1);
        m.setAvailableLine(m.w - 1, m.h - 1, m.w - 1, 0);
        m.setAvailableLine(m.w - 1, 0, 0, 0);
    }

    public static void availableDiagonal(PixelManager m) {
        m.setAvailableLine(0, 0, m.w, m.h);
    }


}
