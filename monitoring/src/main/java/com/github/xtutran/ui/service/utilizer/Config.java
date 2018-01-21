package com.github.xtutran.ui.service.utilizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    /**
     * Name of the file we are looking for to read the configuration.
     */
    private static final String RESOURCE_NAME = "config.properties";
    private static final Config INSTANCE = new Config();
    private final Properties data;

    private Config() {
        data = new Properties();
        load();
    }

    public static Config getInstance() {
        return INSTANCE;
    }

    private void load() {
        System.out.println("load");
        try {
            File optionFile = new File(RESOURCE_NAME);
            if (!optionFile.exists()) {
                System.exit(1);
            }

            InputStream in = new FileInputStream(optionFile);
            data.load(in);
            in.close();

        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println(ex.getMessage());
        }
    }

    private int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        } catch (NullPointerException e) {
            return defaultValue;
        }
    }

    public int getUpdateTime() {
        return parseInt(data.getProperty("updateTime"), 2);
    }

    public int getTakeSnapShotTime() {
        return parseInt(data.getProperty("takeSnapShotTime"), 6);
    }

    public int getUpdateContinuously() {
        return parseInt(data.getProperty("updateContinuously"), 1);
    }

    public int getUpdateMinute() {
        return parseInt(data.getProperty("updateMinute"), 6);
    }

    public int getTimeToPause() {
        return parseInt(data.getProperty("timeToPause"), 12);
    }

    public int getUpdateReport() {
        return parseInt(data.getProperty("updateReport"), 6);
    }

    public String getHost() {
        return data.getProperty("host", "192.168.1.240");
    }

    public String getFTPFolder() {
        return data.getProperty("directory", "/report");
    }

}
