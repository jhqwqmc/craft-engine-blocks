package cn.gtemc.craftengine.util;

import net.momirealms.craftengine.core.world.BlockPos;
import net.momirealms.craftengine.core.world.Vec3d;
import net.momirealms.craftengine.proxy.minecraft.world.phys.Vec3Proxy;

public class PositionUtils {

    public static Vec3d getCenter(BlockPos blockPos) {
        return new Vec3d(blockPos.x() + 0.5, blockPos.y() + 0.5, blockPos.z() + 0.5);
    }

    public static Object toVec3(Vec3d vec3d) {
        return Vec3Proxy.INSTANCE.newInstance(vec3d.x(), vec3d.y(), vec3d.z());
    }
}
