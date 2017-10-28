package search;

import features.HSV166Histogram;
import features.HSV166Histogram.DistanceFunction;
import features.extract.FeatureData;
import net.semanticmetadata.lire.imageanalysis.LireFeature;
import gui.MainFrame.DistanceMeasureMethod;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.LinkedList;

public class ImageSearcher {

    private BufferedImage image;



//    public ImageSearcher() {
//
//    }

    public String[][] searchFromFile(BufferedImage image, DistanceMeasureMethod distanceMethod,
                                     int kForNearestNeibor) {
        this.image = image;

        FeatureData imageFeatures = ImageIndexing.featureExtractFromImage(image, "");
        System.out.println(imageFeatures.getStringRepresentation());

        LireFeature searchTarget = imageFeatures.getLireFeatureFromRepresentation(imageFeatures.getStringRepresentation());

        ((HSV166Histogram) searchTarget).setDistanceFunction(DistanceFunction.values()[distanceMethod.ordinal()]);
//		System.out.println(imageFeatures.getStringRepresentation(featureRearrange));
        LinkedList<FileSummary> results = new LinkedList<FileSummary>();

        ObjectInputStream inputStream = null;
        KNearestNeighborsSearch searcher = new KNearestNeighborsSearch(kForNearestNeibor);

        try {
            inputStream = new ObjectInputStream(new FileInputStream(ImageIndexing.databasefilename));
            //main search code
            FeatureData fd;

            Object obj = null;

            while ((obj = inputStream.readObject()) != null) {

                if (obj instanceof FeatureData) {

                    fd = (FeatureData) obj;
                    if (fd != null) {
                        LireFeature searchSample = fd.getLireFeatureFromRepresentation(fd.getStringRepresentation());

//    					if (fd.getpathName().endsWith("929.jpg") ||  fd.getpathName().endsWith("64.jpg")){
//    						fw.write(fd.getpathName() + " " + searchTarget.getDistance(searchSample) + fd.getHSV166HistogramStringRepresentation() + "\r\n");
//    						System.out.println(fd.getpathName() + " " + searchTarget.getDistance(searchSample) + " " + fd.getHSV166HistogramStringRepresentation() + "\r\n");
//    					}
//    					
                        System.out.println(fd.getpathName() + " " + searchTarget.getDistance(searchSample));
                        searcher.addFeatureData(fd, searchTarget.getDistance(searchSample));
                    }
                }
            }

        } catch (EOFException ex) { //This exception will be caught when EOF is reached
            System.out.println("End of file reached.");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Close the ObjectOutputStream
            try {

                inputStream.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return searcher.getResults();
    }
}

class FileSummary implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 2478626863110765166L;
    public String path;

    FileSummary() {
    }

    FileSummary(String _path) {
        path = _path;
    }

    public String toString() {
        return path;
    }
}

class KNearestNeighborsSearch {
    int k;
    int numberOfFeatureData = 0;
    LinkedList<Double> searchDistance;
    LinkedList<FileSummary> samplesResults;

    public KNearestNeighborsSearch(int k) {
        this.k = k;
        searchDistance = new LinkedList<Double>();
        //samples = new LinkedList<FeatureData>();
        samplesResults = new LinkedList<FileSummary>();

        for (int i = 0; i < k; i++) {
            //searchDistance.add(10000000000000.0);
            searchDistance.add(Double.MAX_VALUE);
            //samples.add(new FeatureData());
            samplesResults.add(new FileSummary());
        }
    }

    public void addFeatureData(FeatureData fd, double searchDist) {
        numberOfFeatureData++;
        if (searchDist >= searchDistance.getLast()) {
            return;
        }
        int position = binarySearch(searchDistance, searchDist);
        if (searchDist == 0) {
            System.out.println(position);
        }
        searchDistance.add(position, searchDist);
        searchDistance.removeLast();
        samplesResults.add(position, new FileSummary(fd.getpathName()));
        samplesResults.removeLast();
    }

    public int binarySearch(LinkedList<Double> searchDistance, double searchDist) {
        int low = 0;
        int high = searchDistance.size() - 1;
        int mid = low;

        while (low < high) {
            mid = (int) Math.floor((low + high) / 2);

            if (searchDistance.get(mid) < searchDist)
                low = mid + 1;
            else if (searchDistance.get(mid) > searchDist)
                high = mid;
            else
                return mid;
        }

        return low;
    }

    public String[][] getResults() {
        if (numberOfFeatureData < k) {
            k = numberOfFeatureData;
        }


        String[][] results = new String[k][1];
        for (int i = 0; i < k; i++) {
            results[i][0] = samplesResults.get(i).path;
        }
        return results;
    }
}
