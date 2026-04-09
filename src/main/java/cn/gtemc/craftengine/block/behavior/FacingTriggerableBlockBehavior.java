package cn.gtemc.craftengine.block.behavior;

import cn.gtemc.craftengine.item.context.PlaceBlockBlockPlaceContext;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.momirealms.craftengine.bukkit.block.behavior.BukkitBlockBehavior;
import net.momirealms.craftengine.bukkit.util.BlockStateUtils;
import net.momirealms.craftengine.bukkit.util.DirectionUtils;
import net.momirealms.craftengine.core.block.BlockDefinition;
import net.momirealms.craftengine.core.block.ImmutableBlockState;
import net.momirealms.craftengine.core.block.UpdateFlags;
import net.momirealms.craftengine.core.block.properties.Property;
import net.momirealms.craftengine.core.entity.player.Player;
import net.momirealms.craftengine.core.util.Direction;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.world.context.BlockPlaceContext;
import net.momirealms.craftengine.proxy.minecraft.core.DirectionProxy;
import net.momirealms.craftengine.proxy.minecraft.world.level.LevelWriterProxy;
import net.momirealms.craftengine.proxy.minecraft.world.level.ScheduledTickAccessProxy;
import net.momirealms.craftengine.proxy.minecraft.world.level.SignalGetterProxy;
import net.momirealms.craftengine.proxy.minecraft.world.level.block.state.BlockBehaviourProxy;

import java.util.Set;

public abstract class FacingTriggerableBlockBehavior extends BukkitBlockBehavior {
    protected static final Set<Key> DEFAULT_BLACKLIST_BLOCKS = ObjectOpenHashSet.of(
            Key.of("minecraft:bedrock"),
            Key.of("minecraft:end_portal_frame"),
            Key.of("minecraft:end_portal"),
            Key.of("minecraft:nether_portal"),
            Key.of("minecraft:barrier"),
            Key.of("minecraft:command_block"),
            Key.of("minecraft:chain_command_block"),
            Key.of("minecraft:repeating_command_block"),
            Key.of("minecraft:structure_block"),
            Key.of("minecraft:end_gateway"),
            Key.of("minecraft:jigsaw"),
            Key.of("minecraft:structure_void"),
            Key.of("minecraft:test_instance_block"),
            Key.of("minecraft:moving_piston"),
            Key.of("minecraft:test_block"),
            Key.of("minecraft:light")
    );
    protected final Property<Direction> facingProperty;
    protected final Property<Boolean> triggeredProperty;
    protected final Set<Key> blocks;
    protected final boolean whitelistMode;

    public FacingTriggerableBlockBehavior(BlockDefinition blockDefinition, Property<Direction> facing, Property<Boolean> triggered, Set<Key> blocks, boolean whitelistMode) {
        super(blockDefinition);
        this.facingProperty = facing;
        this.triggeredProperty = triggered;
        this.blocks = blocks;
        this.whitelistMode = whitelistMode;
    }

    @Override
    public void neighborChanged(Object thisBlock, Object[] args) {
        Object state = args[0];
        Object level = args[1];
        Object pos = args[2];
        boolean hasNeighborSignal = SignalGetterProxy.INSTANCE.hasNeighborSignal(level, pos);
        ImmutableBlockState blockState = BlockStateUtils.getOptionalCustomBlockState(state).orElse(null);
        if (blockState == null || blockState.isEmpty()) return;
        boolean triggeredValue = blockState.get(this.triggeredProperty);
        if (hasNeighborSignal && !triggeredValue) {
            Object tickState = blockState.with(this.triggeredProperty, true).customBlockState().minecraftState();
            LevelWriterProxy.INSTANCE.setBlock(level, pos, tickState, UpdateFlags.UPDATE_CLIENTS);
            ScheduledTickAccessProxy.INSTANCE.scheduleTick$0(level, pos, BlockStateUtils.getBlockOwner(tickState), 1, this.getTickPriority());
        } else if (!hasNeighborSignal && triggeredValue) {
            LevelWriterProxy.INSTANCE.setBlock(level, pos, blockState.with(this.triggeredProperty, false).customBlockState().minecraftState(), UpdateFlags.UPDATE_CLIENTS);
        }
    }

    @Override
    public ImmutableBlockState updateStateForPlacement(BlockPlaceContext context, ImmutableBlockState state) {
        if (context instanceof PlaceBlockBlockPlaceContext placeContext) {
            return state.owner().value().defaultState().with(this.facingProperty, placeContext.getNearestLookingDirection().opposite());
        }
        Player player = context.getPlayer();
        if (player == null) {
            return null;
        }
        Direction direction = DirectionUtils.fromNMSDirection(DirectionProxy.INSTANCE.getOpposite(orderedByNearest(player.serverPlayer())[0]));
        return state.owner().value().defaultState().with(this.facingProperty, direction);
    }

    private Object[] orderedByNearest(Object entity) {
        return DirectionProxy.INSTANCE.orderedByNearest(entity);
    }

    protected boolean blockCheckByBlockState(Object blockState) {
        if (this.blocks.isEmpty()) return !this.whitelistMode;
        if (blockState == null || BlockBehaviourProxy.BlockStateBaseProxy.INSTANCE.isAir(blockState)) {
            return false;
        }
        Key blockId = BlockStateUtils.getOptionalCustomBlockState(blockState)
                .filter(state -> !state.isEmpty())
                .map(state -> state.owner().value().id())
                .orElseGet(() -> BlockStateUtils.getBlockOwnerIdFromState(blockState));
        return blockCheckByKey(blockId);
    }

    protected boolean blockCheckByKey(Key blockId) {
        return this.blocks.contains(blockId) == this.whitelistMode;
    }

    protected abstract Object getTickPriority();
}
