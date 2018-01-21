package com.github.xtutran.ui.service.tasktimer;

import com.github.xtutran.detection.EventDetection;
import com.github.xtutran.ui.MainFrame;

import java.util.TimerTask;

public class UpdateTimeAggregateTask extends TimerTask {

    @Override
    public void run() {
        MainFrame.getInstance().setTimeCounter(EventDetection.getWorkingTime());
        MainFrame.getInstance().setTotalIdleTime(EventDetection.getTotalIdleTime());
    }
}
