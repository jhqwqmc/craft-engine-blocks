package cn.gtemc.craftengine.plugin.context;

import net.momirealms.craftengine.bukkit.item.DataComponentTypes;
import net.momirealms.craftengine.core.entity.player.Player;
import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.plugin.context.ContextHolder;
import net.momirealms.craftengine.core.plugin.context.ContextKey;
import net.momirealms.craftengine.core.plugin.context.PlayerOptionalContext;
import net.momirealms.craftengine.core.plugin.context.parameter.DirectContextParameters;
import net.momirealms.craftengine.libraries.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class RandomNumberContext extends PlayerOptionalContext {
    public static final String RANDOM_NUMBER_KEY = "gtemc:random_number";
    private final Item<?> item;

    public RandomNumberContext(@Nullable Player player, @NotNull Item<?> item, @NotNull ContextHolder contexts) {
        super(player, contexts);
        this.item = item;
    }

    @NotNull
    public static RandomNumberContext of(@Nullable Player player, @NotNull Item<?> item) {
        return new RandomNumberContext(player, item, new ContextHolder(Map.of(DirectContextParameters.PLAYER, () -> player)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Optional<T> getOptionalParameter(ContextKey<T> parameter) {
        String node = parameter.node();
        if (!node.startsWith("random_number_")) {
            return super.getOptionalParameter(parameter);
        }
        CompoundTag customData = this.item.getSparrowNBTComponent(DataComponentTypes.CUSTOM_DATA) instanceof CompoundTag tag ? tag : null;
        if (customData == null) {
            return super.getOptionalParameter(parameter);
        }
        CompoundTag randomNumberData = customData.getCompound(RANDOM_NUMBER_KEY);
        if (randomNumberData == null) {
            return super.getOptionalParameter(parameter);
        }
        String key = node.substring(14);
        return (Optional<T>) Optional.of(randomNumberData.getDouble(key));
    }
}
