package cn.gtemc.craftengine.item.processor;

import cn.gtemc.craftengine.plugin.context.RandomNumberContext;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.momirealms.craftengine.bukkit.item.DataComponentTypes;
import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.item.ItemBuildContext;
import net.momirealms.craftengine.core.item.ItemProcessorFactory;
import net.momirealms.craftengine.core.item.processor.SimpleNetworkItemProcessor;
import net.momirealms.craftengine.core.plugin.context.number.NumberProvider;
import net.momirealms.craftengine.core.plugin.context.number.NumberProviders;
import net.momirealms.craftengine.core.util.ResourceConfigUtils;
import net.momirealms.craftengine.libraries.nbt.CompoundTag;

import java.util.Map;

public class RandomNumberProcessor implements SimpleNetworkItemProcessor {
    public static final ItemProcessorFactory<RandomNumberProcessor> FACTORY = new Factory();
    private final Map<String, NumberProvider> numberProviders;

    public RandomNumberProcessor(Map<String, NumberProvider> numberProviders) {
        this.numberProviders = numberProviders;
    }

    @Override
    public <I> Item<I> apply(Item<I> item, ItemBuildContext context) {
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
        public RandomNumberProcessor create(Object arg) {
            Map<String, Object> data = ResourceConfigUtils.getAsMap(arg, "gtemc:random_number");
            Map<String, NumberProvider> numberProviders = new Object2ObjectOpenHashMap<>();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                numberProviders.put(entry.getKey(), NumberProviders.fromObject(entry.getValue()));
            }
            return new RandomNumberProcessor(numberProviders);
        }
    }
}
