package cn.gtemc.craftengine.block.behavior;

import cn.gtemc.craftengine.block.entity.BlockEntityTypes;
import cn.gtemc.craftengine.block.entity.SeatBlockEntity;
import net.momirealms.craftengine.bukkit.block.behavior.BukkitBlockBehavior;
import net.momirealms.craftengine.bukkit.plugin.user.BukkitServerPlayer;
import net.momirealms.craftengine.core.block.CustomBlock;
import net.momirealms.craftengine.core.block.ImmutableBlockState;
import net.momirealms.craftengine.core.block.behavior.BlockBehaviorFactory;
import net.momirealms.craftengine.core.block.behavior.EntityBlockBehavior;
import net.momirealms.craftengine.core.block.entity.BlockEntity;
import net.momirealms.craftengine.core.block.entity.BlockEntityType;
import net.momirealms.craftengine.core.entity.player.InteractionResult;
import net.momirealms.craftengine.core.plugin.config.ConfigConstants;
import net.momirealms.craftengine.core.plugin.config.ConfigSection;
import net.momirealms.craftengine.core.world.BlockPos;
import net.momirealms.craftengine.core.world.CEWorld;
import net.momirealms.craftengine.core.world.context.UseOnContext;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

@SuppressWarnings("UnstableApiUsage")
public class SeatBlockBehavior extends BukkitBlockBehavior implements EntityBlockBehavior {
    public static final BlockBehaviorFactory<SeatBlockBehavior> FACTORY = new Factory();
    private final Vector3f offset;
    private final float yaw;
    private final boolean limitPlayerRotation;

    public SeatBlockBehavior(CustomBlock customBlock, Vector3f offset, float yaw, boolean limitPlayerRotation) {
        super(customBlock);
        this.offset = offset;
        this.yaw = yaw;
        this.limitPlayerRotation = limitPlayerRotation;
    }

    @Override
    public InteractionResult useWithoutItem(UseOnContext context, ImmutableBlockState state) {
        BukkitServerPlayer player = (BukkitServerPlayer) context.getPlayer();
        if (player == null || player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        }
        player.swingHand(context.getHand());
        CEWorld world = context.getLevel().storageWorld();
        BlockEntity blockEntity = world.getBlockEntityAtIfLoaded(context.getClickedPos());
        if (!(blockEntity instanceof SeatBlockEntity seatBlockEntity)) {
            return InteractionResult.PASS;
        }
        seatBlockEntity.seat(player.platformPlayer());
        return InteractionResult.SUCCESS_AND_CANCEL;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityType<T> blockEntityType(ImmutableBlockState immutableBlockState) {
        return EntityBlockBehavior.blockEntityTypeHelper(BlockEntityTypes.SEAT);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, ImmutableBlockState state) {
        return new SeatBlockEntity(pos, state, this.offset, this.yaw, this.limitPlayerRotation);
    }

    private static class Factory implements BlockBehaviorFactory<SeatBlockBehavior> {
        private static final String[] LIMIT_PLAYER_ROTATION = new String[]{"limit_player_rotation", "limit-player-rotation"};

        @Override
        public SeatBlockBehavior create(CustomBlock block, ConfigSection section) {
            return new SeatBlockBehavior(
                    block,
                    section.getVector3f("offset", ConfigConstants.ZERO_VECTOR3),
                    section.getFloat("yaw"),
                    section.getBoolean(LIMIT_PLAYER_ROTATION, true)
            );
        }
    }
}
