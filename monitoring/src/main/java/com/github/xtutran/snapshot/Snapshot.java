/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.xtutran.snapshot;

import com.github.xtutran.ScheduleAction;

/**
 * @author kekkaishi
 */
public class Snapshot extends ScheduleAction<SnapshotTask> {
    public Snapshot() {
        super(0, SnapshotTask.class);
    }

    public Snapshot(int seconds) {
        super(seconds, SnapshotTask.class);
    }
}
