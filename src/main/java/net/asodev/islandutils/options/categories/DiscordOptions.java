package net.asodev.islandutils.options.categories;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.asodev.islandutils.options.saving.Ignore;
import net.minecraft.network.chat.Component;

public class DiscordOptions implements OptionsCategory {
    @Ignore
    private static final DiscordOptions defaults = new DiscordOptions();

    public boolean discordPresence = true;
    public boolean showGame = true;
    public boolean showGameInfo = true;
    public boolean showTimeRemaining = true;
    public boolean showTimeElapsed = true;

    @Override
    public ConfigCategory getCategory() {
        Option<Boolean> discordPresenceOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.discordPresence"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.discordPresence.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.discordPresence, () -> discordPresence, value -> this.discordPresence = value)
                .build();
        Option<Boolean> showGameOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showGame"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.showGame.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.showGame, () -> showGame, value -> this.showGame = value)
                .build();
        Option<Boolean> showGameInfoOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showGameInfo"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.showGameInfo.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.showGameInfo, () -> showGameInfo, value -> this.showGameInfo = value)
                .build();
        Option<Boolean> showTimeRemainOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showTimeRemaining"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.showTimeRemaining.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.showTimeRemaining, () -> showTimeRemaining, value -> this.showTimeRemaining = value)
                .build();
        Option<Boolean> showTimeElapsedOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showTimeElapsed"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.showTimeElapsed.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.showTimeElapsed, () -> showTimeElapsed, value -> this.showTimeElapsed = value)
                .build();

        return ConfigCategory.createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.category.discord"))
                .option(discordPresenceOption)
                .group(OptionGroup.createBuilder()
                        .name(Component.translatable("text.autoconfig.islandutils.group.presenceDisplayOptions"))
                        .collapsed(false)
                        .option(showGameOption)
                        .option(showGameInfoOption)
                        .option(showTimeRemainOption)
                        .option(showTimeElapsedOption)
                        .build())
                .build();
    }
}
