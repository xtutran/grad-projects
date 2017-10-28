package features;

import net.semanticmetadata.lire.imageanalysis.HSVColorHistogram.HistogramType;
import net.semanticmetadata.lire.imageanalysis.LireFeature;
import net.semanticmetadata.lire.utils.SerializationUtils;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Arrays;
import java.util.StringTokenizer;


public class HSV166Histogram implements LireFeature {
    public static HistogramType DEFAULT_HISTOGRAM_TYPE = HistogramType.HSV;
    public static DistanceFunction DEFAULT_DISTANCE_FUNCTION = DistanceFunction.HI;
    private static float q_h = 18;
    private static float q_s = 3;
    private static float q_v = 3;
    public static final int DEFAULT_NUMBER_OF_BINS = (int) (q_h * q_s * q_v) + 4;
    private static int histogramDistance[][] = new int[166][166];
    private static DistancePair[] distancesSet = new DistancePair[166 * 166];
    private static int maxDistance = 0;

    static {
        int count = 0;
        for (int i = 0; i < 166; i++) {
            for (int j = 0; j < 166; j++) {

                if (i >= 162 || j >= 162) {
                    int[] first = transformBinIndexToHsvAnchor(i);
                    int[] second = transformBinIndexToHsvAnchor(j);
                    histogramDistance[i][j] = 3 * Math.abs(first[1] - second[1])
                            + 3 * Math.abs(first[2] - second[2]);
                    distancesSet[count] = new DistancePair(histogramDistance[i][j], i, j);
                } else {
                    int[] first = transformBinIndexToHsvAnchor(i);
                    int[] second = transformBinIndexToHsvAnchor(j);
                    histogramDistance[i][j] = Math.min(
                            Math.abs(first[0] - second[0]),
                            18 - Math.abs(first[0] - second[0]))
                            + 3 * Math.abs(first[1] - second[1])
                            + 3 * Math.abs(first[2] - second[2]);
                    distancesSet[count] = new DistancePair(histogramDistance[i][j], i, j);
                }
                if (maxDistance < histogramDistance[i][j]) {
                    maxDistance = histogramDistance[i][j];
                }
                count++;
            }

        }
        System.out.println("maxDistance" + maxDistance);
        Arrays.sort(distancesSet);
    }

    /**
     * Temporary pixel field ... re used for speed and memory issues ...
     */
    private int[] pixel = new int[4];
    private double[] histogram;
    private HistogramType histogramType;
    private DistanceFunction distFunc;

    /**
     * Default constructor
     */
    public HSV166Histogram() {
        histogramType = DEFAULT_HISTOGRAM_TYPE;
        histogram = new double[DEFAULT_NUMBER_OF_BINS];
        distFunc = DEFAULT_DISTANCE_FUNCTION;
    }

    /**
     * Constructor for selecting different color spaces as well as a different
     * distance function. Histogram has 256 bins.
     *
     * @param distFunction
     */
    public HSV166Histogram(DistanceFunction distFunction) {
        distFunc = distFunction;
        histogramType = DEFAULT_HISTOGRAM_TYPE;
        histogram = new double[DEFAULT_NUMBER_OF_BINS];
    }

    private static int[] transformBinIndexToHsvAnchor(int binIndex) {
        int[] results = new int[3];
        if (binIndex < 162) {
            results[0] = binIndex / 9;
            int remaining = results[0] % 9;
            results[1] = remaining / 3;
            remaining = remaining % 3;
            // for normal color (from 0 to 162), value is seletecd to be 1,2,3
            results[2] = remaining + 1;
            return results;
        } else {
            results[0] = results[1] = 0;
            results[2] = binIndex - 162;
            return results;
        }
    }

    public static int quant(int[] pixel) {
        // int qH = (int) Math.floor((pixel[0] * 64f) / 360f); // more
        // granularity in color
        // int qS = (int) Math.floor((pixel[2] * 8f) / 100f);
        // return qH * 7 + qS;

        // 4 grey color
        if (pixel[0] == 0 && pixel[1] == 0) {
            int qV = (int) Math.floor((pixel[2] * q_v) / 100f + 0.5f);
            return 162 + qV;
        }
        // add 10 degree
        int qH = (int) (((int) Math.floor((pixel[0] * q_h) / 360f + 0.5f)) % q_h); // more
        // granularity
        // in
        // color
        int qS = (int) Math.floor((pixel[1] * q_s) / 100f);
        int qV = (int) Math.floor((pixel[2] * q_v) / 100f);
        if (qS == q_s)
            qS = (int) (q_s - 1);
        if (qV == q_v)
            qV = (int) (q_v - 1);

        return (qH) * (int) (q_v * q_s) + qS * (int) q_v + qV;
    }

    /**
     * Histogram intersection distance
     *
     * @param h1
     * @param h2
     * @return
     */
    private static double hi(double[] h1, double[] h2) {
        double sum = 0d;
        for (int i = 0; i < h1.length; i++) {
            sum += Math.abs(h1[i] - h2[i]);
        }
        return sum / h1.length;
    }

