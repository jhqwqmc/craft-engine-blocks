package cn.gtemc.craftengine.block;

import cn.gtemc.craftengine.block.behavior.*;
import cn.gtemc.craftengine.util.RegistryUtils;
import net.momirealms.craftengine.core.block.behavior.BlockBehaviorType;

public final class BlockBehaviors {
    private BlockBehaviors() {}

    public static final BlockBehaviorType ChunkLoaderBlock = RegistryUtils.registerBlockBehavior(ChunkLoaderBlockBehavior.ID, ChunkLoaderBlockBehavior.FACTORY);
    public static final BlockBehaviorType AdjustableRedstoneBlock = RegistryUtils.registerBlockBehavior(AdjustableRedstoneBlockBehavior.ID, AdjustableRedstoneBlockBehavior.FACTORY);
    public static final BlockBehaviorType PickaxeBlock = RegistryUtils.registerBlockBehavior(PickaxeBlockBehavior.ID, PickaxeBlockBehavior.FACTORY);
    public static final BlockBehaviorType PlaceBlock = RegistryUtils.registerBlockBehavior(PlaceBlockBehavior.ID, PlaceBlockBehavior.FACTORY);
    public static final BlockBehaviorType SeatBlock = RegistryUtils.registerBlockBehavior(SeatBlockBehavior.ID, SeatBlockBehavior.FACTORY);

    public static void register() {
    }
}
