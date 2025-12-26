package cn.gtemc.craftengine.item;

import cn.gtemc.craftengine.item.modifier.GetArgumentsModifier;
import cn.gtemc.craftengine.item.modifier.RandomNumberModifier;
import cn.gtemc.craftengine.util.RegistryUtils;
import net.momirealms.craftengine.core.item.processor.ItemProcessorType;

public class ItemProcessors {
    private ItemProcessors() {}

    public static final ItemProcessorType<?> RANDOM_NUMBER = RegistryUtils.registerItemProcessorType(RandomNumberModifier.ID, RandomNumberModifier.FACTORY);
    public static final ItemProcessorType<?> GET_ARGUMENTS = RegistryUtils.registerItemProcessorType(GetArgumentsModifier.ID, GetArgumentsModifier.FACTORY);

    public static void register() {
    }
}
