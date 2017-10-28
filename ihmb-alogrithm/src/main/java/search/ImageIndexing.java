package search;

import features.extract.FeatureData;
import features.extract.FeatureExtraction;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageIndexing {
    public static String databasefilename = "FeaturesDatabase.txt";
    String path;

    public ImageIndexing(String path) {
        this.databasefilename = path + "\\FeaturesDatabase.txt";
        this.path = path;
    }

    public static void setPath(String path) {
        databasefilename = path + "\\FeaturesDatabase.txt";
    }

    public static FeatureData featureExtractFromImage(BufferedImage image, String path) {
        FeatureExtraction fe = new FeatureExtraction();
        FeatureData fd = new FeatureData(path);

        fe.setImage(image);
        fd.addHSV166HistogramFeatures(fe.extractFeatures());
//		System.out.println(fd.getHSV166HistogramStringRepresentation());
        return fd;
    }

    public void indexImages() {
        File data = new File(databasefilename);
        if (!data.exists()) {
            try {
                data.createNewFile();
            } catch (IOException e) {
            }
        }

        File folder;

        // LinkedList<File> allFiles = new LinkedList<File>();

        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(
                    databasefilename));

            // For each image in the database, create a FeatureData object and
            // write it into file
            folder = new File(path);

            for (File f : folder.listFiles()) {
                String s = f.getName().toLowerCase();
                if (s.endsWith(".jpg") || s.endsWith(".png")) {
                    try {
                        BufferedImage image = ImageIO.read(f);
                        String absolutePath = path + "\\" + f.getName();
                        outputStream
                                .writeObject(featureExtractFromImage(image, absolutePath));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Close the ObjectOutputStream
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }
}
