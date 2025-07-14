package cn.gtemc.craftengine;

import cn.gtemc.craftengine.block.BlockBehaviors;
import cn.gtemc.craftengine.classpath.ClassPathAppender;
import cn.gtemc.craftengine.classpath.impl.ReflectionClassPathAppender;
import cn.gtemc.craftengine.dependency.Dependencies;
import cn.gtemc.craftengine.dependency.DependencyManager;
import cn.gtemc.craftengine.dependency.DependencyManagerImpl;
import cn.gtemc.craftengine.scheduler.JavaScheduler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public final class CraftEngineBlocks extends JavaPlugin {
    private static CraftEngineBlocks instance;
    private JavaScheduler scheduler;
    private ClassPathAppender classPathAppender;
    private DependencyManager dependencyManager;

    @Override
    public void onLoad() {
        instance = this;
        initPlugin();
        getLogger().info("CraftEngine Blocks Extensions Loaded");
    }

    @Override
    public void onDisable() {
        this.scheduler.shutdownScheduler();
        this.scheduler.shutdownExecutor();
        this.dependencyManager.close();
        getLogger().info("CraftEngine Blocks Extensions Disabled");
    }

    private void initPlugin() {
        this.scheduler = new JavaScheduler(this);
        this.classPathAppender = new ReflectionClassPathAppender(this.getClass().getClassLoader());
        initDependencyManager();
        BlockBehaviors.register();
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
