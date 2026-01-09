package cn.gtemc.craftengine.item.processor;

import cn.gtemc.craftengine.item.settings.AttributesSetting;
import cn.gtemc.craftengine.plugin.context.RandomNumberContext;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.momirealms.craftengine.bukkit.item.DataComponentTypes;
import net.momirealms.craftengine.core.attribute.AttributeModifier;
import net.momirealms.craftengine.core.item.CustomItem;
import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.item.ItemBuildContext;
import net.momirealms.craftengine.core.item.ItemProcessorFactory;
import net.momirealms.craftengine.core.item.processor.SimpleNetworkItemProcessor;
import net.momirealms.craftengine.core.plugin.context.ContextKey;
import net.momirealms.craftengine.core.util.ResourceConfigUtils;
import net.momirealms.craftengine.libraries.nbt.CompoundTag;
import net.momirealms.craftengine.libraries.nbt.DoubleTag;
import net.momirealms.craftengine.libraries.nbt.Tag;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class GetArgumentsProcessor implements SimpleNetworkItemProcessor {
    public static final ItemProcessorFactory<GetArgumentsProcessor> FACTORY = new Factory();
    private final boolean attribute;

    public GetArgumentsProcessor(boolean attribute) {
        this.attribute = attribute;
    }

    @Override
    public <I> Item<I> prepareNetworkItem(Item<I> item, ItemBuildContext context, CompoundTag networkData) {
        RandomNumberContext randomNumberContext = RandomNumberContext.of(context.player(), item);
        if (this.attribute) {
            List<AttributeModifier> attributeModifiers = new ObjectArrayList<>();
            CustomItem<I> customItem = item.getCustomItem().orElse(null);
            if (customItem != null) {
                List<AttributesSetting.AttributeData> attributeDataList = customItem.settings().getCustomData(AttributesSetting.ATTRIBUTES);
                for (AttributesSetting.AttributeData attributeData : attributeDataList) {
                    if (attributeData.expires() != null && attributeData.expires().before(new Date())) continue; // 过期不管
                    if (attributeData.conditions() != null && !attributeData.conditions().test(randomNumberContext)) continue; // 不符合条件不管
                    CompoundTag customData = item.getSparrowNBTComponent(DataComponentTypes.CUSTOM_DATA) instanceof CompoundTag tag ? tag : null;
                    if (customData == null) {
                        continue;
                    }
                    CompoundTag randomNumberData = customData.getCompound(RandomNumberContext.RANDOM_NUMBER_KEY);
                    for (Map.Entry<String, Tag> entry : randomNumberData.entrySet()) {
                        if (entry.getValue() instanceof DoubleTag tag) {
                            context.contexts().withParameter(ContextKey.direct("random_number_" + entry.getKey()), tag.value());
                        }
                    }
                }
            }
            item.attributeModifiers(attributeModifiers);
        }
        return item;
    }

    @Override
    public <I> Item<I> apply(Item<I> item, ItemBuildContext context) {
        return item;
    }

    public static class Factory implements ItemProcessorFactory<GetArgumentsProcessor> {

        @Override
        public GetArgumentsProcessor create(Object arg) {
            Map<String, Object> args = ResourceConfigUtils.getAsMap(arg, "gtemc:get_arguments");
            boolean attribute = ResourceConfigUtils.getAsBoolean(args.get("attribute"), "attribute");
            return new GetArgumentsProcessor(attribute);
        }
    }
}
