package cn.gtemc.craftengine.injector;

import cn.gtemc.craftengine.util.Reflections;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;
import net.momirealms.craftengine.libraries.reflection.clazz.SparrowClass;
import net.momirealms.craftengine.libraries.reflection.constructor.SConstructor5;
import net.momirealms.craftengine.libraries.reflection.constructor.matcher.ConstructorMatcher;
import net.momirealms.craftengine.proxy.minecraft.core.DirectionProxy;
import net.momirealms.craftengine.proxy.minecraft.world.entity.player.PlayerProxy;
import net.momirealms.craftengine.proxy.minecraft.world.item.ItemStackProxy;
import net.momirealms.craftengine.proxy.minecraft.world.level.LevelProxy;
import net.momirealms.craftengine.proxy.minecraft.world.phys.BlockHitResultProxy;

import java.lang.reflect.Array;

public class PlaceBlockBlockPlaceContextGenerator {
    private static SConstructor5 constructor$PlaceBlockBlockPlaceContext;

    public static void init() {
        ByteBuddy byteBuddy = new ByteBuddy(ClassFileVersion.JAVA_V17);
        DynamicType.Builder<?> builder = byteBuddy
                .subclass(Reflections.clazz$BlockPlaceContext)
                .name("cn.gtemc.craftengine.injector.PlaceBlockBlockPlaceContext")
                .method(ElementMatchers.is(Reflections.method$BlockPlaceContext$getNearestLookingDirection.method))
                .intercept(MethodDelegation.to(DirectionHandler.INSTANCE))
                .method(ElementMatchers.is(Reflections.method$BlockPlaceContext$getNearestLookingVerticalDirection.method))
                .intercept(MethodDelegation.to(VerticalDirectionHandler.INSTANCE))
                .method(ElementMatchers.is(Reflections.method$BlockPlaceContext$getNearestLookingDirections.method))
                .intercept(MethodDelegation.to(DirectionsHandler.INSTANCE));

        SparrowClass<?> clazz = SparrowClass.of(builder.make()
                .load(PlaceBlockBlockPlaceContextGenerator.class.getClassLoader())
                .getLoaded());
        constructor$PlaceBlockBlockPlaceContext = clazz.getSparrowConstructor(ConstructorMatcher.takeArguments(
                LevelProxy.CLASS, PlayerProxy.CLASS, Reflections.clazz$InteractionHand, ItemStackProxy.CLASS, BlockHitResultProxy.CLASS
        )).asm$5();
    }

    public static class DirectionHandler {
        public static final DirectionHandler INSTANCE = new DirectionHandler();

        @RuntimeType
        public Object getNearestLookingDirection(@This Object context) {
            Object hitResult = Reflections.method$UseOnContext$getHitResult.invoke(context);
            return BlockHitResultProxy.INSTANCE.getDirection(hitResult);
        }
    }

    public static class VerticalDirectionHandler {
        public static final VerticalDirectionHandler INSTANCE = new VerticalDirectionHandler();

        @RuntimeType
        public Object getNearestLookingVerticalDirection(@This Object context) {
            Object hitResult = Reflections.method$UseOnContext$getHitResult.invoke(context);
            Object direction = Reflections.method$BlockHitResult$getDirection.invoke(hitResult);
            return direction == DirectionProxy.UP ? DirectionProxy.UP : DirectionProxy.DOWN;
        }
    }

    public static class DirectionsHandler {
        public static final DirectionsHandler INSTANCE = new DirectionsHandler();

        @RuntimeType
        public Object[] getNearestLookingDirections(@This Object context) {
            Object hitResult = Reflections.method$UseOnContext$getHitResult.invoke(context);
            Object direction = Reflections.method$BlockHitResult$getDirection.invoke(hitResult);
            Object directions = Array.newInstance(DirectionProxy.CLASS, DirectionProxy.VALUES.length);
            Array.set(directions, 0, DirectionProxy.CLASS.cast(direction));
            Array.set(directions, DirectionProxy.VALUES.length - 1, DirectionProxy.CLASS.cast(DirectionProxy.INSTANCE.getOpposite(direction)));
            int i = 0;
            for (Object direction1 : DirectionProxy.VALUES) {
                if (direction1 != direction && direction1 != DirectionProxy.INSTANCE.getOpposite(direction)) {
                    Array.set(directions, ++i, DirectionProxy.CLASS.cast(direction));
                }
            }
            return (Object[]) DirectionProxy.CLASS.arrayType().cast(directions);
        }
    }

    public static Object create(Object level, Object hand, Object itemStack, Object hitResult) {
        return constructor$PlaceBlockBlockPlaceContext.newInstance(level, null, hand, itemStack, hitResult);
    }
}
