package cn.gtemc.craftEngineBlocks.block;

import cn.gtemc.craftEngineBlocks.block.behavior.ChunkLoaderBlockBehavior;
import cn.gtemc.craftEngineBlocks.util.RegistryUtils;
import net.momirealms.craftengine.core.util.Key;

public class BlockBehaviors {
    public static final Key CHUNK_LOADER_BLOCK = Key.of("gtemc:chunk_loader_block");

    public static void register() {
        RegistryUtils.registerBlockBehavior(CHUNK_LOADER_BLOCK, ChunkLoaderBlockBehavior.FACTORY);
    }
}
