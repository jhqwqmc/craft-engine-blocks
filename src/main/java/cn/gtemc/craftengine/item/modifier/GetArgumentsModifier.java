package cn.gtemc.craftengine.item.modifier;

import cn.gtemc.craftengine.item.ItemDataModifiers;
import cn.gtemc.craftengine.item.settings.AttributesSetting;
import cn.gtemc.craftengine.plugin.context.RandomNumberContext;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.momirealms.craftengine.bukkit.item.DataComponentTypes;
import net.momirealms.craftengine.core.attribute.AttributeModifier;
import net.momirealms.craftengine.core.item.CustomItem;
import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.item.ItemBuildContext;
import net.momirealms.craftengine.core.item.ItemDataModifierFactory;
import net.momirealms.craftengine.core.item.modifier.ItemDataModifier;
import net.momirealms.craftengine.core.item.modifier.SimpleNetworkItemDataModifier;
import net.momirealms.craftengine.core.plugin.context.ContextKey;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.util.ResourceConfigUtils;
import net.momirealms.craftengine.libraries.nbt.CompoundTag;
import net.momirealms.craftengine.libraries.nbt.DoubleTag;
import net.momirealms.craftengine.libraries.nbt.Tag;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class GetArgumentsModifier<I> implements SimpleNetworkItemDataModifier<I> {
    public static final Factory<?> FACTORY = new Factory<>();
    private final boolean attribute;

    public GetArgumentsModifier(boolean attribute) {
        this.attribute = attribute;
    }

    @Override
    public Key type() {
        return ItemDataModifiers.GET_ARGUMENTS;
    }

    @Override
    public Item<I> prepareNetworkItem(Item<I> item, ItemBuildContext context, CompoundTag networkData) {
        RandomNumberContext randomNumberContext = RandomNumberContext.of(context.player(), item);
        if (this.attribute) {
            List<AttributeModifier> attributeModifiers = new ObjectArrayList<>();
            CustomItem<I> customItem = item.getCustomItem().orElse(null);
            if (customItem != null) {
                List<AttributesSetting.AttributeData> attributeDataList = customItem.settings().getCustomData(AttributesSetting.AttributeDataType.INSTANCE);
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
    public Item<I> apply(Item<I> item, ItemBuildContext context) {
        return item;
    }

    public static class Factory<I> implements ItemDataModifierFactory<I> {

        @Override
        public ItemDataModifier<I> create(Object arg) {
            Map<String, Object> args = ResourceConfigUtils.getAsMap(arg, "gtemc:get_arguments");
            boolean attribute = ResourceConfigUtils.getAsBoolean(args.get("attribute"), "attribute");
            return new GetArgumentsModifier<>(attribute);
        }
    }
}
