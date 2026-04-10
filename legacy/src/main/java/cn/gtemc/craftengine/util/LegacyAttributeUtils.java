package cn.gtemc.craftengine.util;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ArmorStand;

import java.util.Objects;

public final class LegacyAttributeUtils {
    private LegacyAttributeUtils() {}

    public static void setMaxHealth(ArmorStand entity) {
        Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(0.01);
    }
}
