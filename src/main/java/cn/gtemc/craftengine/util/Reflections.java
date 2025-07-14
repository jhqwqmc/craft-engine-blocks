package cn.gtemc.craftengine.util;

import net.momirealms.craftengine.bukkit.plugin.reflection.minecraft.CoreReflections;
import net.momirealms.craftengine.bukkit.util.BukkitReflectionUtils;
import net.momirealms.craftengine.core.util.ReflectionUtils;

import java.lang.reflect.Method;

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
}
