package cn.gtemc.craftengine.block.behavior;

import net.momirealms.craftengine.bukkit.block.BukkitBlockManager;
import net.momirealms.craftengine.bukkit.block.behavior.BukkitBlockBehavior;
import net.momirealms.craftengine.bukkit.nms.FastNMS;
import net.momirealms.craftengine.bukkit.util.BlockStateUtils;
import net.momirealms.craftengine.bukkit.util.LocationUtils;
import net.momirealms.craftengine.core.block.BlockBehavior;
import net.momirealms.craftengine.core.block.CustomBlock;
import net.momirealms.craftengine.core.block.ImmutableBlockState;
import net.momirealms.craftengine.core.block.UpdateOption;
import net.momirealms.craftengine.core.block.behavior.BlockBehaviorFactory;
import net.momirealms.craftengine.core.block.properties.IntegerProperty;
import net.momirealms.craftengine.core.entity.player.InteractionResult;
import net.momirealms.craftengine.core.item.context.UseOnContext;

import java.util.Map;
import java.util.concurrent.Callable;

public class AdjustableRedstoneBlockBehavior extends BukkitBlockBehavior {
    public static final Factory FACTORY = new Factory();
    private final IntegerProperty powerProperty;

    public AdjustableRedstoneBlockBehavior(CustomBlock customBlock, IntegerProperty power) {
        super(customBlock);
        this.powerProperty = power;
    }

    @Override
    public InteractionResult useWithoutItem(UseOnContext context, ImmutableBlockState state) {
        int power = state.get(this.powerProperty);
        if (context.isSecondaryUseActive()) {
            if (power - 1 < this.powerProperty.min) {
                power = this.powerProperty.max;
            } else {
                power--;
            }
        } else {
            if (power + 1 > this.powerProperty.max) {
                power = this.powerProperty.min;
            } else {
                power++;
            }
        }
        FastNMS.INSTANCE.method$LevelWriter$setBlock(
                context.getLevel().serverWorld(),
                LocationUtils.toBlockPos(context.getClickedPos()),
                state.with(this.powerProperty, power).customBlockState().literalObject(),
                UpdateOption.UPDATE_ALL.flags()
        );
        return InteractionResult.SUCCESS_AND_CANCEL;
    }

    @Override
    public boolean isSignalSource(Object thisBlock, Object[] args, Callable<Object> superMethod) {
        return true;
    }

    public int getSignal(Object thisBlock, Object[] args, Callable<Object> superMethod) {
        ImmutableBlockState state = BukkitBlockManager.instance().getImmutableBlockState(BlockStateUtils.blockStateToId(args[0]));
        if (state == null || state.isEmpty()) {
            return 0;
        }
        return state.get(this.powerProperty);
    }

    public int getDirectSignal(Object thisBlock, Object[] args, Callable<Object> superMethod) {
        ImmutableBlockState state = BukkitBlockManager.instance().getImmutableBlockState(BlockStateUtils.blockStateToId(args[0]));
        if (state == null || state.isEmpty()) {
            return 0;
        }
        return state.get(this.powerProperty);
    }

    public static class Factory implements BlockBehaviorFactory {

        @Override
        public BlockBehavior create(CustomBlock block, Map<String, Object> arguments) {
            IntegerProperty power = (IntegerProperty) block.getProperty("power");
            if (power == null) {
                throw new IllegalArgumentException("方块 '" + block.id() + "' 的 'gtemc:adjustable_redstone_block' 行为缺少必需的 'power' 属性");
            }
            return new AdjustableRedstoneBlockBehavior(block, power);
        }
    }
}
