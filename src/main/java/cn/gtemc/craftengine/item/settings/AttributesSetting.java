package cn.gtemc.craftengine.item.settings;

import net.momirealms.craftengine.core.attribute.AttributeModifier;
import net.momirealms.craftengine.core.item.ItemSettings;
import net.momirealms.craftengine.core.item.modifier.AttributeModifiersModifier;
import net.momirealms.craftengine.core.plugin.context.Condition;
import net.momirealms.craftengine.core.plugin.context.Context;
import net.momirealms.craftengine.core.plugin.context.condition.AllOfCondition;
import net.momirealms.craftengine.core.plugin.context.event.EventConditions;
import net.momirealms.craftengine.core.plugin.context.number.NumberProvider;
import net.momirealms.craftengine.core.plugin.context.number.NumberProviders;
import net.momirealms.craftengine.core.util.*;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class AttributesSetting implements ItemSettings.Modifier {
    public static final Factory FACTORY = new Factory();
    private final List<AttributeData> data;

    public AttributesSetting(List<AttributeData> data) {
        this.data = data;
    }

    @Override
    public void apply(ItemSettings settings) {
        settings.addCustomData(AttributeDataType.INSTANCE, this.data);
    }

    public static class Factory implements ItemSettings.Modifier.Factory {

        @Override
        public ItemSettings.Modifier createModifier(Object value) {
            List<AttributeData> attributeData = ResourceConfigUtils.parseConfigAsList(value, (map) -> {
                String type = ResourceConfigUtils.requireNonEmptyStringOrThrow(map.get("type"), "warning.config.item.data.attribute_modifiers.missing_type");
                Key nativeType = AttributeModifiersModifier.getNativeAttributeName(net.momirealms.craftengine.core.util.Key.of(type));
                AttributeModifier.Slot slot = AttributeModifier.Slot.valueOf(map.getOrDefault("slot", "any").toString().toUpperCase(Locale.ENGLISH));
                Key id = Key.of(ResourceConfigUtils.requireNonEmptyStringOrThrow(map.get("id"), "warning.config.item.data.attribute_modifiers.missing_id"));
                NumberProvider amount = NumberProviders.fromObject(ResourceConfigUtils.requireNonNullOrThrow(map.get("amount"), "warning.config.item.data.attribute_modifiers.missing_amount"));
                AttributeModifier.Operation operation = AttributeModifier.Operation.valueOf(
                        ResourceConfigUtils.requireNonEmptyStringOrThrow(map.get("operation"), "warning.config.item.data.attribute_modifiers.missing_operation").toUpperCase(Locale.ENGLISH)
                );
                AttributeModifiersModifier.PreModifier.PreDisplay display = null;
                if (VersionHelper.isOrAbove1_21_6() && map.containsKey("display")) {
                    Map<String, Object> displayMap = MiscUtils.castToMap(map.get("display"), false);
                    AttributeModifier.Display.Type displayType = AttributeModifier.Display.Type.valueOf(ResourceConfigUtils.requireNonEmptyStringOrThrow(displayMap.get("type"), "warning.config.item.data.attribute_modifiers.display.missing_type").toUpperCase(Locale.ENGLISH));
                    if (displayType == AttributeModifier.Display.Type.OVERRIDE) {
                        String miniMessageValue = ResourceConfigUtils.requireNonEmptyStringOrThrow(displayMap.get("value"), "warning.config.item.data.attribute_modifiers.display.missing_value");
                        display = new AttributeModifiersModifier.PreModifier.PreDisplay(displayType, miniMessageValue);
                    } else {
                        display = new AttributeModifiersModifier.PreModifier.PreDisplay(displayType, null);
                    }
                }
                AttributeModifiersModifier.PreModifier preModifier = new AttributeModifiersModifier.PreModifier(nativeType.value(), slot, Optional.of(id), amount, operation, display);
                Date expires = (Date) map.getOrDefault("expiry-time", null);
                Condition<Context> conditions = null;
                List<Condition<Context>> conditionList = ResourceConfigUtils.parseConfigAsList(map.get("conditions"), EventConditions::fromMap);
                if (conditionList.size() == 1) {
                    conditions = conditionList.getFirst();
                } else if (conditionList.size() > 1) {
                    conditions = new AllOfCondition<>(conditionList);
                }
                return new AttributeData(preModifier, expires, conditions);
            });
            return new AttributesSetting(attributeData);
        }
    }

    public record AttributeData(
            AttributeModifiersModifier.PreModifier modifier,
            @Nullable Date expires,
            @Nullable Condition<Context> conditions
    ) {}

    public static class AttributeDataType extends CustomDataType<List<AttributeData>> {
        public static final AttributeDataType INSTANCE = new AttributeDataType();
    }
}
