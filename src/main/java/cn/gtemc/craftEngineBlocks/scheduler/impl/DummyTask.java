package cn.gtemc.craftEngineBlocks.scheduler.impl;

import cn.gtemc.craftEngineBlocks.scheduler.SchedulerTask;

public class DummyTask implements SchedulerTask {

    @Override
    public void cancel() {
    }

    @Override
    public boolean cancelled() {
        return true;
    }
}
