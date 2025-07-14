package cn.gtemc.craftengine.block.behavior;

import cn.gtemc.craftengine.CraftEngineBlocks;
import cn.gtemc.craftengine.injector.PlaceBlockBlockPlaceContextGenerator;
import cn.gtemc.craftengine.item.context.PlaceBlockBlockPlaceContext;
import cn.gtemc.craftengine.util.PositionUtils;
import cn.gtemc.craftengine.util.Reflections;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.momirealms.craftengine.bukkit.block.BukkitBlockManager;
import net.momirealms.craftengine.bukkit.item.BukkitItemManager;
import net.momirealms.craftengine.bukkit.item.behavior.BlockItemBehavior;
import net.momirealms.craftengine.bukkit.nms.FastNMS;
import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.CoreReflections;
import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.MEntityTypes;
import net.momirealms.craftengine.bukkit.util.BlockStateUtils;
import net.momirealms.craftengine.bukkit.util.DirectionUtils;
import net.momirealms.craftengine.bukkit.util.LocationUtils;
import net.momirealms.craftengine.bukkit.world.BukkitWorldManager;
import net.momirealms.craftengine.core.block.BlockBehavior;
import net.momirealms.craftengine.core.block.CustomBlock;
import net.momirealms.craftengine.core.block.ImmutableBlockState;
import net.momirealms.craftengine.core.block.behavior.BlockBehaviorFactory;
import net.momirealms.craftengine.core.block.properties.Property;
import net.momirealms.craftengine.core.entity.player.InteractionHand;
import net.momirealms.craftengine.core.entity.player.InteractionResult;
import net.momirealms.craftengine.core.item.CustomItem;
import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.item.behavior.ItemBehavior;
import net.momirealms.craftengine.core.util.Direction;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.util.MiscUtils;
import net.momirealms.craftengine.core.util.ResourceConfigUtils;
import net.momirealms.craftengine.core.world.BlockHitResult;
import net.momirealms.craftengine.core.world.BlockPos;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlaceBlockBehavior extends FacingTriggerableBlockBehavior {
    public static final Factory FACTORY = new Factory();

    public PlaceBlockBehavior(CustomBlock customBlock, Property<Direction> facing, Property<Boolean> triggered, Set<Key> blocks, boolean whitelistMode) {
        super(customBlock, facing, triggered, blocks, whitelistMode);
    }

    @Override
    protected Object getTickPriority() {
        return Reflections.instance$TickPriority$EXTREMELY_LOW;
    }

    @Override
    public void tick(Object thisBlock, Object[] args, Callable<Object> superMethod) {
        Object state = args[0];
        Object level = args[1];
        Object pos = args[2];
        tick(state, level, pos);
    }

    @Override
    public void tick(Object state, Object level, Object nmsBlockPos) {
        BlockPos pos = LocationUtils.fromBlockPos(nmsBlockPos);
        ImmutableBlockState blockState = BukkitBlockManager.instance().getImmutableBlockState(BlockStateUtils.blockStateToId(state));
        if (blockState == null || blockState.isEmpty()) return;
        Direction direction = blockState.get(this.facingProperty);
        Direction opposite = direction.opposite();
        BlockPos blockPos = pos.relative(opposite);
        BlockPos blockPos1 = pos.relative(direction);
        try {
            getItemAndDoThings(level, blockPos, opposite, itemStack -> {
                try {
                    if (FastNMS.INSTANCE.method$ItemStack$isEmpty(itemStack)) {
                        return false;
                    } else {
                        Object itemStack1 = FastNMS.INSTANCE.method$ItemStack$getItem(itemStack);
                        boolean flag = false;
                        if (CoreReflections.clazz$BlockItem.isInstance(itemStack1)) {
                            Object block = Reflections.method$BlockItem$getBlock.invoke(itemStack1);
                            if (blockCheckByBlockState(FastNMS.INSTANCE.method$Block$defaultState(block))) {
                                Object blockHitResult = Reflections.constructor$BlockHitResult.newInstance(
                                        PositionUtils.toVec3(PositionUtils.getCenter(blockPos1)),
                                        DirectionUtils.toNMSDirection(opposite),
                                        LocationUtils.toBlockPos(blockPos1),
                                        false
                                );
                                Object placeBlockBlockPlaceContext = PlaceBlockBlockPlaceContextGenerator.create(
                                        level, CoreReflections.instance$InteractionHand$MAIN_HAND, itemStack, blockHitResult
                                );
                                Object interactionResult = Reflections.method$BlockItem$place.invoke(itemStack1, placeBlockBlockPlaceContext);
                                flag = (boolean) Reflections.method$InteractionResult$consumesAction.invoke(interactionResult);
                            }
                        }
                        if (!flag) {
                            Item<ItemStack> item = BukkitItemManager.instance().wrap(FastNMS.INSTANCE.method$CraftItemStack$asCraftMirror(itemStack));
                            Optional<CustomItem<ItemStack>> optionalCustomItem = item.getCustomItem();
                            if (optionalCustomItem.isPresent()) {
                                CustomItem<ItemStack> customItem = optionalCustomItem.get();
                                for (ItemBehavior itemBehavior : customItem.behaviors()) {
                                    if (itemBehavior instanceof BlockItemBehavior blockItemBehavior) {
                                        if (!blockCheckByKey(blockItemBehavior.block())) continue;
                                        BlockHitResult hitResult = new BlockHitResult(
                                                LocationUtils.toVec3d(blockPos1),
                                                opposite,
                                                blockPos1,
                                                false
                                        );
                                        PlaceBlockBlockPlaceContext context = new PlaceBlockBlockPlaceContext(
                                                BukkitWorldManager.instance().wrap(FastNMS.INSTANCE.method$Level$getCraftWorld(level)),
                                                InteractionHand.MAIN_HAND,
                                                BukkitItemManager.instance().wrap(FastNMS.INSTANCE.method$CraftItemStack$asCraftMirror(itemStack)),
                                                hitResult
                                        );
                                        InteractionResult result = blockItemBehavior.place(context);
                                        if (result.success()) {
                                            return true;
                                        }
                                    }
                                }
                            }
                            double d = ((float) Reflections.method$EntityType$getHeight.invoke(MEntityTypes.ITEM)) / 2.0;
                            double d1 = blockPos1.x() + 0.5;
                            double d2 = blockPos1.y() + 0.5 - d;
                            double d3 = blockPos1.z() + 0.5;
                            Object itemEntity = Reflections.constructor$ItemEntity.newInstance(level, d1, d2, d3, itemStack);
                            Reflections.method$ItemEntity$setDefaultPickUpDelay.invoke(itemEntity);
                            FastNMS.INSTANCE.method$LevelWriter$addFreshEntity(level, itemEntity);
                        }
                        return true;
                    }
                } catch (ReflectiveOperationException e) {
                    CraftEngineBlocks.instance().getLogger().log(Level.WARNING, "Error while placing item", e);
                    return false;
                }
            });
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }

    }

    private static IntStream getSlots(Object container, Object direction) throws ReflectiveOperationException {
        if (Reflections.clazz$WorldlyContainer.isInstance(container)) {
            return IntStream.of((int[]) Reflections.method$WorldlyContainer$getSlotsForFace.invoke(container, direction));
        } else {
            return IntStream.range(0, (int) Reflections.method$Container$getContainerSize.invoke(container));
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    private static boolean getItemAndDoThings(Object level, BlockPos blockPos, Direction direction, Function<Object, Boolean> function) throws ReflectiveOperationException {
        for (Object container : getContainersAt(level, blockPos)) {
            boolean flag = getSlots(container, DirectionUtils.toNMSDirection(direction)).anyMatch(i -> {
                try {
                    Object itemStack = Reflections.method$Container$removeItem.invoke(container, i, 1);
                    if (!FastNMS.INSTANCE.method$ItemStack$isEmpty(itemStack)) {
                        boolean flag1 = function.apply(Reflections.method$ItemStack$copy.invoke(itemStack));
                        if (flag1) {
                            Reflections.method$Container$setChanged.invoke(container);
                        } else {
                            Reflections.method$Container$setItem.invoke(container, i, itemStack);
                        }

                        return true;
                    } else {
                        return false;
                    }
                } catch (ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            if (flag) {
                return true;
            }
        }
        Object itemAt = getItemAt(level, LocationUtils.toBlockPos(blockPos));
        if (itemAt != null) {
            Object item = Reflections.method$ItemEntity$getItem.invoke(itemAt);
            if (!FastNMS.INSTANCE.method$ItemStack$isEmpty(item)) {
                boolean flag = function.apply(Reflections.method$ItemStack$copyWithCount.invoke(item, 1));
                if (flag) {
                    Reflections.method$ItemStack$shrink.invoke(item, 1);
                    if (((int) Reflections.method$ItemStack$getCount.invoke(item)) <= 0) {
                        Reflections.method$Entity$discard.invoke(itemAt);
                    }
                }

                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static List<Object> getContainersAt(Object level, BlockPos blockPos) throws ReflectiveOperationException {
        Object nmsBlockPos = LocationUtils.toBlockPos(blockPos);
        Object blockState = FastNMS.INSTANCE.method$BlockGetter$getBlockState(level, nmsBlockPos);
        Object block = FastNMS.INSTANCE.method$BlockState$getBlock(blockState);
        if (Reflections.clazz$WorldlyContainerHolder.isInstance(block)) {
            Object container = Reflections.method$WorldlyContainerHolder$getContainer.invoke(block, blockState, level, nmsBlockPos);
            if (container != null) {
                return List.of(container);
            }
        } else if ((boolean) Reflections.method$BlockStateBase$hasBlockEntity.invoke(blockState)) {
            Object blockEntity = Reflections.method$BlockGetter$getBlockEntity.invoke(level, nmsBlockPos);
            if (CoreReflections.clazz$Container.isInstance(blockEntity)) {
                if (!(Reflections.clazz$ChestBlockEntity.isInstance(blockEntity)) || !(Reflections.clazz$ChestBlock.isInstance(block))) {
                    return List.of(blockEntity);
                }
                Object container = Reflections.method$ChestBlock$getContainer.invoke(null, block, blockState, level, nmsBlockPos, true);
                if (container != null) {
                    return List.of(container);
                }
            }
        }
        List<Object> list = new ArrayList<>();
        for (Object entity : (List<Object>) Reflections.method$EntityGetter$getEntities.invoke(
                level, null, blockAABB(nmsBlockPos),
                ((Predicate<Object>)entity -> {
                    try {
                        return CoreReflections.clazz$Container.isInstance(entity) && ((boolean) Reflections.method$Entity$isAlive.invoke(entity));
                    } catch (ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }
                })
        )) {
            if (CoreReflections.clazz$Container.isInstance(entity)) {
                list.add(entity);
            }
        }
        return list;
    }

    @Nullable
    @SuppressWarnings("unchecked")
    public static Object getItemAt(Object level, Object blockPos) throws ReflectiveOperationException {
        List<Object> entitiesOfClass = (List<Object>) Reflections.method$EntityGetter$getEntitiesOfClass.invoke(
                level, Reflections.clazz$ItemEntity, blockAABB(blockPos), (Predicate<Object>) e -> {
                    try {
                        return (boolean) Reflections.method$Entity$isAlive.invoke(e);
                    } catch (ReflectiveOperationException ex) {
                        throw new RuntimeException(ex);
                    }
                }
        );
        return entitiesOfClass.isEmpty() ? null : entitiesOfClass.getFirst();
    }

    private static Object blockAABB(Object blockPos) {
        try {
            return Reflections.method$AABB$ofSize.invoke(null, PositionUtils.toVec3(PositionUtils.getCenter(LocationUtils.fromBlockPos(blockPos))), 0.9999999, 0.9999999, 0.9999999);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Factory implements BlockBehaviorFactory {

        @Override
        @SuppressWarnings({"unchecked", "all"})
        public BlockBehavior create(CustomBlock block, Map<String, Object> arguments) {
            Property<Direction> facing = (Property<Direction>) ResourceConfigUtils.requireNonNullOrThrow(block.getProperty("facing"), "warning.config.block.behavior.place_block.missing_facing");
            Property<Boolean> triggered = (Property<Boolean>) ResourceConfigUtils.requireNonNullOrThrow(block.getProperty("triggered"), "warning.config.block.behavior.place_block.missing_triggered");
            boolean whitelistMode = (boolean) arguments.getOrDefault("whitelist", false);
            Set<Key> blocks = MiscUtils.getAsStringList(arguments.get("blocks")).stream().map(Key::of).collect(Collectors.toCollection(ObjectOpenHashSet::new));
            if (blocks.isEmpty() && !whitelistMode) {
                blocks = FacingTriggerableBlockBehavior.DEFAULT_BLACKLIST_BLOCKS;
            }
            return new PlaceBlockBehavior(block, facing, triggered, blocks, whitelistMode);
        }
    }
}
