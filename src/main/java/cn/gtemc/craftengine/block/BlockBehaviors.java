package cn.gtemc.craftengine.block;

import cn.gtemc.craftengine.block.behavior.AdjustableRedstoneBlockBehavior;
import cn.gtemc.craftengine.block.behavior.ChunkLoaderBlockBehavior;
import cn.gtemc.craftengine.block.behavior.PickaxeBlockBehavior;
import cn.gtemc.craftengine.block.behavior.PlaceBlockBehavior;
import cn.gtemc.craftengine.util.RegistryUtils;
import net.momirealms.craftengine.core.util.Key;

public class BlockBehaviors {
    public static final Key CHUNK_LOADER_BLOCK = Key.of("gtemc:chunk_loader_block");
    public static final Key ADJUSTABLE_REDSTONE_BLOCK = Key.of("gtemc:adjustable_redstone_block");
    public static final Key PICKAXE_BLOCK = Key.of("gtemc:pickaxe_block");
    public static final Key PLACE_BLOCK = Key.of("gtemc:place_block");

    public static void register() {
        RegistryUtils.registerBlockBehavior(CHUNK_LOADER_BLOCK, ChunkLoaderBlockBehavior.FACTORY);
        RegistryUtils.registerBlockBehavior(ADJUSTABLE_REDSTONE_BLOCK, AdjustableRedstoneBlockBehavior.FACTORY);
        RegistryUtils.registerBlockBehavior(PICKAXE_BLOCK, PickaxeBlockBehavior.FACTORY);
        RegistryUtils.registerBlockBehavior(PLACE_BLOCK, PlaceBlockBehavior.FACTORY);
    }
}
