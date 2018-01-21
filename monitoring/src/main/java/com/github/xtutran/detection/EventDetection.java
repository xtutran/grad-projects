package com.github.xtutran.detection;

import com.github.xtutran.FreelancerMonitoring;
import com.github.xtutran.ScheduleAction;
import com.github.xtutran.TaskFactory;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.NativeInputEvent;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseInputListener;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Orrmaster
 */

public class EventDetection extends ScheduleAction<EvenDetectionTask> implements NativeKeyListener, NativeMouseInputListener {
    public static Date lastDetection = new Date();
    public static Date lastAddMovementDetection = Calendar.getInstance().getTime();
    public static Date lastAddIdle = Calendar.getInstance().getTime();
    public static Date lastAddStop = Calendar.getInstance().getTime();
    private static EventDetection currentEventDetector;
    private static int totalWorkingTime = 0;
    private static int totalIdleTime = 0;
    private static int currentIdleTime = 0;

    public EventDetection(int timeToCheckDelay, int updateTime, int timeToPause) {
        super(timeToCheckDelay, EvenDetectionTask.class);
        this.periodTime = timeToCheckDelay;

        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

        // Disable log
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        // Don't forget to disable the parent handlers.
        logger.setUseParentHandlers(false);

        // Don't forget to disable the parent handlers.
        GlobalScreen.addNativeKeyListener(this);
        GlobalScreen.addNativeMouseListener(this);
        GlobalScreen.addNativeMouseMotionListener(this);
    }

    public static EventDetection getInstance() {
        if (currentEventDetector == null) {
            currentEventDetector = new EventDetection(
                    TaskFactory.updateContinuously, TaskFactory.updateTime,
                    TaskFactory.timeToPause);
        }
        return currentEventDetector;
    }

    public static void addMoreWorkingTime() {
        totalWorkingTime++;
    }

    public static int getWorkingTime() {
        return totalWorkingTime;
    }

    public static void addMoreTotalIdleTime() {
        totalIdleTime++;
    }

    public static int getTotalIdleTime() {
        return totalIdleTime;
    }

    public static void addMoreCurrentIdleTime() {
        currentIdleTime++;
    }

    public static int getCurrentIdleTime() {
        return currentIdleTime;
    }

    public void updateLastDetection() {
        lastDetection = Calendar.getInstance().getTime();
    }

    private void updateInputDetection(NativeInputEvent e) {

        //System.out.println("Dectect: " + e.paramString());

        currentIdleTime = 0;
        Calendar currentTime = Calendar.getInstance();
        long duration = currentTime.getTime().getTime()
                - EventDetection.lastAddMovementDetection.getTime();

        if (duration >= 1000 * TaskFactory.updateTime) {
            Message.addMessage(Message.DETECT);
            lastAddMovementDetection = currentTime.getTime();
            return;
        }

        if (!isActiveLog) {

            if (FreelancerMonitoring.currentStatus == FreelancerMonitoring.RUNNING) {
                Message.addMessage(Message.RESUME);
                long duration2 = currentTime.getTime().getTime()
                        - EventDetection.lastDetection.getTime();
                totalIdleTime += duration2 / 1000;
                TaskFactory.restartLogTasks();
            }
        }
        updateLastDetection();
    }

    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) {
        updateInputDetection(nativeKeyEvent);
    }

    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        updateInputDetection(nativeKeyEvent);
    }

    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        updateInputDetection(nativeKeyEvent);
    }

    public void nativeMouseClicked(NativeMouseEvent nativeMouseEvent) {
        updateInputDetection(nativeMouseEvent);
    }

    public void nativeMousePressed(NativeMouseEvent nativeMouseEvent) {
        updateInputDetection(nativeMouseEvent);
    }

    public void nativeMouseReleased(NativeMouseEvent nativeMouseEvent) {
        updateInputDetection(nativeMouseEvent);
    }

    public void nativeMouseMoved(NativeMouseEvent nativeMouseEvent) {
        updateInputDetection(nativeMouseEvent);
    }

    public void nativeMouseDragged(NativeMouseEvent nativeMouseEvent) {
        updateInputDetection(nativeMouseEvent);
    }
}
