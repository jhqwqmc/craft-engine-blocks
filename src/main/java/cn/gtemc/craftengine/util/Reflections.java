package cn.gtemc.craftengine.util;

import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.CoreReflections;
import net.momirealms.craftengine.bukkit.util.BukkitReflectionUtils;
import net.momirealms.craftengine.core.util.ReflectionUtils;
import net.momirealms.craftengine.core.util.VersionHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;


public class Reflections {

    public static void init() {
    }

    public static final Method method$Direction$orderedByNearest = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    CoreReflections.clazz$Direction, CoreReflections.clazz$Direction.arrayType(), CoreReflections.clazz$Entity
            )
    );

    public static final Class<?> clazz$TickPriority = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.ticks.TickListPriority",
                    "world.ticks.TickPriority"
            )
    );

    public static final Method method$TickPriority$values = requireNonNull(
            ReflectionUtils.getStaticMethod(clazz$TickPriority, clazz$TickPriority.arrayType())
    );

    public static final Object instance$TickPriority$EXTREMELY_HIGH;
    public static final Object instance$TickPriority$VERY_HIGH;
    public static final Object instance$TickPriority$HIGH;
    public static final Object instance$TickPriority$NORMAL;
    public static final Object instance$TickPriority$LOW;
    public static final Object instance$TickPriority$VERY_LOW;
    public static final Object instance$TickPriority$EXTREMELY_LOW;

    static {
        try {
            Object[] values = (Object[]) method$TickPriority$values.invoke(null);
            instance$TickPriority$EXTREMELY_HIGH = values[0];
            instance$TickPriority$VERY_HIGH = values[1];
            instance$TickPriority$HIGH = values[2];
            instance$TickPriority$NORMAL = values[3];
            instance$TickPriority$LOW = values[4];
            instance$TickPriority$VERY_LOW = values[5];
            instance$TickPriority$EXTREMELY_LOW = values[6];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static final Class<?> clazz$WorldlyContainerHolder = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.IInventoryHolder",
                    "world.WorldlyContainerHolder"
            )
    );

    public static final Class<?> clazz$ChestBlockEntity = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.entity.TileEntityChest",
                    "world.level.block.entity.ChestBlockEntity"
            )
    );

    public static final Class<?> clazz$ChestBlock = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.block.BlockChest",
                    "world.level.block.ChestBlock"
            )
    );

    public static final Class<?> clazz$ItemEntity = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.entity.item.EntityItem",
                    "world.entity.item.ItemEntity"
            )
    );

    public static final Method method$BlockItem$getBlock = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    CoreReflections.clazz$BlockItem, CoreReflections.clazz$Block
            )
    );

    public static final Constructor<?> constructor$BlockHitResult = requireNonNull(
            ReflectionUtils.getConstructor(
                    CoreReflections.clazz$BlockHitResult, CoreReflections.clazz$Vec3, CoreReflections.clazz$Direction, CoreReflections.clazz$BlockPos, boolean.class
            )
    );

    public static final Constructor<?> constructor$Vec3 = requireNonNull(
            ReflectionUtils.getConstructor(
                    CoreReflections.clazz$Vec3, double.class, double.class, double.class
            )
    );

    public static final Method method$AABB$ofSize = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    CoreReflections.clazz$AABB, CoreReflections.clazz$AABB, CoreReflections.clazz$Vec3, double.class, double.class, double.class
            )
    );

    public static final Class<?> clazz$InteractionResult = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.EnumInteractionResult",
                    "world.InteractionResult"
            )
    );

    public static final Class<?> clazz$BlockPlaceContext = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.item.context.BlockActionContext",
                    "world.item.context.BlockPlaceContext"
            )
    );

    public static final Method method$BlockItem$place = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    CoreReflections.clazz$BlockItem, clazz$InteractionResult, clazz$BlockPlaceContext
            )
    );

    public static final Method method$InteractionResult$consumesAction = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    clazz$InteractionResult, boolean.class, new String[]{ "consumesAction", "a" }
            )
    );

    public static final Method method$EntityType$getHeight = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    CoreReflections.clazz$EntityType, float.class, VersionHelper.isOrAbove1_20_5() ? new String[]{ "getHeight", "m" } : new String[]{ "getHeight", "l" }
            )
    );

    public static final Constructor<?> constructor$ItemEntity = requireNonNull(
            ReflectionUtils.getConstructor(
                    clazz$ItemEntity, CoreReflections.clazz$Level, double.class, double.class, double.class, CoreReflections.clazz$ItemStack
            )
    );

    public static final Method method$ItemEntity$setDefaultPickUpDelay = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    clazz$ItemEntity, void.class, VersionHelper.isOrAbove1_21_5()
                            ? new String[]{ "setDefaultPickUpDelay", "i" }
                            : VersionHelper.isOrAbove1_21_2()
                                ? new String[]{ "setDefaultPickUpDelay", "s" }
                                : VersionHelper.isOrAbove1_20_5()
                                    ? new String[]{ "setDefaultPickUpDelay", "v" }
                                    : VersionHelper.isOrAbove1_20_3()
                                        ? new String[]{ "setDefaultPickUpDelay", "u" }
                                        : VersionHelper.isOrAbove1_20_2()
                                            ? new String[]{ "setDefaultPickUpDelay", "t" }
                                            : new String[]{ "setDefaultPickUpDelay", "o" }
            )
    );

    public static final Method method$Container$setChanged = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    CoreReflections.clazz$Container, void.class, new String[]{ "setChanged", "e" }
            )
    );

    public static final Method method$Container$removeItem = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    CoreReflections.clazz$Container, CoreReflections.clazz$ItemStack, int.class, int.class
            )
    );

    public static final Method method$Container$setItem = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    CoreReflections.clazz$Container, void.class, int.class, CoreReflections.clazz$ItemStack
            )
    );

    public static final Method method$ItemStack$copy = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    CoreReflections.clazz$ItemStack, CoreReflections.clazz$ItemStack, VersionHelper.isOrAbove1_21_2()
                            ? new String[]{ "copy", "v" }
                            : VersionHelper.isOrAbove1_20_5()
                                ? new String[]{ "copy", "s" }
                                : new String[]{ "copy", "p" }
            )
    );

    public static final Method method$ItemEntity$getItem = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    clazz$ItemEntity, CoreReflections.clazz$ItemStack
            )
    );

    public static final Method method$ItemStack$copyWithCount = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    CoreReflections.clazz$ItemStack, CoreReflections.clazz$ItemStack, new String[]{"copyWithCount", "c"}, int.class
            )
    );

    public static final Method method$ItemStack$shrink = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    CoreReflections.clazz$ItemStack, void.class, new String[]{"shrink", "h"}, int.class
            )
    );

    public static final Method method$ItemStack$getCount = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    CoreReflections.clazz$ItemStack, int.class, VersionHelper.isOrAbove1_21_4()
                            ? new String[]{ "getCount", "M" }
                            : VersionHelper.isOrAbove1_21_2()
                                ? new String[]{ "getCount", "L" }
                                : VersionHelper.isOrAbove1_21()
                                    ? new String[]{ "getCount", "H" }
                                    : VersionHelper.isOrAbove1_20_5()
                                        ? new String[]{ "getCount", "I" }
                                        : new String[]{ "getCount", "L" }
            )
    );

    public static final Method method$Entity$discard = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    CoreReflections.clazz$Entity, void.class, VersionHelper.isOrAbove1_21_6()
                                ? new String[]{ "discard", "at" }
                                : VersionHelper.isOrAbove1_21_5()
                                    ? new String[]{ "discard", "aq" }
                                    : VersionHelper.isOrAbove1_21_2()
                                        ? new String[]{ "discard", "at" }
                                        : VersionHelper.isOrAbove1_21()
                                            ? new String[]{ "discard", "aq" }
                                            : VersionHelper.isOrAbove1_20_5()
                                                ? new String[]{ "discard", "ao" }
                                                : VersionHelper.isOrAbove1_20_3()
                                                    ? new String[]{ "discard", "am" }
                                                    : VersionHelper.isOrAbove1_20_2()
                                                        ? new String[]{ "discard", "ak" }
                                                        : new String[]{ "discard", "ai" }
            )
    );

    public static final Class<?> clazz$WorldlyContainer = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.IWorldInventory",
                    "world.WorldlyContainer"
            )
    );

    public static final Method method$WorldlyContainerHolder$getContainer = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    clazz$WorldlyContainerHolder, clazz$WorldlyContainer, CoreReflections.clazz$BlockState, CoreReflections.clazz$LevelAccessor, CoreReflections.clazz$BlockPos
            )
    );

    public static final Method method$BlockStateBase$hasBlockEntity = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    CoreReflections.clazz$BlockStateBase, boolean.class, VersionHelper.isOrAbove1_21_2() ? new String[] {"hasBlockEntity", "x"} : new String[] {"hasBlockEntity", "t"}
            )
    );

    public static final Method method$BlockGetter$getBlockEntity = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    CoreReflections.clazz$BlockGetter, CoreReflections.clazz$BlockEntity, new String[] {"getBlockEntity", "c_"}, CoreReflections.clazz$BlockPos
            )
    );

    public static final Method method$ChestBlock$getContainer = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    clazz$ChestBlock, CoreReflections.clazz$Container, clazz$ChestBlock, CoreReflections.clazz$BlockState, CoreReflections.clazz$Level, CoreReflections.clazz$BlockPos, boolean.class
            )
    );

    public static final Method method$Entity$isAlive = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    CoreReflections.clazz$Entity, boolean.class, VersionHelper.isOrAbove1_21_6()
                            ? new String[]{ "isAlive", "bO" }
                            : VersionHelper.isOrAbove1_21_5()
                                ? new String[]{ "isAlive", "bJ" }
                                : VersionHelper.isOrAbove1_21_2()
                                    ? new String[]{ "isAlive", "bL" }
                                    : VersionHelper.isOrAbove1_21()
                                        ? new String[]{ "isAlive", "bE" }
                                        : VersionHelper.isOrAbove1_20_5()
                                            ? new String[]{ "isAlive", "bD" }
                                            : VersionHelper.isOrAbove1_20_3()
                                                ? new String[]{ "isAlive", "bx" }
                                                : VersionHelper.isOrAbove1_20_2()
                                                    ? new String[]{ "isAlive", "bv" }
                                                    : new String[]{ "isAlive", "bs" }
            )
    );

    public static final Class<?> clazz$EntityGetter = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.level.IEntityAccess",
                    "world.level.EntityGetter"
            )
    );

    public static final Method method$EntityGetter$getEntitiesOfClass = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    clazz$EntityGetter, List.class, Class.class, CoreReflections.clazz$AABB, Predicate.class
            )
    );

    public static final Method method$EntityGetter$getEntities = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    clazz$EntityGetter, List.class, CoreReflections.clazz$Entity, CoreReflections.clazz$AABB, Predicate.class
            )
    );

    public static final Method method$Container$getContainerSize = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    CoreReflections.clazz$Container, int.class, new String[]{ "getContainerSize", "b" }
            )
    );

    public static final Method method$WorldlyContainer$getSlotsForFace = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    clazz$WorldlyContainer, int[].class, CoreReflections.clazz$Direction
            )
    );

    public static final Method method$BlockPlaceContext$getNearestLookingDirection = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    clazz$BlockPlaceContext, CoreReflections.clazz$Direction, new String[]{ "getNearestLookingDirection", "d" }
            )
    );

    public static final Method method$BlockPlaceContext$getNearestLookingVerticalDirection = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    clazz$BlockPlaceContext, CoreReflections.clazz$Direction, new String[]{ "getNearestLookingVerticalDirection", "e" }
            )
    );

    public static final Method method$BlockPlaceContext$getNearestLookingDirections = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    clazz$BlockPlaceContext, CoreReflections.clazz$Direction.arrayType(), new String[]{ "getNearestLookingDirections", "f" }
            )
    );

    public static final Class<?> clazz$UseOnContext = requireNonNull(
            BukkitReflectionUtils.findReobfOrMojmapClass(
                    "world.item.context.ItemActionContext",
                    "world.item.context.UseOnContext"
            )
    );

    public static final Method method$UseOnContext$getHitResult = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    clazz$UseOnContext, CoreReflections.clazz$BlockHitResult
            )
    );

    public static final Method method$BlockHitResult$getDirection = requireNonNull(
            ReflectionUtils.getDeclaredMethod(
                    CoreReflections.clazz$BlockHitResult, CoreReflections.clazz$Direction
            )
    );
}
