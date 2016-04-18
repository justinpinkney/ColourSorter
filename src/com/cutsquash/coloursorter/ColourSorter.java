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
    String outputFilename;

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
        ColourShuffleStrategy shuffler = parseShuffler((String) opts.get("--sort"), randFactor);
        DistanceMetric metric = parseMetric((String) opts.get("--distance"));
        String preset = (String) opts.get("--preset");

        // Set up and run
        ColourSorter sorter = new ColourSorter(x, y, inputFile,
                                                metric, shuffler, outputFile, preset);
        sorter.run();
    }

    public ColourSorter(int width,
                        int height,
                        String filename,
                        DistanceMetric distanceMetric,
                        ColourShuffleStrategy shuffler,
                        String outputFilename,
                        String preset) {

        manager = new PixelManager(width, height, distanceMetric);
        cManager = new ColourManager(filename, width, height, shuffler);
        if (preset.length()>0) {
            this.applyPreset(preset);
        }
        this.outputFilename = outputFilename;
    }

    public ColourSorter(int width,
                        int height,
                        String filename) {

        this(width, height, filename,
                new DistanceMetricHSB(),
                new ShuffleStrategies.Randomiser(
                        new ShuffleStrategies.BrightnessSorter()
                        ,0.2
                ),
                "output.png", ""
            );
    }

    public void run() {

        for (int c : cManager) manager.placeColour(c);

        BufferedImage img = manager.render();

        try {
            File outputfile = new File(outputFilename);
            ImageIO.write(img, "png", outputfile);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    private void applyPreset(String preset) {
        switch (preset) {
            case "Centre":
                manager.setAvailable(manager.w/2, manager.h/2);
                break;
            case "Corner":
                manager.setAvailable(0, 0);
                break;
            case "Border":
                manager.setAvailableLine(0, 0, 0, manager.h - 1);
                manager.setAvailableLine(0, manager.h - 1, manager.w - 1, manager.h - 1);
                manager.setAvailableLine(manager.w - 1, manager.h - 1, manager.w - 1, 0);
                manager.setAvailableLine(manager.w - 1, 0, 0, 0);
                break;
            case "Diagonal":
                manager.setAvailableLine(0, 0, manager.w, manager.h);
                break;
            case "Random":
                manager.setAvailableRandom(10);
                break;
            default:
                System.out.println("Unrecognised preset");
        }
    }


    private static ColourShuffleStrategy parseShuffler(String optionString, double randFactor) {
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
        if (randFactor > 0) {
            // wrap in a randomiser
            shuffler = new ShuffleStrategies.Randomiser(shuffler, randFactor);
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

}
