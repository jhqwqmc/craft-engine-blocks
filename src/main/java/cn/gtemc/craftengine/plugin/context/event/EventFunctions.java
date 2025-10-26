package cn.gtemc.craftengine.plugin.context.event;

import cn.gtemc.craftengine.plugin.context.function.ApplyDataFunction;
import cn.gtemc.craftengine.plugin.context.function.SetLoreFunction;
import cn.gtemc.craftengine.util.RegistryUtils;
import net.momirealms.craftengine.core.util.Key;

public class EventFunctions {
    public static final Key SET_LORE = Key.of("gtemc:set_lore");
    public static final Key APPLY_DATA = Key.of("gtemc:apply_data");

    public static void register() {
        RegistryUtils.registerEventFunction(SET_LORE, SetLoreFunction.FACTORY);
        RegistryUtils.registerEventFunction(APPLY_DATA, ApplyDataFunction.FACTORY);
    }
}
