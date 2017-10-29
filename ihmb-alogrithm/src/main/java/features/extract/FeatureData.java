package features.extract;

import features.HSV166Histogram;
import net.semanticmetadata.lire.imageanalysis.LireFeature;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Arrays;

import java.util.List;

public class FeatureData implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6330691198474391299L;
    private LinkedList<Double> hsv166Histogram;
    private String imagePath;

    private FeatureData(String imagePath) {
        this.imagePath = imagePath;
        this.hsv166Histogram = new LinkedList<Double>();
    }

    public static FeatureData extract(File imageFile) throws IOException {
        FeatureData feature = new FeatureData(imageFile.getPath());
        HSV166Histogram sch = new HSV166Histogram();
        sch.extract(ImageIO.read(imageFile));
        feature.add(sch.getDoubleHistogram());
        return feature;
    }

    private void add(double[] newFeatures) {
        for(double feature : newFeatures) {
            this.hsv166Histogram.add(feature);
        }
    }

    @Override
    public String toString() {
        List<Object> metadata = new ArrayList<Object>();
        metadata.add("hsv166Histogram");
        metadata.add(hsv166Histogram.size());
        metadata.addAll(hsv166Histogram);

        return StringUtils.join(metadata, ' ');
    }

    public LireFeature getLireFeatureFromRepresentation(String StringRepresentation) {
        LireFeature sch;
        sch = new HSV166Histogram();
        sch.setStringRepresentation(StringRepresentation);
        return sch;
    }

    public String getImagePath() {
        return imagePath;
    }

    public static void main(String args[]) throws IOException {
        FeatureData fd = FeatureData.extract(new File("/Volumes/BOOTCAMP/Code/previous_work/school-projects/ihmb-alogrithm/indexing/0001.jpg"));

        System.out.println(fd);
        LireFeature sch = fd.getLireFeatureFromRepresentation(fd.toString());
        System.out.println(sch.getDistance(sch));
    }
}
