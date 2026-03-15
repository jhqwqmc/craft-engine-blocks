package cn.gtemc.craftengine.item.processor;

import cn.gtemc.craftengine.plugin.context.RandomNumberContext;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.momirealms.craftengine.bukkit.item.DataComponentTypes;
import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.item.ItemBuildContext;
import net.momirealms.craftengine.core.item.ItemProcessorFactory;
import net.momirealms.craftengine.core.item.processor.SimpleNetworkItemProcessor;
import net.momirealms.craftengine.core.plugin.config.ConfigSection;
import net.momirealms.craftengine.core.plugin.config.ConfigValue;
import net.momirealms.craftengine.core.plugin.context.number.NumberProvider;
import net.momirealms.craftengine.libraries.nbt.CompoundTag;

import java.util.Map;

public final class RandomNumberProcessor implements SimpleNetworkItemProcessor {
    public static final ItemProcessorFactory<RandomNumberProcessor> FACTORY = new Factory();
    private final Map<String, NumberProvider> numberProviders;

    public RandomNumberProcessor(Map<String, NumberProvider> numberProviders) {
        this.numberProviders = numberProviders;
    }

    @Override
    public Item apply(Item item, ItemBuildContext context) {
        CompoundTag customData = item.getSparrowNBTComponent(DataComponentTypes.CUSTOM_DATA) instanceof CompoundTag tag ? tag : new CompoundTag();
        CompoundTag randomNumberData = customData.getCompound(RandomNumberContext.RANDOM_NUMBER_KEY, new CompoundTag());
        for (Map.Entry<String, NumberProvider> entry : this.numberProviders.entrySet()) {
            randomNumberData.putDouble(entry.getKey(), entry.getValue().getDouble(context));
        }
        customData.put(RandomNumberContext.RANDOM_NUMBER_KEY, randomNumberData);
        item.setNBTComponent(DataComponentTypes.CUSTOM_DATA, customData);
        return item;
    }

    private static class Factory implements ItemProcessorFactory<RandomNumberProcessor> {

        @Override
        public RandomNumberProcessor create(ConfigValue value) {
            ConfigSection section = value.getAsSection();
            Map<String, NumberProvider> numberProviders = new Object2ObjectOpenHashMap<>();
            for (String key : section.keySet()) {
                numberProviders.put(key, section.getNonNullNumber(key));
            }
            return new RandomNumberProcessor(numberProviders);
        }
    }
}
