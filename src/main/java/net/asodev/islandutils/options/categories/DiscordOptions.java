package net.asodev.islandutils.options.categories;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.network.chat.Component;

public class DiscordOptions implements OptionsCategory {

    public boolean discordPresence = true;
    public boolean showGame = true;
    public boolean showGameInfo = true;
    public boolean showTimeRemaining = true;
    public boolean showTimeElapsed = true;
    public boolean showFactionLevel = true;

    @Override
    public ConfigCategory getCategory() {
        Option<Boolean> showGameOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showGame"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.showGame.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(showGame, () -> showGame, value -> this.showGame = value)
                .build();
        Option<Boolean> showGameInfoOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showGameInfo"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.showGameInfo.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(showGameInfo, () -> showGameInfo, value -> this.showGameInfo = value)
                .build();
        Option<Boolean> showTimeRemainOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showTimeRemaining"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.showTimeRemaining.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(showTimeRemaining, () -> showTimeRemaining, value -> this.showTimeRemaining = value)
                .build();
        Option<Boolean> showTimeElapsedOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showTimeElapsed"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.showTimeElapsed.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(showTimeElapsed, () -> showTimeElapsed, value -> this.showTimeElapsed = value)
                .build();
        Option<Boolean> showFactionOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showFactionLevel"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.showFactionLevel.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(showFactionLevel, () -> showFactionLevel, value -> this.showFactionLevel = value)
                .build();

        return ConfigCategory.createBuilder()
                .name(Component.literal("Discord Presence"))
                .option(showGameOption)
                .option(showGameInfoOption)
                .option(showTimeRemainOption)
                .option(showTimeElapsedOption)
                .option(showFactionOption)
                .build();
    }
}
