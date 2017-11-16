package features.extract;

import features.HSV166Histogram;

import java.awt.image.BufferedImage;

public class FeatureExtraction {
    BufferedImage image;
    String representation = "";

    public FeatureExtraction() {
    }

    public FeatureExtraction(BufferedImage _image) {
        image = _image;
    }

    public void setImage(BufferedImage _image) {
        image = _image;
    }


    public double[] extractFeatures() {
        HSV166Histogram sch = new HSV166Histogram();
        sch.extract(image);
        representation = sch.getStringRepresentation();
        return sch.getDoubleHistogram();
    }
}
