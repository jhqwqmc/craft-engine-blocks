package cn.gtemc.craftEngineBlocks.block.behavior;

import net.momirealms.craftengine.bukkit.block.behavior.BukkitBlockBehavior;
import net.momirealms.craftengine.core.block.BlockBehavior;
import net.momirealms.craftengine.core.block.CustomBlock;
import net.momirealms.craftengine.core.block.behavior.BlockBehaviorFactory;

import java.util.Map;

public class ChunkLoaderBlockBehavior extends BukkitBlockBehavior {
    public static final Factory FACTORY = new Factory();

    public ChunkLoaderBlockBehavior(CustomBlock customBlock) {
        super(customBlock);
    }

    public static class Factory implements BlockBehaviorFactory {

        @Override
        public BlockBehavior create(CustomBlock block, Map<String, Object> arguments) {
            return new ChunkLoaderBlockBehavior(block);
        }
    }
}
