package cn.gtemc.craftengine.plugin.context.function;

import cn.gtemc.craftengine.plugin.context.event.EventFunctions;
import net.momirealms.craftengine.core.entity.player.Player;
import net.momirealms.craftengine.core.item.ItemBuildContext;
import net.momirealms.craftengine.core.item.modifier.ItemDataModifier;
import net.momirealms.craftengine.core.plugin.context.Condition;
import net.momirealms.craftengine.core.plugin.context.Context;
import net.momirealms.craftengine.core.plugin.context.PlayerOptionalContext;
import net.momirealms.craftengine.core.plugin.context.event.EventConditions;
import net.momirealms.craftengine.core.plugin.context.function.AbstractConditionalFunction;
import net.momirealms.craftengine.core.plugin.context.function.Function;
import net.momirealms.craftengine.core.plugin.context.parameter.DirectContextParameters;
import net.momirealms.craftengine.core.registry.BuiltInRegistries;
import net.momirealms.craftengine.core.util.Key;
import net.momirealms.craftengine.core.util.ResourceConfigUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ApplyDataFunction<CTX extends Context> extends AbstractConditionalFunction<CTX> {
    public static final FactoryImpl<Context> FACTORY = new FactoryImpl<>(EventConditions::fromMap);
    private final ItemDataModifier<?>[] modifiers;

    public ApplyDataFunction(List<Condition<CTX>> predicates, ItemDataModifier<?>[] modifiers) {
        super(predicates);
        this.modifiers = modifiers;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected void runInternal(CTX ctx) {
        Player player = ctx.getOptionalParameter(DirectContextParameters.PLAYER).orElse(null);
        ctx.getOptionalParameter(DirectContextParameters.ITEM_IN_HAND).ifPresent(item -> {
            for (ItemDataModifier modifier : this.modifiers) {
                modifier.apply(item, ItemBuildContext.of(player));
            }
        });
    }

    @Override
    public Key type() {
        return EventFunctions.APPLY_DATA;
    }

    public static class FactoryImpl<CTX extends Context> extends AbstractFactory<CTX> {

        public FactoryImpl(java.util.function.Function<Map<String, Object>, Condition<CTX>> factory) {
            super(factory);
        }

        @Override
        public Function<CTX> create(Map<String, Object> arguments) {
            List<ItemDataModifier<?>> modifiers = new ArrayList<>();
            Map<String, Object> data = ResourceConfigUtils.getAsMap(arguments.get("data"), "data");
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                Optional.ofNullable(BuiltInRegistries.ITEM_DATA_MODIFIER_FACTORY.getValue(Key.withDefaultNamespace(entry.getKey(), Key.DEFAULT_NAMESPACE)))
                        .ifPresent(factory -> modifiers.add(factory.create(entry.getValue())));
            }
            return new ApplyDataFunction<>(getPredicates(arguments), modifiers.toArray(new ItemDataModifier[0]));
        }
    }
}
