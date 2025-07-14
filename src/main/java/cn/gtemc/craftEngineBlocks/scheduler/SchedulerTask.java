package cn.gtemc.craftEngineBlocks.scheduler;

public interface SchedulerTask {

    void cancel();

    boolean cancelled();

}
