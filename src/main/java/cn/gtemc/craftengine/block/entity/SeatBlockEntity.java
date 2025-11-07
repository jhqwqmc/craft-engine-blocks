package cn.gtemc.craftengine.block.entity;

import cn.gtemc.craftengine.util.LegacyAttributeUtils;
import net.momirealms.craftengine.bukkit.util.EntityUtils;
import net.momirealms.craftengine.core.block.ImmutableBlockState;
import net.momirealms.craftengine.core.block.entity.BlockEntity;
import net.momirealms.craftengine.core.block.properties.IntegerProperty;
import net.momirealms.craftengine.core.block.properties.Property;
import net.momirealms.craftengine.core.util.HorizontalDirection;
import net.momirealms.craftengine.core.util.QuaternionUtils;
import net.momirealms.craftengine.core.util.VersionHelper;
import net.momirealms.craftengine.core.world.BlockPos;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class SeatBlockEntity extends BlockEntity {
    public static final NamespacedKey SEAT_KEY = new NamespacedKey("gtemc", "seat");
    @Nullable
    private WeakReference<Entity> seatEntity;
    private final Vector3f offset;
    private final float yaw;
    private final boolean limitPlayerRotation;

    public SeatBlockEntity(BlockPos pos, ImmutableBlockState blockState, Vector3f offset, float yaw, boolean limitPlayerRotation) {
        super(BlockEntityTypes.SEAT, pos, blockState);
        this.offset = offset;
        this.yaw = yaw;
        this.limitPlayerRotation = limitPlayerRotation;
    }

    @Override
    public void preRemove() {
        Entity entity = seatEntity();
        if (entity != null) entity.remove();
    }

    public void seat(@NotNull Player player) {
        if (isOccupied()) return;
        this.spawnSeat(player);
    }

    @Nullable
    private Entity seatEntity() {
        return this.seatEntity == null ? null : this.seatEntity.get();
    }

    private boolean isOccupied() {
        Entity entity = seatEntity();
        return entity != null && entity.isValid() && !entity.getPassengers().isEmpty();
    }

    public void destroy() {
        Entity entity = seatEntity();
        if (entity != null) {
            entity.remove();
            this.seatEntity = null;
        }
    }

    private void spawnSeat(Player player) {
        destroy();
        Location location = calculateSeatLocation(new Location(player.getWorld(), super.pos.x() + 0.5, super.pos.y(), super.pos.z() + 0.5, 0, 0));
        Entity seatEntity = limitPlayerRotation ?
                EntityUtils.spawnEntity(player.getWorld(),
                        VersionHelper.isOrAbove1_20_2() ? location.subtract(0, 0.9875, 0) : location.subtract(0, 0.990625, 0),
                        EntityType.ARMOR_STAND,
                        entity -> {
                            ArmorStand armorStand = (ArmorStand) entity;
                            if (VersionHelper.isOrAbove1_21_3()) {
                                Objects.requireNonNull(armorStand.getAttribute(Attribute.MAX_HEALTH)).setBaseValue(0.01);
                            } else {
                                LegacyAttributeUtils.setMaxHealth(armorStand);
                            }
                            armorStand.setSmall(true);
                            armorStand.setInvisible(true);
                            armorStand.setSilent(true);
                            armorStand.setInvulnerable(true);
                            armorStand.setArms(false);
                            armorStand.setCanTick(false);
                            armorStand.setAI(false);
                            armorStand.setGravity(false);
                            armorStand.setPersistent(false);
                            armorStand.getPersistentDataContainer().set(SEAT_KEY, PersistentDataType.BOOLEAN, true);
                        }) :
                EntityUtils.spawnEntity(player.getWorld(),
                        VersionHelper.isOrAbove1_20_2() ? location : location.subtract(0, 0.25, 0),
                        EntityType.ITEM_DISPLAY,
                        entity -> {
                            ItemDisplay itemDisplay = (ItemDisplay) entity;
                            itemDisplay.setPersistent(false);
                            itemDisplay.getPersistentDataContainer().set(SEAT_KEY, PersistentDataType.BOOLEAN, true);
                        });
        if (!seatEntity.addPassenger(player)) {
            seatEntity.remove();
        } else {
            this.seatEntity = new WeakReference<>(seatEntity);
        }
    }

    private Location calculateSeatLocation(Location sourceLocation) {
        for (Property<?> property : super.blockState.getProperties()) {
            if (property.name().equals("facing") && property.valueClass() == HorizontalDirection.class) {
                switch ((HorizontalDirection) super.blockState.get(property)) {
                    case NORTH -> sourceLocation.setYaw(0);
                    case SOUTH -> sourceLocation.setYaw(180);
                    case WEST -> sourceLocation.setYaw(270);
                    case EAST -> sourceLocation.setYaw(90);
                }
                break;
            }
            if (property.name().equals("facing_clockwise") && property.valueClass() == HorizontalDirection.class) {
                switch ((HorizontalDirection) super.blockState.get(property)) {
                    case NORTH -> sourceLocation.setYaw(90);
                    case SOUTH -> sourceLocation.setYaw(270);
                    case WEST -> sourceLocation.setYaw(0);
                    case EAST -> sourceLocation.setYaw(180);
                }
                break;
            }
            if (property.name().equals("rotation") && property.valueClass() == Integer.class) {
                IntegerProperty rotation = (IntegerProperty) property;
                int min = rotation.min;
                int max = rotation.max;
                int current = (Integer) super.blockState.get(property);
                sourceLocation.setYaw((float) ((current - min) * 360) / (max - min));
            }
        }
        Vector3f offset = QuaternionUtils.toQuaternionf(0, Math.toRadians(180 - sourceLocation.getYaw()), 0).conjugate().transform(new Vector3f(this.offset));
        double yaw = this.yaw + sourceLocation.getYaw();
        if (yaw < -180) yaw += 360;
        Location newLocation = sourceLocation.clone();
        newLocation.setYaw((float) yaw);
        newLocation.add(offset.x, offset.y + 0.6, -offset.z);
        return newLocation;
    }
}
