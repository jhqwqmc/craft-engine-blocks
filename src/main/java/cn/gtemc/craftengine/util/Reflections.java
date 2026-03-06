package cn.gtemc.craftengine.util;

import net.momirealms.craftengine.libraries.reflection.clazz.SparrowClass;
import net.momirealms.craftengine.libraries.reflection.constructor.SConstructor5;
import net.momirealms.craftengine.libraries.reflection.constructor.matcher.ConstructorMatcher;
import net.momirealms.craftengine.libraries.reflection.method.*;
import net.momirealms.craftengine.libraries.reflection.method.matcher.MethodMatcher;
import net.momirealms.craftengine.proxy.minecraft.core.BlockPosProxy;
import net.momirealms.craftengine.proxy.minecraft.core.DirectionProxy;
import net.momirealms.craftengine.proxy.minecraft.world.entity.EntityProxy;
import net.momirealms.craftengine.proxy.minecraft.world.item.BlockItemProxy;
import net.momirealms.craftengine.proxy.minecraft.world.item.ItemStackProxy;
import net.momirealms.craftengine.proxy.minecraft.world.level.BlockGetterProxy;
import net.momirealms.craftengine.proxy.minecraft.world.level.LevelAccessorProxy;
import net.momirealms.craftengine.proxy.minecraft.world.level.LevelProxy;
import net.momirealms.craftengine.proxy.minecraft.world.level.block.state.BlockBehaviourProxy;
import net.momirealms.craftengine.proxy.minecraft.world.level.block.state.BlockStateProxy;
import net.momirealms.craftengine.proxy.minecraft.world.phys.BlockHitResultProxy;

import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;


