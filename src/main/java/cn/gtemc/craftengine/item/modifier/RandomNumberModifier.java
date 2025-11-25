package cn.gtemc.craftengine.item.modifier;

import cn.gtemc.craftengine.item.ItemDataModifiers;
import cn.gtemc.craftengine.plugin.context.RandomNumberContext;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.momirealms.craftengine.bukkit.item.DataComponentTypes;
import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.item.ItemBuildContext;
import net.momirealms.craftengine.core.item.ItemDataModifierFactory;
import net.momirealms.craftengine.core.item.modifier.ItemDataModifier;
import net.momirealms.craftengine.core.item.modifier.SimpleNetworkItemDataModifier;
import net.momirealms.craftengine.core.plugin.context.number.NumberProvider;
import net.momirealms.craftengine.core.plugin.context.number.NumberProviders;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.util.ResourceConfigUtils;
import net.momirealms.craftengine.libraries.nbt.CompoundTag;

import java.util.Map;

public class RandomNumberModifier<I> implements SimpleNetworkItemDataModifier<I> {
    public static final Factory<?> FACTORY = new Factory<>();
    private final Map<String, NumberProvider> numberProviders;

    public RandomNumberModifier(Map<String, NumberProvider> numberProviders) {
        this.numberProviders = numberProviders;
    }

    @Override
    public Key type() {
        return ItemDataModifiers.RANDOM_NUMBER;
    }

    @Override
    public Item<I> apply(Item<I> item, ItemBuildContext context) {
        CompoundTag customData = item.getSparrowNBTComponent(DataComponentTypes.CUSTOM_DATA) instanceof CompoundTag tag ? tag : new CompoundTag();
        CompoundTag randomNumberData = customData.getCompound(RandomNumberContext.RANDOM_NUMBER_KEY, new CompoundTag());
        for (Map.Entry<String, NumberProvider> entry : this.numberProviders.entrySet()) {
            randomNumberData.putDouble(entry.getKey(), entry.getValue().getDouble(context));
        }
        customData.put(RandomNumberContext.RANDOM_NUMBER_KEY, randomNumberData);
        item.setNBTComponent(DataComponentTypes.CUSTOM_DATA, customData);
        return item;
    }

    public static class Factory<I> implements ItemDataModifierFactory<I> {

        @Override
        public ItemDataModifier<I> create(Object arg) {
            Map<String, Object> data = ResourceConfigUtils.getAsMap(arg, "gtemc:random_number");
            Map<String, NumberProvider> numberProviders = new Object2ObjectOpenHashMap<>();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                numberProviders.put(entry.getKey(), NumberProviders.fromObject(entry.getValue()));
            }
            return new RandomNumberModifier<>(numberProviders);
        }
    }
}
