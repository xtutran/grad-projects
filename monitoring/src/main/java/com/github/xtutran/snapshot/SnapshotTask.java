package com.github.xtutran.snapshot;

import com.github.xtutran.detection.Message;
import com.github.xtutran.ftp.FTPClientHelper;
import com.github.xtutran.ui.MainFrame;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

public class SnapshotTask extends TimerTask {

    @Override
    public void run() {
        try {
            MainFrame mf = MainFrame.getInstance();

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

            //get current date time with Calendar()
            Calendar cal = Calendar.getInstance();
            String fileName = dateFormat.format(cal.getTime()) + ".jpg";

            DateFormat folderFormat = new SimpleDateFormat("MM-dd-yyyy");
            String ftpFolder = mf.getFtpFolder() + "/" + folderFormat.format(cal.getTime());

            System.out.println(fileName);
            File outputFile = new File(fileName);
            Robot robot = new Robot();
            Rectangle captureSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage bufferedImage = robot.createScreenCapture(captureSize);

            try {
                ImageIO.write(bufferedImage, "jpg", outputFile);
                System.out.println("hoho take screenshot");
                Message.addMessage(Message.SCREENSHOT);

                FTPClientHelper ftp = mf.getFtp();
                if (ftp != null) {
                    ftp.uploadFile(outputFile.getAbsolutePath(), ftpFolder + "/" + fileName);
                }
                FileUtils.deleteQuietly(outputFile);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (AWTException e) {
            e.printStackTrace();
            //throw e;
        }
    }
}
