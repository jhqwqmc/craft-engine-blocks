package cn.gtemc.craftengine.injector;

import cn.gtemc.craftengine.CraftEngineBlocks;
import cn.gtemc.craftengine.util.Reflections;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatchers;
import net.momirealms.craftengine.bukkit.nms.FastNMS;
import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.CoreReflections;
import net.momirealms.craftengine.core.util.ReflectionUtils;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;

public class PlaceBlockBlockPlaceContextGenerator {
    private static MethodHandle constructor$PlaceBlockBlockPlaceContext;

    public static void init() {
        ByteBuddy byteBuddy = new ByteBuddy(ClassFileVersion.JAVA_V17);
        DynamicType.Builder<?> builder = byteBuddy
                .subclass(Reflections.clazz$BlockPlaceContext)
                .name("cn.gtemc.craftengine.injector.PlaceBlockBlockPlaceContext")
                .method(ElementMatchers.is(Reflections.method$BlockPlaceContext$getNearestLookingDirection))
                .intercept(MethodDelegation.to(DirectionHandler.INSTANCE))
                .method(ElementMatchers.is(Reflections.method$BlockPlaceContext$getNearestLookingVerticalDirection))
                .intercept(MethodDelegation.to(VerticalDirectionHandler.INSTANCE))
                .method(ElementMatchers.is(Reflections.method$BlockPlaceContext$getNearestLookingDirections))
                .intercept(MethodDelegation.to(DirectionsHandler.INSTANCE));

        Class<?> clazz = builder.make()
                .load(PlaceBlockBlockPlaceContextGenerator.class.getClassLoader())
                .getLoaded();
        Constructor<?> constructor = Objects.requireNonNull(
                ReflectionUtils.getConstructor(
                        clazz, CoreReflections.clazz$Level, CoreReflections.clazz$Player, CoreReflections.clazz$InteractionHand, CoreReflections.clazz$ItemStack, CoreReflections.clazz$BlockHitResult
                )
        );
        try {
            constructor$PlaceBlockBlockPlaceContext = ReflectionUtils.unreflectConstructor(constructor);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public static class DirectionHandler {
        public static final DirectionHandler INSTANCE = new DirectionHandler();

        @RuntimeType
        public Object getNearestLookingDirection(@This Object context) {
            try {
                Object hitResult = Reflections.method$UseOnContext$getHitResult.invoke(context);
                return Reflections.method$BlockHitResult$getDirection.invoke(hitResult);
            } catch (Throwable e) {
                CraftEngineBlocks.instance().getLogger().log(Level.WARNING, "Failed to run getNearestLookingDirection", e);
                return CoreReflections.instance$Direction$EAST;
            }
        }
    }

    public static class VerticalDirectionHandler {
        public static final VerticalDirectionHandler INSTANCE = new VerticalDirectionHandler();

        @RuntimeType
        public Object getNearestLookingVerticalDirection(@This Object context) {
            try {
                Object hitResult = Reflections.method$UseOnContext$getHitResult.invoke(context);
                Object direction = Reflections.method$BlockHitResult$getDirection.invoke(hitResult);
                return direction == CoreReflections.instance$Direction$UP ? CoreReflections.instance$Direction$UP : CoreReflections.instance$Direction$DOWN;
            } catch (Throwable e) {
                CraftEngineBlocks.instance().getLogger().log(Level.WARNING, "Failed to run getNearestLookingVerticalDirection", e);
                return CoreReflections.instance$Direction$EAST;
            }
        }
    }

    public static class DirectionsHandler {
        public static final DirectionsHandler INSTANCE = new DirectionsHandler();

        @RuntimeType
        public Object[] getNearestLookingDirections(@This Object context) {
            try {
                Object hitResult = Reflections.method$UseOnContext$getHitResult.invoke(context);
                Object direction = Reflections.method$BlockHitResult$getDirection.invoke(hitResult);
                Object[] directions = Arrays.copyOf(CoreReflections.instance$Directions, CoreReflections.instance$Directions.length);
                directions[0] = direction;
                directions[directions.length - 1] = FastNMS.INSTANCE.method$Direction$getOpposite(direction);
                int i = 0;
                for (Object direction1 : CoreReflections.instance$Directions) {
                    if (direction1 != direction && direction1 != FastNMS.INSTANCE.method$Direction$getOpposite(direction)) {
                        directions[++i] = CoreReflections.clazz$Direction.cast(direction);
                    }
                }
                return (Object[]) CoreReflections.clazz$Direction.arrayType().cast(directions);
            } catch (Throwable e) {
                CraftEngineBlocks.instance().getLogger().log(Level.WARNING, "Failed to run getNearestLookingDirections", e);
                return new Object[]{CoreReflections.instance$Direction$EAST};
            }
        }
    }

    public static Object create(Object level, Object hand, Object itemStack, Object hitResult) {
        try {
            return constructor$PlaceBlockBlockPlaceContext.invoke(level, null, hand, itemStack, hitResult);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
