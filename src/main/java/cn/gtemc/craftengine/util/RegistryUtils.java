package cn.gtemc.craftengine.util;

import net.momirealms.craftengine.core.block.behavior.BlockBehaviorFactory;
import net.momirealms.craftengine.core.block.entity.BlockEntity;
import net.momirealms.craftengine.core.block.entity.BlockEntityType;
import net.momirealms.craftengine.core.plugin.context.PlayerOptionalContext;
import net.momirealms.craftengine.core.plugin.context.function.FunctionFactory;
import net.momirealms.craftengine.core.registry.BuiltInRegistries;
import net.momirealms.craftengine.core.registry.Registries;
import net.momirealms.craftengine.core.registry.WritableRegistry;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.util.ResourceKey;

public class RegistryUtils {

    public static void registerBlockBehavior(Key key, BlockBehaviorFactory factory) {
        ((WritableRegistry<BlockBehaviorFactory>) BuiltInRegistries.BLOCK_BEHAVIOR_FACTORY)
                .register(ResourceKey.create(Registries.BLOCK_BEHAVIOR_FACTORY.location(), key), factory);
    }

    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(Key id, BlockEntity.Factory<T> factory) {
        BlockEntityType<T> type = new BlockEntityType<>(id, factory);
        ((WritableRegistry<BlockEntityType<?>>) BuiltInRegistries.BLOCK_ENTITY_TYPE)
                .register(ResourceKey.create(Registries.BLOCK_ENTITY_TYPE.location(), id), type);
        return type;
    }

    public static void registerEventFunction(Key key, FunctionFactory<PlayerOptionalContext> factory) {
        ((WritableRegistry<FunctionFactory<PlayerOptionalContext>>) BuiltInRegistries.EVENT_FUNCTION_FACTORY)
                .register(ResourceKey.create(Registries.EVENT_FUNCTION_FACTORY.location(), key), factory);
    }
}
