package com.github.xtutran.detection;

import com.github.xtutran.TaskFactory;
import com.github.xtutran.ui.MainFrame;

import java.util.Calendar;
import java.util.TimerTask;

public class EvenDetectionTask extends TimerTask {

    @Override
    public void run() {
        EventDetection.addMoreWorkingTime();
        MainFrame.getInstance().setCurrentIdleTime(EventDetection.getCurrentIdleTime());

        Calendar currentTime = Calendar.getInstance();
        long duration = currentTime.getTime().getTime()
                - EventDetection.lastDetection.getTime();
        //pause because of idle
        if (duration >= 1000 * TaskFactory.timeToPause) {
            long duration2 = currentTime.getTime().getTime()
                    - EventDetection.lastAddStop.getTime();

            //add only one stop
            if (duration2 >= 1000 * TaskFactory.updateTime) {
                Message.addMessage(Message.STOP);
                EventDetection.lastAddStop = currentTime.getTime();
                TaskFactory.cancelLogTasks();
                Message.addMessageBatch();
                MainFrame.getInstance().updateLog();
                MainFrame.getInstance().temporaryPause();
            }
            return;
        }

        if (duration >= 1000) {
            EventDetection.addMoreTotalIdleTime();
            EventDetection.addMoreCurrentIdleTime();
        }

        //if there is no event in a duration of updateTime
        if (duration >= 1000 * TaskFactory.updateTime) {
            long duration2 = currentTime.getTime().getTime()
                    - EventDetection.lastAddIdle.getTime();
            if (duration2 >= 1000 * TaskFactory.updateTime) {
                Message.addMessage(Message.IDLE);

                EventDetection.lastAddIdle = currentTime.getTime();
            }

            if (1000 + 1000 * TaskFactory.updateTime > duration) {
                Message.addMessage(Message.TIME_WARNING);
            }
        }
    }
}