    /**
     * Histogram Euclidean distance
     *
     * @param h1
     * @param h2
     * @return
     */
    private static double hed(double[] h1, double[] h2) {
        double sum = 0d;
        for (int i = 0; i < h1.length; i++) {
            sum += (h1[i] - h2[i]) * (h1[i] - h2[i]);
        }
        return Math.sqrt(sum);
    }

    /**
     * Histogram Quadratic Distance Measures (HQDM)
     *
     * @param h1
     * @param h2
     * @return
     */
    private static double hqdm(double[] h1, double[] h2) {
        double sum = 0d;
        for (int i = 0; i < h1.length; i++)
            for (int j = 0; j < h2.length; j++) {

                sum += (h1[i] - h2[i])
                        * (h1[j] - h2[j])
                        * (1 - (double) histogramDistance[i][j]
                        / (double) maxDistance);
            }
        return sum;
    }

    /**
     * Integrated Histogram Bin Matching (IHBM)
     *
     * @param h1
     * @param h2
     * @return result in double
     */
    private static double ihbm(double[] h1, double[] h2) {
        double sum = 0d;
        //false if a histogram bin is not depleted
        boolean[] h1Status = new boolean[h1.length];
        boolean[] h2Status = new boolean[h2.length];
        double[] h1Tempo = new double[h1.length];
        double[] h2Tempo = new double[h2.length];
        for (int i = 0; i < h1Status.length; i++) {
            h1Status[i] = false;
            h1Tempo[i] = h1[i];
        }

        for (int i = 0; i < h2Status.length; i++) {
            h2Status[i] = false;
            h2Tempo[i] = h2[i];
        }

        for (int i = 0; i < distancesSet.length; i++) {
            if (!h1Status[distancesSet[i].first] && !h2Status[distancesSet[i].second]) {
                double w;

                if (h1Tempo[distancesSet[i].first] < h2Tempo[distancesSet[i].second]) {
                    w = h1Tempo[distancesSet[i].first];
                    h2Tempo[distancesSet[i].second] -= w;
                    h1Status[distancesSet[i].first] = true;
                } else {
                    w = h2Tempo[distancesSet[i].second];
                    h1Tempo[distancesSet[i].first] -= w;
                    h2Status[distancesSet[i].second] = true;
                    if (h1Tempo[distancesSet[i].first] == 0) {
                        h1Status[distancesSet[i].first] = true;
                    }
                }
                sum += distancesSet[i].distance * w;
            }
        }

        return sum;
    }

    /**
     * Adapted from ImageJ documentation:
     * http://www.f4.fhtw-berlin.de/~barthel/ImageJ
     * /ColorInspector//HTMLHelp/farbraumJava.htm
     *
     * @param r
     * @param g
     * @param b
     * @param yuv
     */
    public static void rgb2yuv(int r, int g, int b, int[] yuv) {
        int y = (int) (0.299 * r + 0.587 * g + 0.114 * b);
        int u = (int) ((b - y) * 0.492f);
        int v = (int) ((r - y) * 0.877f);

        yuv[0] = y;
        yuv[1] = u;
        yuv[2] = v;
    }

    /**
     * Adapted from ImageJ documentation:
     * http://www.f4.fhtw-berlin.de/~barthel/ImageJ
     * /ColorInspector//HTMLHelp/farbraumJava.htm
     *
     * @param r
     * @param g
     * @param b
     * @param hsv
     */
    public static void rgb2hsv(int r, int g, int b, int hsv[]) {

        int min;    //Min. value of RGB
        int max;    //Max. value of RGB
        int delMax; //Delta RGB value

        min = Math.min(r, g);
        min = Math.min(min, b);

        max = Math.max(r, g);
        max = Math.max(max, b);

        delMax = max - min;


        float H = 0f, S = 0f;
        float V = max / 255f;

        if (delMax == 0) {
            H = 0f;
            S = 0f;
        } else {
            S = delMax / max;
            if (r == max) {
                if (g >= b) {
                    H = ((g / 255f - b / 255f) / ((float) delMax / 255f)) * 60;
                } else {
                    H = ((g / 255f - b / 255f) / ((float) delMax / 255f)) * 60 + 360;
                }
            } else if (g == max) {
                H = (2 + (b / 255f - r / 255f) / ((float) delMax / 255f)) * 60;
            } else if (b == max) {
                H = (4 + (r / 255f - g / 255f) / ((float) delMax / 255f)) * 60;
            }
        }
//        System.out.println("H = " + H);
        hsv[0] = (int) (H);
        hsv[1] = (int) (S * 100);
        hsv[2] = (int) (V * 100);
    }

