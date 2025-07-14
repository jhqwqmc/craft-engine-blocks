package cn.gtemc.craftengine.scheduler.impl;

import cn.gtemc.craftengine.scheduler.SchedulerTask;

public class BukkitTask implements SchedulerTask {

    private final org.bukkit.scheduler.BukkitTask bukkitTask;

    public BukkitTask(org.bukkit.scheduler.BukkitTask bukkitTask) {
        this.bukkitTask = bukkitTask;
    }

    @Override
    public void cancel() {
        this.bukkitTask.cancel();
    }

    @Override
    public boolean cancelled() {
        return this.bukkitTask.isCancelled();
    }
}
