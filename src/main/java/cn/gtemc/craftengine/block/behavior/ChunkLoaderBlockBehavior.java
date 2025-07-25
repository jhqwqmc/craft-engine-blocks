package cn.gtemc.craftengine.block.behavior;

import net.momirealms.craftengine.bukkit.block.behavior.BukkitBlockBehavior;
import net.momirealms.craftengine.bukkit.nms.FastNMS;
import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.CoreReflections;
import net.momirealms.craftengine.core.block.BlockBehavior;
import net.momirealms.craftengine.core.block.CustomBlock;
import net.momirealms.craftengine.core.block.ImmutableBlockState;
import net.momirealms.craftengine.core.block.behavior.BlockBehaviorFactory;
import net.momirealms.craftengine.core.item.context.BlockPlaceContext;

import java.util.Map;
import java.util.concurrent.Callable;

public class ChunkLoaderBlockBehavior extends BukkitBlockBehavior {
    public static final Factory FACTORY = new Factory();

    public ChunkLoaderBlockBehavior(CustomBlock customBlock) {
        super(customBlock);
    }

    @Override
    public void setPlacedBy(BlockPlaceContext context, ImmutableBlockState state) {
        Object serverLevel = context.getLevel().serverWorld();
        int blockX = context.getClickedPos().x();
        int blockZ = context.getClickedPos().z();
        updateChunkForced(serverLevel, blockX, blockZ, true);
    }

    @Override
    public Object playerWillDestroy(Object thisBlock, Object[] args, Callable<Object> superMethod) throws Exception {
        Object serverLevel = args[0];
        int blockX = FastNMS.INSTANCE.field$Vec3i$x(args[1]);
        int blockZ = FastNMS.INSTANCE.field$Vec3i$z(args[1]);
        updateChunkForced(serverLevel, blockX, blockZ, false);
        return super.playerWillDestroy(thisBlock, args, superMethod);
    }

    private static void updateChunkForced(Object serverLevel, int blockX, int blockZ, boolean add) {
        if (!CoreReflections.clazz$ServerLevel.isInstance(serverLevel)) return;
        FastNMS.INSTANCE.method$ServerLevel$setChunkForced(serverLevel, blockX >> 4, blockZ >> 4, add);
    }

    public static class Factory implements BlockBehaviorFactory {

        @Override
        public BlockBehavior create(CustomBlock block, Map<String, Object> arguments) {
            return new ChunkLoaderBlockBehavior(block);
        }
    }
}
