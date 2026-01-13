package cn.gtemc.craftengine.item.processor;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.momirealms.craftengine.core.item.DataComponentKeys;
import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.item.ItemBuildContext;
import net.momirealms.craftengine.core.item.ItemProcessorFactory;
import net.momirealms.craftengine.core.item.data.Enchantment;
import net.momirealms.craftengine.core.item.processor.SimpleNetworkItemProcessor;
import net.momirealms.craftengine.core.util.AdventureHelper;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.util.ResourceConfigUtils;
import net.momirealms.craftengine.libraries.adventure.text.Component;
import net.momirealms.craftengine.libraries.adventure.text.format.NamedTextColor;
import net.momirealms.craftengine.libraries.adventure.text.format.Style;
import net.momirealms.craftengine.libraries.adventure.text.format.TextDecoration;

import java.util.List;
import java.util.Map;

public record EnchantmentsLoreProcessor(Map<Key, String> descriptions) implements SimpleNetworkItemProcessor {
    public static final ItemProcessorFactory<EnchantmentsLoreProcessor> FACTORY = new Factory();
    private static final Style DEFAULT_STYLE = Style.style().color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false).build();
    private static final Component SPLIT_COMPONENT = Component.text(" ");

    @Override
    public <I> Item<I> apply(Item<I> item, ItemBuildContext context) {
        List<Enchantment> enchantments = item.enchantments().orElse(List.of());
        List<Component> lore = new ObjectArrayList<>();
        for (Enchantment enchantment : enchantments) {
            Key id = enchantment.id();
            int level = enchantment.level();
            String description = this.descriptions.get(id);
            if (description != null) {
                description = description
                        .replace("%id_namespace%", id.namespace())
                        .replace("%id_value%", id.value())
                        .replace("%level%", String.valueOf(level));
                lore.addAll(AdventureHelper.splitLines(AdventureHelper.miniMessage().deserialize(description, context.tagResolvers())));
                continue;
            }
            String nameKey = "enchantment." + id.namespace() + "." + id.value();
            String levelKey = "enchantment.level." + level;
            lore.add(Component.translatable(nameKey)
                    .append(SPLIT_COMPONENT)
                    .append(Component.translatable(levelKey))
                    .style(DEFAULT_STYLE));
        }
        lore.addAll(item.loreComponent().orElse(List.of()));
        item.loreComponent(lore);
        return item;
    }

    @Override
    public <I> Key componentType(Item<I> item, ItemBuildContext context) {
        return DataComponentKeys.LORE;
    }

    @Override
    public <I> Object[] nbtPath(Item<I> item, ItemBuildContext context) {
        return new Object[]{"display", "Lore"};
    }

    @Override
    public <I> String nbtPathString(Item<I> item, ItemBuildContext context) {
        return "display.Lore";
    }

    private static class Factory implements ItemProcessorFactory<EnchantmentsLoreProcessor> {

        @Override
        public EnchantmentsLoreProcessor create(Object arg) {
            Map<Key, String> descriptions = new Object2ObjectOpenHashMap<>();
            Map<String, Object> raw = ResourceConfigUtils.getAsMap(arg, "gtemc:enchantments_lore");
            for (Map.Entry<String, Object> entry : raw.entrySet()) {
                Key id = Key.of(entry.getKey());
                Object value = entry.getValue();
                if (value == null) continue;
                String string = value.toString();
                if (string.isEmpty()) continue;
                descriptions.put(id, string);
            }
            return new EnchantmentsLoreProcessor(descriptions);
        }
    }
}
