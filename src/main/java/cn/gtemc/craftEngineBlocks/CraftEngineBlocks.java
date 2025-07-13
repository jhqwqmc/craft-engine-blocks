package cn.gtemc.craftEngineBlocks;

import cn.gtemc.craftEngineBlocks.block.BlockBehaviors;
import org.bukkit.plugin.java.JavaPlugin;

public final class CraftEngineBlocks extends JavaPlugin {
    private static CraftEngineBlocks instance;

    @Override
    public void onLoad() {
        instance = this;
        BlockBehaviors.register();
        getLogger().info("CraftEngine Blocks Extensions Loaded");
    }

    public static CraftEngineBlocks instance() {
        return instance;
    }
}
