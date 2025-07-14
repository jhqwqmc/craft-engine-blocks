package cn.gtemc.craftengine.scheduler;

public interface SchedulerTask {

    void cancel();

    boolean cancelled();

}
