package cn.gtemc.craftengine.block.behavior;

import cn.gtemc.craftengine.injector.PlaceBlockBlockPlaceContextGenerator;
import cn.gtemc.craftengine.item.context.PlaceBlockBlockPlaceContext;
import cn.gtemc.craftengine.util.PositionUtils;
import cn.gtemc.craftengine.util.Reflections;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.momirealms.craftengine.bukkit.block.BukkitBlockManager;
import net.momirealms.craftengine.bukkit.item.BukkitItemManager;
import net.momirealms.craftengine.bukkit.item.behavior.BlockItemBehavior;
import net.momirealms.craftengine.bukkit.util.BlockStateUtils;
import net.momirealms.craftengine.bukkit.util.DirectionUtils;
import net.momirealms.craftengine.bukkit.util.LocationUtils;
import net.momirealms.craftengine.bukkit.world.BukkitWorldManager;
import net.momirealms.craftengine.core.block.CustomBlock;
import net.momirealms.craftengine.core.block.ImmutableBlockState;
import net.momirealms.craftengine.core.block.behavior.BlockBehaviorFactory;
import net.momirealms.craftengine.core.block.properties.Property;
import net.momirealms.craftengine.core.entity.player.InteractionHand;
import net.momirealms.craftengine.core.entity.player.InteractionResult;
import net.momirealms.craftengine.core.item.CustomItem;
import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.item.behavior.ItemBehavior;
import net.momirealms.craftengine.core.plugin.config.ConfigSection;
import net.momirealms.craftengine.core.plugin.config.ConfigValue;
import net.momirealms.craftengine.core.util.Direction;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.world.BlockHitResult;
import net.momirealms.craftengine.core.world.BlockPos;
import net.momirealms.craftengine.proxy.bukkit.craftbukkit.inventory.CraftItemStackProxy;
import net.momirealms.craftengine.proxy.minecraft.world.ContainerProxy;
import net.momirealms.craftengine.proxy.minecraft.world.InteractionHandProxy;
import net.momirealms.craftengine.proxy.minecraft.world.WorldlyContainerHolderProxy;
import net.momirealms.craftengine.proxy.minecraft.world.entity.EntityTypeProxy;
import net.momirealms.craftengine.proxy.minecraft.world.item.BlockItemProxy;
import net.momirealms.craftengine.proxy.minecraft.world.item.ItemStackProxy;
import net.momirealms.craftengine.proxy.minecraft.world.level.BlockGetterProxy;
import net.momirealms.craftengine.proxy.minecraft.world.level.EntityGetterProxy;
import net.momirealms.craftengine.proxy.minecraft.world.level.LevelProxy;
import net.momirealms.craftengine.proxy.minecraft.world.level.LevelWriterProxy;
import net.momirealms.craftengine.proxy.minecraft.world.level.block.BlockProxy;
import net.momirealms.craftengine.proxy.minecraft.world.level.block.state.BlockStateProxy;
import net.momirealms.craftengine.proxy.minecraft.world.phys.BlockHitResultProxy;
import net.momirealms.craftengine.proxy.minecraft.world.ticks.TickPriorityProxy;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlaceBlockBehavior extends FacingTriggerableBlockBehavior {
    public static final BlockBehaviorFactory<PlaceBlockBehavior> FACTORY = new Factory();

    public PlaceBlockBehavior(CustomBlock customBlock, Property<Direction> facing, Property<Boolean> triggered, Set<Key> blocks, boolean whitelistMode) {
        super(customBlock, facing, triggered, blocks, whitelistMode);
    }

    private static IntStream getSlots(Object container, Object direction) {
        if (Reflections.clazz$WorldlyContainer.isInstance(container)) {
            return IntStream.of((int[]) Reflections.method$WorldlyContainer$getSlotsForFace.invoke(container, direction));
        } else {
            return IntStream.range(0, ContainerProxy.INSTANCE.getContainerSize(container));
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    private static boolean getItemAndDoThings(Object level, BlockPos blockPos, Direction direction, Function<Object, Boolean> function) {
        for (Object container : getContainersAt(level, blockPos)) {
            boolean flag = getSlots(container, DirectionUtils.toNMSDirection(direction)).anyMatch(i -> {
                Object itemStack = Reflections.method$Container$removeItem.invoke(container, i, 1);
                if (!ItemStackProxy.INSTANCE.isEmpty(itemStack)) {
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
            });
            if (flag) {
                return true;
            }
        }
        Object itemAt = getItemAt(level, LocationUtils.toBlockPos(blockPos));
        if (itemAt != null) {
            Object item = Reflections.method$ItemEntity$getItem.invoke(itemAt);
            if (!ItemStackProxy.INSTANCE.isEmpty(item)) {
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
    public static List<Object> getContainersAt(Object level, BlockPos blockPos) {
        Object nmsBlockPos = LocationUtils.toBlockPos(blockPos);
        Object blockState = BlockGetterProxy.INSTANCE.getBlockState(level, nmsBlockPos);
        Object block = BlockStateProxy.INSTANCE.getBlock(blockState);
        if (WorldlyContainerHolderProxy.CLASS.isInstance(block)) {
            Object container = Reflections.method$WorldlyContainerHolder$getContainer.invoke(block, blockState, level, nmsBlockPos);
            if (container != null) {
                return List.of(container);
            }
        } else if ((boolean) Reflections.method$BlockStateBase$hasBlockEntity.invoke(blockState)) {
            Object blockEntity = Reflections.method$BlockGetter$getBlockEntity.invoke(level, nmsBlockPos);
            if (Reflections.clazz$Container.isInstance(blockEntity)) {
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
                (Predicate<?>) entity -> Reflections.clazz$Container.isInstance(entity) && (boolean) Reflections.method$Entity$isAlive.invoke(entity)
        )) {
            if (Reflections.clazz$Container.isInstance(entity)) {
                list.add(entity);
            }
        }
        return list;
    }

    @Nullable
    public static Object getItemAt(Object level, Object blockPos) {
        List<Object> entitiesOfClass = EntityGetterProxy.INSTANCE.getEntitiesOfClass(
                level, Reflections.clazz$ItemEntity, blockAABB(blockPos), e -> (boolean) Reflections.method$Entity$isAlive.invoke(e)
        );
        return entitiesOfClass.isEmpty() ? null : entitiesOfClass.getFirst();
    }

    private static Object blockAABB(Object blockPos) {
        return Reflections.method$AABB$ofSize.invoke(null, PositionUtils.toVec3(PositionUtils.getCenter(LocationUtils.fromBlockPos(blockPos))), 0.9999999, 0.9999999, 0.9999999);
    }

    @Override
    protected Object getTickPriority() {
        return TickPriorityProxy.EXTREMELY_LOW;
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
        getItemAndDoThings(level, blockPos, opposite, itemStack -> {
            if (ItemStackProxy.INSTANCE.isEmpty(itemStack)) return false;
            Object itemStack1 = ItemStackProxy.INSTANCE.getItem(itemStack);
            boolean flag = false;
            if (BlockItemProxy.CLASS.isInstance(itemStack1)) {
                Object block = BlockItemProxy.INSTANCE.getBlock(itemStack1);
                if (blockCheckByBlockState(BlockProxy.INSTANCE.getDefaultBlockState(block))) {
                    Object blockHitResult = BlockHitResultProxy.INSTANCE.newInstance(
                            PositionUtils.toVec3(PositionUtils.getCenter(blockPos1)),
                            DirectionUtils.toNMSDirection(opposite),
                            LocationUtils.toBlockPos(blockPos1),
                            false
                    );
                    Object placeBlockBlockPlaceContext = PlaceBlockBlockPlaceContextGenerator.create(
                            level, InteractionHandProxy.MAIN_HAND, itemStack, blockHitResult
                    );
                    Object interactionResult = Reflections.method$BlockItem$place.invoke(itemStack1, placeBlockBlockPlaceContext);
                    flag = (boolean) Reflections.method$InteractionResult$consumesAction.invoke(interactionResult);
                }
            }
            if (!flag) {
                Item<ItemStack> item = BukkitItemManager.instance().wrap(CraftItemStackProxy.INSTANCE.asCraftMirror(itemStack));
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
                                    BukkitWorldManager.instance().wrap(LevelProxy.INSTANCE.getWorld(level)),
                                    InteractionHand.MAIN_HAND,
                                    BukkitItemManager.instance().wrap(CraftItemStackProxy.INSTANCE.asCraftMirror(itemStack)),
                                    hitResult
                            );
                            InteractionResult result = blockItemBehavior.place(context);
                            if (result.success()) {
                                return true;
                            }
                        }
                    }
                }
                double d = ((float) Reflections.method$EntityType$getHeight.invoke(EntityTypeProxy.ITEM)) / 2.0;
                double d1 = blockPos1.x() + 0.5;
                double d2 = blockPos1.y() + 0.5 - d;
                double d3 = blockPos1.z() + 0.5;
                Object itemEntity = Reflections.constructor$ItemEntity.newInstance(level, d1, d2, d3, itemStack);
                Reflections.method$ItemEntity$setDefaultPickUpDelay.invoke(itemEntity);
                LevelWriterProxy.INSTANCE.addFreshEntity(level, itemEntity, null);
            }
            return true;
        });

    }

    private static class Factory implements BlockBehaviorFactory<PlaceBlockBehavior> {

        @Override
        public PlaceBlockBehavior create(CustomBlock block, ConfigSection section) {
            boolean whitelistMode = section.getBoolean("whitelist");
            Set<Key> blocks = section.getList("blocks", ConfigValue::getAsIdentifier).stream().collect(Collectors.toCollection(ObjectOpenHashSet::new));
            if (blocks.isEmpty() && !whitelistMode) {
                blocks = FacingTriggerableBlockBehavior.DEFAULT_BLACKLIST_BLOCKS;
            }
            return new PlaceBlockBehavior(
                    block,
                    BlockBehaviorFactory.getProperty(section.path(), block, "facing", Direction.class),
                    BlockBehaviorFactory.getProperty(section.path(), block, "triggered", Boolean.class),
                    blocks,
                    whitelistMode
            );
        }
    }
}
