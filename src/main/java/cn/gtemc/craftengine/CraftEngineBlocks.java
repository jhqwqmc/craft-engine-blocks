package cn.gtemc.craftengine;

import cn.gtemc.craftengine.block.BlockBehaviors;
import cn.gtemc.craftengine.classpath.ClassPathAppender;
import cn.gtemc.craftengine.classpath.impl.ReflectionClassPathAppender;
import cn.gtemc.craftengine.dependency.Dependencies;
import cn.gtemc.craftengine.dependency.DependencyManager;
import cn.gtemc.craftengine.dependency.DependencyManagerImpl;
import cn.gtemc.craftengine.entity.seat.SeatManager;
import cn.gtemc.craftengine.injector.PlaceBlockBlockPlaceContextGenerator;
import cn.gtemc.craftengine.plugin.context.event.EventFunctions;
import cn.gtemc.craftengine.scheduler.JavaScheduler;
import cn.gtemc.craftengine.util.Reflections;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public final class CraftEngineBlocks extends JavaPlugin {
    private static CraftEngineBlocks instance;
    private JavaScheduler scheduler;
    private ClassPathAppender classPathAppender;
    private DependencyManager dependencyManager;
    private SeatManager seatManager;

    @Override
    public void onLoad() {
        instance = this;
        initPlugin();
    }

    @Override
    public void onEnable() {
        this.seatManager = new SeatManager(this);
        this.scheduler.sync().runDelayed(() -> {
            this.seatManager.delayedInit();
        });
    }

    @Override
    public void onDisable() {
        if (this.seatManager != null) this.seatManager.disable();
        if (this.scheduler != null) this.scheduler.shutdownScheduler();
        if (this.scheduler != null) this.scheduler.shutdownExecutor();
        if (this.dependencyManager != null) this.dependencyManager.close();
    }

    private void initPlugin() {
        this.scheduler = new JavaScheduler(this);
        this.classPathAppender = new ReflectionClassPathAppender(this.getClass().getClassLoader());
        initDependencyManager();
        Reflections.init();
        PlaceBlockBlockPlaceContextGenerator.init();
        BlockBehaviors.register();
        EventFunctions.register();
    }

    private void initDependencyManager() {
        this.dependencyManager = new DependencyManagerImpl(this);
        this.dependencyManager.loadDependencies(Set.of(
                Dependencies.BYTE_BUDDY,
                Dependencies.GSON
        ));
    }

    public JavaScheduler scheduler() {
        return this.scheduler;
    }

    public ClassPathAppender classPathAppender() {
        return this.classPathAppender;
    }

    public DependencyManager dependencyManager() {
        return this.dependencyManager;
    }

    public static CraftEngineBlocks instance() {
        return instance;
    }
}
