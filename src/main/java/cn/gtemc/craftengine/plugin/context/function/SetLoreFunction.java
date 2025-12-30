package cn.gtemc.craftengine.plugin.context.function;

import net.momirealms.craftengine.core.entity.player.InteractionHand;
import net.momirealms.craftengine.core.entity.player.Player;
import net.momirealms.craftengine.core.item.Item;
import net.momirealms.craftengine.core.item.ItemBuildContext;
import net.momirealms.craftengine.core.item.processor.ItemProcessor;
import net.momirealms.craftengine.core.item.processor.lore.LoreProcessor;
import net.momirealms.craftengine.core.plugin.context.Condition;
import net.momirealms.craftengine.core.plugin.context.Context;
import net.momirealms.craftengine.core.plugin.context.function.AbstractConditionalFunction;
import net.momirealms.craftengine.core.plugin.context.function.FunctionFactory;
import net.momirealms.craftengine.core.plugin.context.selector.PlayerSelector;
import net.momirealms.craftengine.core.plugin.context.selector.PlayerSelectors;
import net.momirealms.craftengine.core.util.ItemUtils;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class SetLoreFunction<CTX extends Context> extends AbstractConditionalFunction<CTX> {
    private final PlayerSelector<CTX> selector;
    private final Optional<InteractionHand> hand;
    private final ItemProcessor itemProcessor;

    public SetLoreFunction(
            List<Condition<CTX>> predicates,
            PlayerSelector<CTX> selector,
            Optional<InteractionHand> optionalHand,
            ItemProcessor itemProcessor
    ) {
        super(predicates);
        this.selector = selector;
        this.hand = optionalHand;
        this.itemProcessor = itemProcessor;
    }

    @Override
    protected void runInternal(CTX ctx) {
        for (Player player : this.selector.get(ctx)) {
            Item<?> item = player.getItemInHand(this.hand.orElse(InteractionHand.MAIN_HAND));
            if (ItemUtils.isEmpty(item)) continue;
            item.apply(this.itemProcessor, ItemBuildContext.of(player));
        }
    }

    public static <CTX extends Context> FunctionFactory<CTX, SetLoreFunction<CTX>> factory(java.util.function.Function<Map<String, Object>, Condition<CTX>> factory) {
        return new SetLoreFunction.Factory<>(factory);
    }

    public static class Factory<CTX extends Context> extends AbstractFactory<CTX, SetLoreFunction<CTX>> {

        public Factory(java.util.function.Function<Map<String, Object>, Condition<CTX>> factory) {
            super(factory);
        }

        @Override
        public SetLoreFunction<CTX> create(Map<String, Object> arguments) {
            PlayerSelector<CTX> selector = PlayerSelectors.fromObject(arguments.getOrDefault("target", "self"), conditionFactory());
            Optional<InteractionHand> optionalHand = Optional.ofNullable(arguments.get("hand")).map(it -> InteractionHand.valueOf(it.toString().toUpperCase(Locale.ENGLISH)));
            LoreProcessor loreProcessor = LoreProcessor.createLoreModifier(arguments.get("lore"));
            return new SetLoreFunction<>(getPredicates(arguments), selector, optionalHand, loreProcessor);
        }
    }
}
