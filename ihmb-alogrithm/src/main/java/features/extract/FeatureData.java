package features.extract;

import features.HSV166Histogram;
import net.semanticmetadata.lire.imageanalysis.LireFeature;

import java.io.Serializable;
import java.util.LinkedList;

public class FeatureData implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 6330691198474391299L;
    String pathName = "";
    LinkedList<Double> HSV166Histogram;

    public FeatureData(String pathName) {
        this.pathName = pathName;
        HSV166Histogram = new LinkedList<Double>();
    }

    public String getpathName() {
        return pathName;
    }

    public void addHSV166HistogramFeatures(double[] newFeatures) {
        for (int i = 0; i < newFeatures.length; i++)
            HSV166Histogram.add((Double) newFeatures[i]);
    }

    public LinkedList<Double> getHSV166HistogramFeatures() {
        return HSV166Histogram;
    }

    public String getStringRepresentation() {
        return getHSV166HistogramStringRepresentation();
    }


    public String getHSV166HistogramStringRepresentation() {
        StringBuilder sb = new StringBuilder();
        sb.append("HSV166Histogram");
        sb.append(' ');
        sb.append(HSV166Histogram.size());
        sb.append(' ');
        for (int i = 0; i < HSV166Histogram.size(); i++) {
            sb.append(HSV166Histogram.get(i));
            sb.append(' ');
        }
        return sb.toString().trim();
    }


    public LireFeature getLireFeatureFromRepresentation(String StringRepresentation) {
        LireFeature sch;
        sch = new HSV166Histogram();
        sch.setStringRepresentation(StringRepresentation);
        return sch;
    }
}
