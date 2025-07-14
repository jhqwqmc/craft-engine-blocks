package cn.gtemc.craftengine.scheduler.impl;

import cn.gtemc.craftengine.scheduler.SchedulerTask;

public class DummyTask implements SchedulerTask {

    @Override
    public void cancel() {
    }

    @Override
    public boolean cancelled() {
        return true;
    }
}
