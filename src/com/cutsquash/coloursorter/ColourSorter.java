package com.cutsquash.coloursorter;

import org.docopt.Docopt;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static java.lang.Math.round;

/**
 * Main class to set up and run the colour sorting algorithm.
 *
 * @author  Justin Pinkney
 * @version 0.1
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
                    +   "[ --sort=<sortMethod> | --interleave <sort1> <sort2> ] "
                    + " ["
                    +   " --output=<outputFile>"
                    +   " --reverse=<reverseFlag>"
                    +   " --random=<randFactor>"
                    +   " --distance=<metric>"
                    +   " --checker=<method>"
                    +   " --preset=<presetName>"
                    + "]\n"
                + "  ColourSorter (-h | --help)\n"
                + "  ColourSorter --version\n"
                + "\n"
                + "Options:\n"
                + "  -h --help                  Show this screen.\n"
                + "  --version                  Show version.\n"
                + "  --output <outputFile>      Output file location.                       [default: output.png]\n"
                + "  --sort <sortMethod>        (R|G|B|Hue|Saturation|Brightness|Shuffle)   [default: Shuffle]\n"
                + "  --reverse <reverseFlag>    Reverse the sort method?                    [default: False]\n"
                + "  --random <randFactor>      Fraction of ordered pixels to randomise.    [default: 0]\n"
                + "  --distance <metric>        (RGB|HSB)                                   [default: RGB]\n"
                + "  --checker <method>         (min|max|mean|mod)                          [default: min]\n"
                + "  --preset <presetName>      (Centre|Corner|Edge|Border|Diagonal|Random|RandomLine)"
                +                               " Or pass a filename to use the non-black area of the image as available pixels."
                +                                                                                   "[default: Centre]\n"
                + "\n";

    /**
     * Creates and runs a ColourSorter with options specified by the string args.
     *
     * @param args  Argument string (use --help for more info)
     */
    public static void main(String[] args) {
        Map<String, Object> opts =
            new Docopt(doc).withVersion("Colour Sorter 1.0").parse(args);

        // Parse arguments
        int x = Integer.parseInt((String) opts.get("<x>"));
        int y = Integer.parseInt((String) opts.get("<y>"));
        String inputFile = (String) opts.get("<file>");

        // Options
        String outputFile = (String) opts.get("--output");
        boolean reverse = Boolean.parseBoolean((String) opts.get("--reverse"));
        double randFactor = Double.parseDouble((String) opts.get("--random"));
        boolean interleave = (boolean) opts.get("--interleave");

        ColourShuffleStrategy shuffler;
        if (interleave) {
            ColourShuffleStrategy shuffler1 = parseShuffler((String) opts.get("<sort1>"), false, 0);
            ColourShuffleStrategy shuffler2 = parseShuffler((String) opts.get("<sort2>"), false, 0);
            shuffler = new ShuffleStrategies.Interleaver(shuffler1, shuffler2);
            shuffler = applyModifiers(reverse, randFactor, shuffler);
        } else {
            shuffler = parseShuffler((String) opts.get("--sort"), reverse, randFactor);
        }
        DistanceMetric metric = parseMetric((String) opts.get("--distance"));
        Checker checker = parseChecker((String) opts.get("--checker"));
        String preset = (String) opts.get("--preset");

        // Set up and run
        ColourSorter sorter = new ColourSorter(x, y,
                                                inputFile,
                                                metric,
                                                checker,
                                                shuffler,
                                                outputFile,
                                                preset);
        sorter.run();
    }

    /**
     * Construct a ColourSorter.
     *
     * @param width             Output image width in pixels
     * @param height            Output image height in pixels
     * @param filename          Path string to input file
     * @param distanceMetric    {@link DistanceMetric} to use
     * @param shuffler          {@link ColourShuffleStrategy} to use
     * @param outputFilename    Path string for output file
     */
    public ColourSorter(int width,
                        int height,
                        String filename,
                        DistanceMetric distanceMetric,
                        Checker checker,
                        ColourShuffleStrategy shuffler,
                        String outputFilename,
                        String preset) {

        manager = new PixelManager(width, height, distanceMetric, checker);
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
                new MinChecker(),
                new ShuffleStrategies.Randomiser(
                        new ShuffleStrategies.BrightnessSorter()
                        ,0.2
                ),
                "output.png", ""
            );
    }

    public void run() {

        int total = manager.w * manager.h;
        int count = 0;
        int interval = 1000;
        for (int c : cManager) {
            manager.placeColour(c);
            count++;
            if (count % interval == 0) ProgressBar.update(count, total);
        }
        renderAndSave(outputFilename);

    }

    private void renderAndSave(String filename) {
        BufferedImage img = manager.render();

        try {
            File outputfile = new File(filename);
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
            case "Edge":
                switch (new Random().nextInt(4)) {
                    case 0:
                        manager.setAvailableLine(0, 0, 0, manager.h - 1);
                        break;
                    case 1:
                        manager.setAvailableLine(0, manager.h - 1, manager.w - 1, manager.h - 1);
                        break;
                    case 2:
                        manager.setAvailableLine(manager.w - 1, manager.h - 1, manager.w - 1, 0);
                        break;
                    case 3:
                        manager.setAvailableLine(manager.w - 1, 0, 0, 0);
                }
                break;
            case "Diagonal":
                switch (new Random().nextInt(3)) {
                    case 0:
                        manager.setAvailableLine(0, 0, manager.w, manager.h);
                        break;
                    case 1:
                        manager.setAvailableLine(manager.w, 0, 0, manager.h);
                        break;
                    case 2:
                        manager.setAvailableLine(0, 0, manager.w, manager.h);
                        manager.setAvailableLine(manager.w, 0, 0, manager.h);
                        break;
                }
                break;
            case "Random":
                manager.setAvailableRandom(10);
                break;
            case "RandomLine":
                manager.setAvailableLine(new Random().nextInt(manager.w ),
                                            new Random().nextInt(manager.h),
                                            new Random().nextInt(manager.w),
                                            new Random().nextInt(manager.h));
                break;
            case "Line":
                switch (new Random().nextInt(3)) {
                    case 0: // Horizontal line
                        manager.setAvailableLine(0, manager.h/2, manager.w, manager.h/2);
                        break;
                    case 1: // Vertical line
                        manager.setAvailableLine(manager.w/2, 0, manager.w/2, manager.h);
                        break;
                    case 2: //Both
                        manager.setAvailableLine(0, manager.h/2, manager.w, manager.h/2);
                        manager.setAvailableLine(manager.w/2, 0, manager.w/2, manager.h);
                        break;
                }
                break;
            default:
                // If not a recognised preset, assume this is a path to a file to use as a template
                BufferedImage originalTemplate = null;
                BufferedImage templateImg = null;
                try {
                    originalTemplate = ImageIO.read(new File(preset));
                    templateImg = new BufferedImage(manager.w, manager.h, BufferedImage.TYPE_INT_ARGB);

                    Graphics g = templateImg.createGraphics();
                    g.drawImage(originalTemplate, 0, 0, manager.w, manager.h, null);
                    g.dispose();
                } catch (IOException e) {
                    System.out.println(e.getMessage() + preset);
                }

                for (int i=0; i<manager.w; i++) {
                    for (int j=0; j<manager.h; j++) {
                        int c = templateImg.getRGB(i, j);
                        int[] rgb = Utils.getRgb(c);
                        // If non black set available
                        if (rgb[0] > 0 || rgb[1] > 0 || rgb[2] > 0) {
                            manager.setAvailable(i,j);
                        }
                    }
                }
        }
    }

    private static Checker parseChecker(String method) {
        Checker checker = new MinChecker();
        switch (method) {
            case "min":
                checker = new MinChecker();
                break;
            case "max":
                checker = new MaxChecker();
                break;
            case "mean":
                checker = new MeanChecker();
                break;
            case "mod":
                checker = new ModifiedMinChecker();
                break;
            default:
                System.out.println("Unrecognised checker options");
        }
        return checker;
    }


    private static ColourShuffleStrategy parseShuffler(String optionString, boolean reverse, double randFactor) {
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
        shuffler = applyModifiers(reverse, randFactor, shuffler);
        return shuffler;
    }

    private static ColourShuffleStrategy applyModifiers(boolean reverse, double randFactor, ColourShuffleStrategy shuffler) {
        if (reverse) {
            shuffler = new ShuffleStrategies.Reverser(shuffler);
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

    private class saveTask implements Runnable {
        int i = 0;
        boolean keepGoing = true;

        public void stop() {
            keepGoing = false;
        }

        public void run() {
            while (keepGoing) {
                renderAndSave(i + ".png");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
        }
    }
}
