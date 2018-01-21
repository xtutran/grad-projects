/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.xtutran;

import com.github.xtutran.ui.LoginPanel;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author kekkaishi
 */
public class DesktopMonitoring {

    /**
     * the command line arguments
     */
    public static int NOT_START = 0, RUNNING = 1, PAUSED = 2, STOPPED = 3;
    public static int currentStatus = NOT_START;

    public static void setStatus(int status) {
        currentStatus = status;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        JFrame loginFrame = new LoginPanel();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        loginFrame.setLocation((width - loginFrame.getWidth()) / 2, (height - loginFrame.getHeight()) / 2);
        loginFrame.setVisible(true);

        //MainFrame frame = MainFrame.getInstance();
        //SingleInstance.getSingleInstance(frame) ;
        //frame.getRootPane().setContentPane(new settingForm());
        //frame.setVisible(true);
    }
}
