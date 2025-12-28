package cn.gtemc.craftengine.block;

import cn.gtemc.craftengine.block.behavior.*;
import cn.gtemc.craftengine.util.RegistryUtils;
import net.momirealms.craftengine.core.block.behavior.BlockBehaviorType;
import net.momirealms.craftengine.core.util.Key;

public final class BlockBehaviors {
    private BlockBehaviors() {}

    public static final BlockBehaviorType<ChunkLoaderBlockBehavior> CHUNK_LOADER_BLOCK = RegistryUtils.registerBlockBehavior(Key.of("gtemc:chunk_loader_block"), ChunkLoaderBlockBehavior.FACTORY);
    public static final BlockBehaviorType<AdjustableRedstoneBlockBehavior> ADJUSTABLE_REDSTONE_BLOCK = RegistryUtils.registerBlockBehavior(Key.of("gtemc:adjustable_redstone_block"), AdjustableRedstoneBlockBehavior.FACTORY);
    public static final BlockBehaviorType<PickaxeBlockBehavior> PICKAXE_BLOCK = RegistryUtils.registerBlockBehavior(Key.of("gtemc:pickaxe_block"), PickaxeBlockBehavior.FACTORY);
    public static final BlockBehaviorType<PlaceBlockBehavior> PLACE_BLOCK = RegistryUtils.registerBlockBehavior(Key.of("gtemc:place_block"), PlaceBlockBehavior.FACTORY);
    public static final BlockBehaviorType<SeatBlockBehavior> SEAT_BLOCK = RegistryUtils.registerBlockBehavior(Key.of("gtemc:seat_block"), SeatBlockBehavior.FACTORY);

    public static void register() {
    }
}
