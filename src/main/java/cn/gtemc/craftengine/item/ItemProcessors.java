package cn.gtemc.craftengine.item;

import cn.gtemc.craftengine.item.modifier.GetArgumentsModifier;
import cn.gtemc.craftengine.item.modifier.RandomNumberModifier;
import cn.gtemc.craftengine.util.RegistryUtils;
import net.momirealms.craftengine.core.item.processor.ItemProcessorType;
import net.momirealms.craftengine.core.util.Key;

public class ItemProcessors {
    private ItemProcessors() {}

    public static final ItemProcessorType<RandomNumberModifier> RANDOM_NUMBER = RegistryUtils.registerItemProcessorType(Key.of("gtemc:random_number"), RandomNumberModifier.FACTORY);
    public static final ItemProcessorType<GetArgumentsModifier> GET_ARGUMENTS = RegistryUtils.registerItemProcessorType(Key.of("gtemc:get_arguments"), GetArgumentsModifier.FACTORY);

    public static void register() {
    }
}