    /**
     * Adapted under GPL from VizIR: author was adis@ims.tuwien.ac.at
     */
    private static int[] rgb2hmmd(int ir, int ig, int ib) {
        int hmmd[] = new int[5];

        float max = (float) Math.max(Math.max(ir, ig), Math.max(ig, ib));
        float min = (float) Math.min(Math.min(ir, ig), Math.min(ig, ib));

        float diff = (max - min);
        float sum = (float) ((max + min) / 2.);

        float hue = 0;
        if (diff == 0)
            hue = 0;
        else if (ir == max && (ig - ib) > 0)
            hue = 60 * (ig - ib) / (max - min);
        else if (ir == max && (ig - ib) <= 0)
            hue = 60 * (ig - ib) / (max - min) + 360;
        else if (ig == max)
            hue = (float) (60 * (2. + (ib - ir) / (max - min)));
        else if (ib == max)
            hue = (float) (60 * (4. + (ir - ig) / (max - min)));

        diff /= 2;

        hmmd[0] = (int) (hue);
        hmmd[1] = (int) (max);
        hmmd[2] = (int) (min);
        hmmd[3] = (int) (diff);
        hmmd[4] = (int) (sum);

        return (hmmd);
    }

    public void setDistanceFunction(DistanceFunction distFunc) {
        this.distFunc = distFunc;
    }

    /**
     * Extracts the color histogram from the given image.
     *
     * @param image
     */
    public void extract(BufferedImage image) {
        if (image.getColorModel().getColorSpace().getType() != ColorSpace.TYPE_RGB)
            throw new UnsupportedOperationException(
                    "Color space not supported. Only RGB.");
        WritableRaster raster = image.getRaster();
        int count = 0;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                raster.getPixel(x, y, pixel);
                rgb2hsv(pixel[0], pixel[1], pixel[2], pixel);
                histogram[quant(pixel)]++;
                count++;
            }
        }
        normalize(histogram, count);
    }

    public byte[] getByteArrayRepresentation() {
        return SerializationUtils.toByteArray(histogram);
    }

    public void setByteArrayRepresentation(byte[] in) {
        int[] temporary = SerializationUtils.toIntArray(in);
        histogram = new double[temporary.length];
        for (int i = 0; i < temporary.length; i++) {
            histogram[i] = temporary[i];
        }
    }

    private void normalize(double[] histogram, int numPixels) {
        for (int i = 0; i < histogram.length; i++) {
//			if ( histogram[i] > 0 ) {
//				System.out.println(i + " " + histogram[i]);
//				System.out.println(i/9);
//				System.out.println( (i%9) / 3);
//				System.out.println( (i%9) % 3);
//			}
            histogram[i] /= numPixels;
        }
    }

    public float getDistance(LireFeature vd) {
        // Check if instance of the right class ...
        if (!(vd instanceof HSV166Histogram))
            throw new UnsupportedOperationException("Wrong descriptor.");

        // casting ...
        HSV166Histogram ch = (HSV166Histogram) vd;

        // check if parameters are fitting ...
        if ((ch.histogram.length != histogram.length)
                || (ch.histogramType != histogramType))
            throw new UnsupportedOperationException(
                    "Histogram lengths or color spaces do not match");

        // do the comparison ...
        double sum = 0;
        if (distFunc == DistanceFunction.HI)
            return (float) hi(histogram, ch.histogram);
        else if (distFunc == DistanceFunction.HED)
            return (float) hed(histogram, ch.histogram);
        else if (distFunc == DistanceFunction.HQDM)
            return (float) hqdm(histogram, ch.histogram);
        else
            return (float) ihbm(histogram, ch.histogram);
    }

	/* **************************************************************
     * Color Conversion routines ...
	 * *************************************************************
	 */

    public String getStringRepresentation() {
        StringBuilder sb = new StringBuilder(histogram.length * 4);
        sb.append(histogramType.name());
        sb.append(' ');
        sb.append(histogram.length);
        sb.append(' ');
        for (int i = 0; i < histogram.length; i++) {
            sb.append(histogram[i]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    public void setStringRepresentation(String s) {
        StringTokenizer st = new StringTokenizer(s);
        st.nextToken();
        histogram = new double[Integer.parseInt(st.nextToken())];
        for (int i = 0; i < histogram.length; i++) {
            if (!st.hasMoreTokens())
                throw new IndexOutOfBoundsException(
                        "Too few numbers in string representation!");
            histogram[i] = Double.parseDouble(st.nextToken());
        }
    }

    @Override
    public double[] getDoubleHistogram() {
        return histogram;
    }

    /**
     * Lists distance functions possible for this histogram class
     */
    public enum DistanceFunction {
        HI, HED, HQDM, IHBM
    }

}

class DistancePair implements Comparable<DistancePair> {
    double distance;
    int first;
    int second;

    DistancePair(double distance, int first, int second) {
        this.distance = distance;
        this.first = first;
        this.second = second;
    }

    @Override
    public int compareTo(DistancePair o) {
        // TODO Auto-generated method stub
        if (this.distance < o.distance) {
            return -1;
        } else if (this.distance > o.distance) {
            return 1;
        } else {
            return 0;
        }
    }
}
