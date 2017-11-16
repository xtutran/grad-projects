package search;

import evaluation.Pair;
import features.HSV166Histogram;
import features.HSV166Histogram.DistanceFunction;
import features.extract.FeatureData;
import gui.MainFrame;
import net.semanticmetadata.lire.imageanalysis.LireFeature;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.Map.Entry;

public class ImageSearcher {

    private static ImageSearcher INSTANCE = new ImageSearcher();
    // private BufferedImage image;
    private SearchKey key = null;
    private Map<SearchKey, Map<Pair<String>, Double>> searchingDb;

    private ImageSearcher() {
        searchingDb = new HashMap<SearchKey, Map<Pair<String>, Double>>();
    }

    public static final ImageSearcher getInstance() {
        return INSTANCE;
    }

    // public void set(File inputImage, DistanceMeasureMethod distanceMethod) {

    // }

    public void calcSimilarity(File inputImage, DistanceFunction distanceMethod) throws IOException {
        this.key = new SearchKey(inputImage.getAbsolutePath(), distanceMethod);
        if (searchingDb.containsKey(key)) {
            return;
        }
        searchingDb.put(key, new HashMap<Pair<String>, Double>());

        Map<Pair<String>, Double> scores = searchingDb.get(key);
        FeatureData imageFeatures = FeatureData.extract(inputImage);
        LireFeature searchTarget = imageFeatures.getLireFeatureFromRepresentation(imageFeatures.toString());

        ((HSV166Histogram) searchTarget).setDistanceFunction(DistanceFunction.values()[distanceMethod.ordinal()]);
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(MainFrame.IMAGE_DB));

            Object obj = inputStream.readObject();
            if (obj instanceof List) {
                @SuppressWarnings("unchecked")
                List<FeatureData> features = (List<FeatureData>) obj;
                for (FeatureData fd : features) {
                    LireFeature searchSample = fd.getLireFeatureFromRepresentation(fd.toString());
                    Pair<String> pair = new Pair<String>(inputImage.getAbsolutePath(), fd.getImagePath());
                    double score = (double) searchTarget.getDistance(searchSample);
                    scores.put(pair, score);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Close the ObjectOutputStream
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public Object[][] search(File inputImage, DistanceFunction distanceMethod, int k) {
        key = new SearchKey(inputImage.getAbsolutePath(), distanceMethod);
        if (!searchingDb.containsKey(key)) {
            return new Object[][]{};
        }

        Map<Pair<String>, Double> scores = topK(searchingDb.get(key), k);
        Object[][] results = new Object[k][2];
        int i = 0;
        for (Map.Entry<Pair<String>, Double> entry : scores.entrySet()) {
            results[i][0] = entry.getKey().second;
            results[i][1] = entry.getValue();
            System.out.println(entry.getKey().second);
            System.out.println(entry.getValue());
            i++;
        }
        return results;
    }

    /*
     * Java method to sort Map in Java by value e.g. HashMap or Hashtable throw
     * NullPointerException if Map contains null values It also sort values even
     * if they are duplicates
     */
    public Map<Pair<String>, Double> topK(Map<Pair<String>, Double> map, int k) {
        List<Map.Entry<Pair<String>, Double>> entries = new LinkedList<Map.Entry<Pair<String>, Double>>(map.entrySet());

        Collections.sort(entries, new Comparator<Map.Entry<Pair<String>, Double>>() {
            @Override
            public int compare(Entry<Pair<String>, Double> o1, Entry<Pair<String>, Double> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        // LinkedHashMap will keep the keys in the order they are inserted
        // which is currently sorted on natural ordering
        Map<Pair<String>, Double> sortedMap = new LinkedHashMap<Pair<String>, Double>();
        int i = 0;
        for (Map.Entry<Pair<String>, Double> entry : entries) {
            if (i >= k) {
                break;
            }
            sortedMap.put(entry.getKey(), entry.getValue());
            i++;
        }

        return sortedMap;
    }

}