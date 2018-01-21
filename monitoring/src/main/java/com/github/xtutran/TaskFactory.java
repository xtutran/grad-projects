package com.github.xtutran;

import com.github.xtutran.ui.service.utilizer.*;
import com.github.xtutran.detection.EventDetection;
import com.github.xtutran.detection.Message;
import com.github.xtutran.snapshot.Snapshot;

public class TaskFactory {
    // static int updateTime = 20;
    // static int takeSnapShotTime = 60;
    // static int updateContinuously = 1;
    // static int updateMinute = 60;
    // static int timeToPause = 120;

    public static int updateTime = 2;
    public static int takeSnapShotTime = 6;
    public static int updateContinuously = 1;
    public static int updateMinute = 6;
    public static int timeToPause = 12;
    public static int updateReport = 6;

    static Snapshot ss;
    static Message me;
    static UpdateLogger log;
    static UpdateTime time;
    static EventDetection eveDe;
    static UpdateTimeAggregate upTime;
    static UpdateReport upReport;

    public static void factory() {
        updateTime = Config.getInstance().getUpdateTime();
        takeSnapShotTime = Config.getInstance().getTakeSnapShotTime();
        updateContinuously = Config.getInstance().getUpdateContinuously();
        updateMinute = Config.getInstance().getUpdateMinute();
        timeToPause = Config.getInstance().getTimeToPause();
        updateReport = Config.getInstance().getUpdateReport();

        ss = new Snapshot(takeSnapShotTime);
        me = new Message(updateTime);
        log = new UpdateLogger(updateTime);
        time = new UpdateTime(updateMinute);
        eveDe = new EventDetection(updateContinuously, updateTime, timeToPause);
        upTime = new UpdateTimeAggregate(updateMinute);
        upReport = new UpdateReport(updateReport);
    }

    public static void cancelLogTasks() {
        if (ss != null)
            ss.cancel();
        if (me != null)
            me.cancel();
        if (log != null)
            log.cancel();
        if (eveDe != null)
            eveDe.cancel();
        if (upTime != null)
            upTime.cancel();
        if (upReport != null)
            upReport.cancel();
    }

    public static void restartLogTasks() {
        factory();
        ss.restart();
        me.restart();
        log.restart();
        eveDe.restart();
        upTime.restart();
        upReport.restart();
    }
}
