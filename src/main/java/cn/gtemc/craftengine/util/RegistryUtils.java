package cn.gtemc.craftengine.util;

import net.momirealms.craftengine.core.block.behavior.BlockBehaviorFactory;
import net.momirealms.craftengine.core.block.behavior.BlockBehaviorType;
import net.momirealms.craftengine.core.block.entity.BlockEntity;
import net.momirealms.craftengine.core.block.entity.BlockEntityType;
import net.momirealms.craftengine.core.item.ItemProcessorFactory;
import net.momirealms.craftengine.core.item.ItemSettings;
import net.momirealms.craftengine.core.item.processor.ItemProcessorType;
import net.momirealms.craftengine.core.plugin.context.Context;
import net.momirealms.craftengine.core.plugin.context.function.FunctionFactory;
import net.momirealms.craftengine.core.registry.BuiltInRegistries;
import net.momirealms.craftengine.core.registry.Registries;
import net.momirealms.craftengine.core.registry.WritableRegistry;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.util.ResourceKey;

public class RegistryUtils {

    public static BlockBehaviorType registerBlockBehavior(Key key, BlockBehaviorFactory factory) {
        BlockBehaviorType type = new BlockBehaviorType(key, factory);
        ((WritableRegistry<BlockBehaviorType>) BuiltInRegistries.BLOCK_BEHAVIOR_TYPE)
                .register(ResourceKey.create(Registries.BLOCK_BEHAVIOR_TYPE.location(), key), type);
        return type;
    }

    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(Key id) {
        BlockEntityType<T> type = new BlockEntityType<>(id);
        ((WritableRegistry<BlockEntityType<?>>) BuiltInRegistries.BLOCK_ENTITY_TYPE)
                .register(ResourceKey.create(Registries.BLOCK_ENTITY_TYPE.location(), id), type);
        return type;
    }

    public static void registerEventFunction(Key key, FunctionFactory<Context> factory) {
        ((WritableRegistry<FunctionFactory<Context>>) BuiltInRegistries.EVENT_FUNCTION_FACTORY)
                .register(ResourceKey.create(Registries.EVENT_FUNCTION_FACTORY.location(), key), factory);
    }

    public static void registerItemSetting(Key key, ItemSettings.Modifier.Factory factory) {
        ItemSettings.Modifiers.registerFactory(key.asString(), factory);
    }

    public static <T> ItemProcessorType<T> registerItemProcessorType(Key key, ItemProcessorFactory<T> factory) {
        ItemProcessorType<T> type = new ItemProcessorType<>(key, factory);
        ((WritableRegistry<ItemProcessorType<?>>) BuiltInRegistries.ITEM_PROCESSOR_TYPE)
                .register(ResourceKey.create(Registries.ITEM_PROCESSOR_TYPE.location(), key), type);
        return type;
    }
}
