package search;

import features.extract.FeatureData;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImageIndexing {
    private String dbPath;
    private List<FeatureData> db;
    private final static ImageIndexing INSTANCE = new ImageIndexing();

    private ImageIndexing() {
        this.dbPath = "features.db";
        this.db = new ArrayList<FeatureData>();
    }

    public static ImageIndexing getInstance(String path) {
        INSTANCE.dbPath = path;
        return INSTANCE;
    }

    public String getDbPath() {
        return dbPath;
    }

    public void createImageDb(String datasetPath, boolean append) {

        if (!append && !db.isEmpty()) {
            db.clear();
        }

        File datasetFile = new File(datasetPath);

        if (datasetFile.isDirectory()) {
            Iterator<File> files = FileUtils.iterateFiles(datasetFile, new String[]{"jpg", "png"}, false);
            while (files.hasNext()) {
                try {
                    this.db.add(FeatureData.extract(files.next()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (datasetFile.isFile()) {
            try {
                this.db.add(FeatureData.extract(datasetFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println(datasetPath + "  not a file or directory!");
        }
    }

    public void indexImages() {
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(dbPath));
            outputStream.writeObject(this.db);
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

    public static void main(String args[]) {
        ImageIndexing indexor = ImageIndexing.getInstance("/Volumes/BOOTCAMP/Code/previous_work/school-projects/ihmb-alogrithm/features.db");
        indexor.createImageDb("/Volumes/BOOTCAMP/Code/previous_work/school-projects/ihmb-alogrithm/indexing", false);
        indexor.indexImages();
    }
}

