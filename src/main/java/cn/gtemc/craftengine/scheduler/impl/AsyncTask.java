package cn.gtemc.craftengine.scheduler.impl;

import cn.gtemc.craftengine.scheduler.SchedulerTask;

import java.util.concurrent.ScheduledFuture;

public class AsyncTask implements SchedulerTask {
    private final ScheduledFuture<?> future;

    public AsyncTask(ScheduledFuture<?> future) {
        this.future = future;
    }

    @Override
    public void cancel() {
        this.future.cancel(false);
    }

    @Override
    public boolean cancelled() {
        return this.future.isCancelled();
    }
}
