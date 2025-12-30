package cn.gtemc.craftengine.plugin.context.event;

import cn.gtemc.craftengine.plugin.context.function.ApplyDataFunction;
import cn.gtemc.craftengine.plugin.context.function.SetLoreFunction;
import cn.gtemc.craftengine.util.RegistryUtils;
import net.momirealms.craftengine.core.plugin.context.CommonConditions;
import net.momirealms.craftengine.core.plugin.context.CommonFunctionType;
import net.momirealms.craftengine.core.plugin.context.Context;
import net.momirealms.craftengine.core.plugin.context.FunctionType;
import net.momirealms.craftengine.core.util.Key;

public class EventFunctions {
    public static final CommonFunctionType<SetLoreFunction<Context>> SET_LORE = RegistryUtils.registerEventFunction(Key.of("gtemc:set_lore"), SetLoreFunction.factory(CommonConditions::fromMap));
    public static final CommonFunctionType<ApplyDataFunction<Context>> APPLY_DATA = RegistryUtils.registerEventFunction(Key.of("gtemc:apply_data"), ApplyDataFunction.factory(CommonConditions::fromMap));

    public static void register() {
    }
}
