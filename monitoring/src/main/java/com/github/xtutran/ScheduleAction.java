package com.github.xtutran;

import java.util.Timer;
import java.util.TimerTask;

public class ScheduleAction<T extends TimerTask> {
    public static boolean isActiveLog = true;
    protected Timer timer;
    protected int periodTime;         // period time in seconds
    protected Class<T> cl;

    public ScheduleAction(int seconds, Class<T> cl) {
        this.cl = cl;
        try {
            timer = new Timer();
            T t;
            t = cl.newInstance();
            timer.schedule(t, 0, seconds * 1000);
            periodTime = seconds;
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void restart() {
        timer.cancel();
        timer = new Timer();
        isActiveLog = true;
        T t;
        try {
            t = cl.newInstance();
            timer.schedule(t, 0, periodTime * 1000);
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void cancel() {
        timer.cancel();
        isActiveLog = false;
    }

    public int getPeriod() {
        return periodTime;
    }

    public void setPeriod(int seconds) {
        periodTime = seconds;
        this.restart();
    }
}
