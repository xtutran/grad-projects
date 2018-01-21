package com.github.xtutran.ui.service.tasktimer;

import com.github.xtutran.ui.MainFrame;

import java.util.TimerTask;

public class UpdateLoggerTask extends TimerTask {

    @Override
    public void run() {
        MainFrame.getInstance().updateLog();
    }
}