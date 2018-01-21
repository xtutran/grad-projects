package com.github.xtutran.ui.service.utilizer;

import com.github.xtutran.ScheduleAction;
import com.github.xtutran.ui.service.tasktimer.UpdateLoggerTask;

public class UpdateLogger extends ScheduleAction<UpdateLoggerTask> {

    public UpdateLogger(int seconds) {
        super(seconds, UpdateLoggerTask.class);
        // TODO Auto-generated constructor stub
    }
}
