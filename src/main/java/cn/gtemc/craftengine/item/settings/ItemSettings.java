package cn.gtemc.craftengine.item.settings;

import cn.gtemc.craftengine.util.RegistryUtils;
import net.momirealms.craftengine.core.util.Key;

public class ItemSettings {
    public static final Key ATTRIBUTES = Key.of("gtemc:attributes");

    public static void register() {
        RegistryUtils.registerItemSetting(ATTRIBUTES, AttributesSetting.FACTORY);
    }
}
