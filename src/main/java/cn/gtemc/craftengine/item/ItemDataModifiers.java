package cn.gtemc.craftengine.item;

import cn.gtemc.craftengine.item.modifier.GetArgumentsModifier;
import cn.gtemc.craftengine.item.modifier.RandomNumberModifier;
import cn.gtemc.craftengine.util.RegistryUtils;
import net.momirealms.craftengine.core.util.Key;

public class ItemDataModifiers {
    private ItemDataModifiers() {}

    public static final Key RANDOM_NUMBER = Key.of("gtemc:random_number");
    public static final Key GET_ARGUMENTS = Key.of("gtemc:get_arguments");

    public static void register() {
        RegistryUtils.registerItemDataModifier(RANDOM_NUMBER, RandomNumberModifier.FACTORY);
        RegistryUtils.registerItemDataModifier(GET_ARGUMENTS, GetArgumentsModifier.FACTORY);
    }
}
