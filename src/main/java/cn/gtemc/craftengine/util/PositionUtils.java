package cn.gtemc.craftengine.util;

import net.momirealms.craftengine.core.world.Vec3d;
import net.momirealms.craftengine.proxy.minecraft.core.BlockPosProxy;
import net.momirealms.craftengine.proxy.minecraft.world.phys.Vec3Proxy;

public final class PositionUtils {
    private PositionUtils() {}

    public static Object getCenterVec3(Object blockPos) {
        int x = BlockPosProxy.INSTANCE.getX(blockPos);
        int y = BlockPosProxy.INSTANCE.getY(blockPos);
        int z = BlockPosProxy.INSTANCE.getZ(blockPos);
        return Vec3Proxy.INSTANCE.newInstance(x + 0.5, y + 0.5, z + 0.5);
    }

    public static Vec3d toVec3d(Object blockPos) {
        int x = BlockPosProxy.INSTANCE.getX(blockPos);
        int y = BlockPosProxy.INSTANCE.getY(blockPos);
        int z = BlockPosProxy.INSTANCE.getZ(blockPos);
        return new Vec3d(x, y, z);
    }
}
