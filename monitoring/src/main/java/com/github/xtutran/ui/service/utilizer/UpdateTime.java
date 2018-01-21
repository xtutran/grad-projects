package com.github.xtutran.ui.service.utilizer;

import com.github.xtutran.ScheduleAction;
import com.github.xtutran.ui.service.tasktimer.UpdateTimeTask;

public class UpdateTime extends ScheduleAction<UpdateTimeTask> {

    public UpdateTime(int seconds) {
        super(seconds, UpdateTimeTask.class);
        // TODO Auto-generated constructor stub
    }
}
