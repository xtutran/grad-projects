package com.github.xtutran.ui.service.utilizer;

import com.github.xtutran.ScheduleAction;
import com.github.xtutran.ui.service.tasktimer.UpdateReportTask;

public class UpdateReport extends ScheduleAction<UpdateReportTask> {

    public UpdateReport(int seconds) {
        super(seconds, UpdateReportTask.class);
        // TODO Auto-generated constructor stub
    }
}
