package cn.gtemc.craftengine.util;

import net.momirealms.craftengine.core.block.behavior.BlockBehavior;
import net.momirealms.craftengine.core.block.behavior.BlockBehaviorFactory;
import net.momirealms.craftengine.core.block.behavior.BlockBehaviorType;
import net.momirealms.craftengine.core.block.entity.BlockEntity;
import net.momirealms.craftengine.core.block.entity.BlockEntityType;
import net.momirealms.craftengine.core.item.ItemProcessorFactory;
import net.momirealms.craftengine.core.item.ItemSettingsModifier;
import net.momirealms.craftengine.core.item.ItemSettingsModifierFactory;
import net.momirealms.craftengine.core.item.ItemSettingsModifierType;
import net.momirealms.craftengine.core.item.processor.ItemProcessor;
import net.momirealms.craftengine.core.item.processor.ItemProcessorType;
import net.momirealms.craftengine.core.plugin.context.CommonFunctionType;
import net.momirealms.craftengine.core.plugin.context.Context;
import net.momirealms.craftengine.core.plugin.context.function.Function;
import net.momirealms.craftengine.core.plugin.context.function.FunctionFactory;
import net.momirealms.craftengine.core.registry.BuiltInRegistries;
import net.momirealms.craftengine.core.registry.Registries;
import net.momirealms.craftengine.core.registry.WritableRegistry;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.util.ResourceKey;

public class RegistryUtils {

    public static <T extends BlockBehavior> BlockBehaviorType<T> registerBlockBehavior(Key id, BlockBehaviorFactory<T> factory) {
        BlockBehaviorType<T> type = new BlockBehaviorType<>(id, factory);
        ((WritableRegistry<BlockBehaviorType<? extends BlockBehavior>>) BuiltInRegistries.BLOCK_BEHAVIOR_TYPE)
                .register(ResourceKey.create(Registries.BLOCK_BEHAVIOR_TYPE.location(), id), type);
        return type;
    }

    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(Key id) {
        BlockEntityType<T> type = new BlockEntityType<>(id);
        ((WritableRegistry<BlockEntityType<? extends BlockEntity>>) BuiltInRegistries.BLOCK_ENTITY_TYPE)
                .register(ResourceKey.create(Registries.BLOCK_ENTITY_TYPE.location(), id), type);
        return type;
    }

    public static <T extends Function<Context>> CommonFunctionType<T> registerEventFunction(Key id, FunctionFactory<Context, T> factory) {
        CommonFunctionType<T> type = new CommonFunctionType<>(id, factory);
        ((WritableRegistry<CommonFunctionType<?>>) BuiltInRegistries.COMMON_FUNCTION_TYPE)
                .register(ResourceKey.create(Registries.COMMON_FUNCTION_TYPE.location(), id), type);
        return type;
    }

    public static <M extends ItemSettingsModifier> ItemSettingsModifierType<M> registerItemSetting(Key id, ItemSettingsModifierFactory<M> factory) {
        ItemSettingsModifierType<M> type = new ItemSettingsModifierType<>(id, factory);
        ((WritableRegistry<ItemSettingsModifierType<? extends ItemSettingsModifier>>) BuiltInRegistries.ITEM_SETTINGS_TYPE)
                .register(ResourceKey.create(Registries.ITEM_SETTINGS_TYPE.location(), id), type);
        return type;
    }

    public static <T extends ItemProcessor> ItemProcessorType<T> registerItemProcessorType(Key id, ItemProcessorFactory<T> factory) {
        ItemProcessorType<T> type = new ItemProcessorType<>(id, factory);
        ((WritableRegistry<ItemProcessorType<?>>) BuiltInRegistries.ITEM_PROCESSOR_TYPE)
                .register(ResourceKey.create(Registries.ITEM_PROCESSOR_TYPE.location(), id), type);
        return type;
    }
}
