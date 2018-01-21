package com.github.xtutran.ui.service.tasktimer;

import com.github.xtutran.ui.MainFrame;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

public class UpdateTimeTask extends TimerTask {

    @Override
    public void run() {
        DateFormat dateFormat = new SimpleDateFormat("MMM yyyy dd, hh:mm a");

        // get current date time with Calendar()
        Calendar cal = Calendar.getInstance();
        String currentTime = dateFormat.format(cal.getTime());
        MainFrame.getInstance().setTextTime(currentTime);
    }
}