public class Reflections {
    public static final Class<?> clazz$Container = requireNonNull(SparrowClass.find("net.minecraft.world.Container"));
    public static final Class<?> clazz$WorldlyContainerHolder = requireNonNull(SparrowClass.find("net.minecraft.world.WorldlyContainerHolder"));
    public static final Class<?> clazz$ChestBlockEntity = requireNonNull(SparrowClass.find("net.minecraft.world.level.block.entity.ChestBlockEntity"));
    public static final Class<?> clazz$ChestBlock = requireNonNull(SparrowClass.find("net.minecraft.world.level.block.ChestBlock"));
    public static final Class<?> clazz$ItemEntity = requireNonNull(SparrowClass.find("net.minecraft.world.entity.item.ItemEntity"));
    public static final Class<?> clazz$InteractionResult = requireNonNull(SparrowClass.find("net.minecraft.world.InteractionResult"));
    public static final Class<?> clazz$BlockPlaceContext = requireNonNull(SparrowClass.find("net.minecraft.world.item.context.BlockPlaceContext"));
    public static final Class<?> clazz$WorldlyContainer = requireNonNull(SparrowClass.find("net.minecraft.world.WorldlyContainer"));
    public static final Class<?> clazz$EntityGetter = requireNonNull(SparrowClass.find("net.minecraft.world.level.EntityGetter"));
    public static final Class<?> clazz$UseOnContext = requireNonNull(SparrowClass.find("net.minecraft.world.item.context.UseOnContext"));
    public static final Class<?> clazz$InteractionHand = requireNonNull(SparrowClass.find("net.minecraft.world.InteractionHand"));
    public static final Class<?> clazz$AABB = requireNonNull(SparrowClass.find("net.minecraft.world.phys.AABB"));
    public static final Class<?> clazz$Vec3 = requireNonNull(SparrowClass.find("net.minecraft.world.phys.Vec3"));
    public static final Class<?> clazz$EntityType = requireNonNull(SparrowClass.find("net.minecraft.world.entity.EntityType"));
    public static final SMethod1 method$Direction$orderedByNearest = SparrowClass.of(DirectionProxy.CLASS).getDeclaredSparrowMethod(
            MethodMatcher.named("orderedByNearest").and(MethodMatcher.takeArguments(EntityProxy.CLASS))
    ).asm$1();
    public static final SMethod4 method$AABB$ofSize = SparrowClass.of(clazz$AABB).getDeclaredSparrowMethod(
            MethodMatcher.named("ofSize").and(MethodMatcher.takeArguments(clazz$Vec3, double.class, double.class, double.class))
    ).asm$4();
    public static final SMethod1 method$BlockItem$place = SparrowClass.of(BlockItemProxy.CLASS).getDeclaredSparrowMethod(
            MethodMatcher.named("place").and(MethodMatcher.takeArguments(clazz$BlockPlaceContext))
    ).asm$1();
    public static final SMethod0 method$InteractionResult$consumesAction = SparrowClass.of(clazz$InteractionResult).getDeclaredSparrowMethod(
            MethodMatcher.named("consumesAction")
    ).asm$0();
    public static final SMethod0 method$EntityType$getHeight = SparrowClass.of(clazz$EntityType).getDeclaredSparrowMethod(
            MethodMatcher.named("getHeight")
    ).asm$0();
    public static final SConstructor5 constructor$ItemEntity = SparrowClass.of(clazz$ItemEntity).getSparrowConstructor(
            ConstructorMatcher.takeArguments(LevelProxy.CLASS, double.class, double.class, double.class, ItemStackProxy.CLASS)
    ).asm$5();
    public static final SMethod0 method$ItemEntity$setDefaultPickUpDelay = SparrowClass.of(clazz$ItemEntity).getDeclaredSparrowMethod(
            MethodMatcher.named("setDefaultPickUpDelay")
    ).asm$0();
    public static final SMethod0 method$Container$setChanged = SparrowClass.of(clazz$Container).getDeclaredSparrowMethod(
            MethodMatcher.named("setChanged")
    ).asm$0();
    public static final SMethod2 method$Container$removeItem = SparrowClass.of(clazz$Container).getDeclaredSparrowMethod(
            MethodMatcher.named("removeItem").and(MethodMatcher.takeArguments(int.class, int.class))
    ).asm$2();
    public static final SMethod2 method$Container$setItem = SparrowClass.of(clazz$Container).getDeclaredSparrowMethod(
            MethodMatcher.named("setItem").and(MethodMatcher.takeArguments(int.class, ItemStackProxy.CLASS))
    ).asm$2();
    public static final SMethod0 method$ItemStack$copy = SparrowClass.of(ItemStackProxy.CLASS).getDeclaredSparrowMethod(
            MethodMatcher.named("copy")
    ).asm$0();
    public static final SMethod0 method$ItemEntity$getItem = SparrowClass.of(clazz$ItemEntity).getDeclaredSparrowMethod(
            MethodMatcher.named("getItem")
    ).asm$0();
    public static final SMethod1 method$ItemStack$copyWithCount = SparrowClass.of(ItemStackProxy.CLASS).getDeclaredSparrowMethod(
            MethodMatcher.named("copyWithCount").and(MethodMatcher.takeArguments(int.class))
    ).asm$1();
    public static final SMethod1 method$ItemStack$shrink = SparrowClass.of(ItemStackProxy.CLASS).getDeclaredSparrowMethod(
            MethodMatcher.named("shrink").and(MethodMatcher.takeArguments(int.class))
    ).asm$1();
    public static final SMethod0 method$ItemStack$getCount = SparrowClass.of(ItemStackProxy.CLASS).getDeclaredSparrowMethod(
            MethodMatcher.named("getCount")
    ).asm$0();
    public static final SMethod0 method$Entity$discard = SparrowClass.of(EntityProxy.CLASS).getDeclaredSparrowMethod(
            MethodMatcher.named("discard")
    ).asm$0();
    public static final SMethod3 method$WorldlyContainerHolder$getContainer = SparrowClass.of(clazz$WorldlyContainerHolder).getDeclaredSparrowMethod(
            MethodMatcher.named("getContainer").and(MethodMatcher.takeArguments(BlockStateProxy.CLASS, LevelAccessorProxy.CLASS, BlockPosProxy.CLASS))
    ).asm$3();
    public static final SMethod0 method$BlockStateBase$hasBlockEntity = SparrowClass.of(BlockBehaviourProxy.BlockStateBaseProxy.CLASS).getDeclaredSparrowMethod(
            MethodMatcher.named("hasBlockEntity")
    ).asm$0();
    public static final SMethod1 method$BlockGetter$getBlockEntity = SparrowClass.of(BlockGetterProxy.CLASS).getDeclaredSparrowMethod(
            MethodMatcher.named("getBlockEntity").and(MethodMatcher.takeArguments(BlockPosProxy.CLASS))
    ).asm$1();
    public static final SMethod5 method$ChestBlock$getContainer = SparrowClass.of(clazz$ChestBlock).getDeclaredSparrowMethod(
            MethodMatcher.named("getContainer").and(MethodMatcher.takeArguments(clazz$ChestBlock, BlockStateProxy.CLASS, LevelProxy.CLASS, BlockPosProxy.CLASS, boolean.class))
    ).asm$5();
    public static final SMethod0 method$Entity$isAlive = SparrowClass.of(EntityProxy.CLASS).getDeclaredSparrowMethod(
            MethodMatcher.named("isAlive")
    ).asm$0();
    public static final SMethod3 method$EntityGetter$getEntities = SparrowClass.of(clazz$EntityGetter).getDeclaredSparrowMethod(
            MethodMatcher.named("getEntities").and(MethodMatcher.takeArguments(EntityProxy.CLASS, clazz$AABB, Predicate.class))
    ).asm$3();
    public static final SMethod1 method$WorldlyContainer$getSlotsForFace = SparrowClass.of(clazz$WorldlyContainer).getDeclaredSparrowMethod(
            MethodMatcher.named("getSlotsForFace").and(MethodMatcher.takeArguments(DirectionProxy.CLASS))
    ).asm$1();
    public static final SMethod0 method$UseOnContext$getHitResult = SparrowClass.of(clazz$UseOnContext).getDeclaredSparrowMethod(
            MethodMatcher.named("getHitResult")
    ).asm$0();
    public static final SMethod0 method$BlockHitResult$getDirection = SparrowClass.of(BlockHitResultProxy.CLASS).getDeclaredSparrowMethod(
            MethodMatcher.named("getDirection")
    ).asm$0();
    public static final SparrowMethod method$BlockPlaceContext$getNearestLookingDirection = SparrowClass.of(clazz$BlockPlaceContext).getDeclaredSparrowMethod(
            MethodMatcher.named("getNearestLookingDirection")
    );
    public static final SparrowMethod method$BlockPlaceContext$getNearestLookingVerticalDirection = SparrowClass.of(clazz$BlockPlaceContext).getDeclaredSparrowMethod(
            MethodMatcher.named("getNearestLookingVerticalDirection")
    );
    public static final SparrowMethod method$BlockPlaceContext$getNearestLookingDirections = SparrowClass.of(clazz$BlockPlaceContext).getDeclaredSparrowMethod(
            MethodMatcher.named("getNearestLookingDirections")
    );

    public static void init() {
    }
}
