package com.github.xtutran.ui.service.utilizer;

import com.github.xtutran.ScheduleAction;
import com.github.xtutran.ui.service.tasktimer.UpdateTimeAggregateTask;

public class UpdateTimeAggregate extends ScheduleAction<UpdateTimeAggregateTask> {

    public UpdateTimeAggregate(int seconds) {
        super(seconds, UpdateTimeAggregateTask.class);
    }
}

