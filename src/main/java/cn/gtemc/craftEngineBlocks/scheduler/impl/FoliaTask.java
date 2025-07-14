package cn.gtemc.craftEngineBlocks.scheduler.impl;

import cn.gtemc.craftEngineBlocks.scheduler.SchedulerTask;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;

public class FoliaTask implements SchedulerTask {
    private final ScheduledTask task;

    public FoliaTask(ScheduledTask task) {
        this.task = task;
    }

    @Override
    public void cancel() {
        this.task.cancel();
    }

    @Override
    public boolean cancelled() {
        return this.task.isCancelled();
    }
}
