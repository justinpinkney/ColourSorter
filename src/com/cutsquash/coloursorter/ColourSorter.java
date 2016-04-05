package com.cutsquash.coloursorter;

import com.cutsquash.coloursorter.model.ColourManager;
import com.cutsquash.coloursorter.model.DistanceMetricRGB;
import com.cutsquash.coloursorter.model.PixelManager;
import javafx.scene.image.Image;
import org.docopt.Docopt;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by jnp1 on 05/04/2016.
 */
public class ColourSorter {

    private static final String doc =
        "Colour Sorter.\n"
                + "\n"
                + "Usage:\n"
                + "  ColourSorter <x> <y> <file> [ --output=<outputFile> ]\n"
                + "  ColourSorter (-h | --help)\n"
                + "  ColourSorter --version\n"
                + "\n"
                + "Options:\n"
                + "  -h --help     Show this screen.\n"
                + "  --version     Show version.\n"
                + "  --speed=<kn>  Speed in knots [default: 10].\n"
                + "  --moored      Moored (anchored) mine.\n"
                + "  --drifting    Drifting mine.\n"
                + "\n";

    public static void main(String[] args) {
        Map<String, Object> opts =
            new Docopt(doc).withVersion("Colour Sorter 1.0").parse(args);
        System.out.println(opts);
        ColourSorter sorter = new ColourSorter();
        sorter.run(
                Integer.parseInt((String) opts.get("<x>")),
                Integer.parseInt((String) opts.get("<y>")),
                (String) opts.get("<file>"));
    }

    public void run(int width, int height, String filename) {

        PixelManager manager = new PixelManager(width, height, new DistanceMetricRGB());
        for (int i = 0; i < width; i++) {
            manager.setAvailable(0, 0);
        }
        ColourManager cManager = new ColourManager(filename, width, height);

        for (int c : cManager) manager.placeColour(c);

        BufferedImage img = manager.render();

        try {
            File outputfile = new File("saved.png");
            ImageIO.write((RenderedImage) img, "png", outputfile);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}
