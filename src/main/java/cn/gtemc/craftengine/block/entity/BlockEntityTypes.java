package cn.gtemc.craftengine.block.entity;

import cn.gtemc.craftengine.util.RegistryUtils;
import net.momirealms.craftengine.core.block.entity.BlockEntityType;

public final class BlockEntityTypes {
    private BlockEntityTypes() {}

    public static final BlockEntityType<SeatBlockEntity> SEAT = RegistryUtils.registerBlockEntity(BlockEntityTypeKeys.SEAT);
}
