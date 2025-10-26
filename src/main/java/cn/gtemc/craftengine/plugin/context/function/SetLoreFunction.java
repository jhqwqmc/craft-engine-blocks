package cn.gtemc.craftengine.plugin.context.function;

import cn.gtemc.craftengine.plugin.context.event.EventFunctions;
import net.momirealms.craftengine.core.entity.player.InteractionHand;
import net.momirealms.craftengine.core.entity.player.Player;
import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.item.ItemBuildContext;
import net.momirealms.craftengine.core.item.modifier.ItemDataModifier;
import net.momirealms.craftengine.core.item.modifier.lore.LoreModifier;
import net.momirealms.craftengine.core.plugin.context.Condition;
import net.momirealms.craftengine.core.plugin.context.Context;
import net.momirealms.craftengine.core.plugin.context.PlayerOptionalContext;
import net.momirealms.craftengine.core.plugin.context.event.EventConditions;
import net.momirealms.craftengine.core.plugin.context.function.AbstractConditionalFunction;
import net.momirealms.craftengine.core.plugin.context.function.Function;
import net.momirealms.craftengine.core.plugin.context.selector.PlayerSelector;
import net.momirealms.craftengine.core.plugin.context.selector.PlayerSelectors;
import net.momirealms.craftengine.core.util.ItemUtils;
import net.momirealms.craftengine.core.util.Key;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings({"unchecked", "rawtypes", "OptionalUsedAsFieldOrParameterType"})
public class SetLoreFunction<CTX extends Context> extends AbstractConditionalFunction<CTX> {
    public static final FactoryImpl<PlayerOptionalContext> FACTORY = new FactoryImpl<>(EventConditions::fromMap);
    private final PlayerSelector<CTX> selector;
    private final Optional<InteractionHand> hand;
    private final ItemDataModifier<?> loreModifier;

    public SetLoreFunction(
            List<Condition<CTX>> predicates,
            PlayerSelector<CTX> selector,
            Optional<InteractionHand> optionalHand,
            ItemDataModifier<?> loreModifier
    ) {
        super(predicates);
        this.selector = selector;
        this.hand = optionalHand;
        this.loreModifier = loreModifier;
    }

    @Override
    protected void runInternal(CTX ctx) {
        for (Player player : this.selector.get(ctx)) {
            Item item = player.getItemInHand(this.hand.orElse(InteractionHand.MAIN_HAND));
            if (ItemUtils.isEmpty(item)) continue;
            item.apply(this.loreModifier, ItemBuildContext.of(player));
        }
    }

    @Override
    public Key type() {
        return EventFunctions.SET_LORE;
    }

    public static class FactoryImpl<CTX extends Context> extends AbstractFactory<CTX> {

        public FactoryImpl(java.util.function.Function<Map<String, Object>, Condition<CTX>> factory) {
            super(factory);
        }

        @Override
        public Function<CTX> create(Map<String, Object> arguments) {
            PlayerSelector<CTX> selector = PlayerSelectors.fromObject(arguments.getOrDefault("target", "self"), conditionFactory());
            Optional<InteractionHand> optionalHand = Optional.ofNullable(arguments.get("hand")).map(it -> InteractionHand.valueOf(it.toString().toUpperCase(Locale.ENGLISH)));
            ItemDataModifier<?> loreModifier = LoreModifier.createLoreModifier(arguments.get("lore"));
            return new SetLoreFunction<>(getPredicates(arguments), selector, optionalHand, loreModifier);
        }
    }
}
