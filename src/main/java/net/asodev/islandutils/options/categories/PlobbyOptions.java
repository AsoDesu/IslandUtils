package net.asodev.islandutils.options.categories;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.asodev.islandutils.options.saving.Ignore;
import net.minecraft.network.chat.Component;

public class PlobbyOptions implements OptionsCategory {
    @Ignore
    private static final PlobbyOptions defaults = new PlobbyOptions();

    private boolean showOnScreen = false;
    private boolean showInGame = false;

    @Override
    public ConfigCategory getCategory() {
        Option<Boolean> showOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showOnScreen"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.showOnScreen.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.showOnScreen, () -> showOnScreen, value -> this.showOnScreen = value)
                .build();
        Option<Boolean> inGameOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showInGame"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.showInGame.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.showInGame, () -> showInGame, value -> this.showInGame = value)
                .build();


        return ConfigCategory.createBuilder()
                .name(Component.literal("Plobby Integration"))
                .group(OptionGroup.createBuilder()
                        .name(Component.literal("On-Screen code"))
                        .option(showOption)
                        .option(inGameOption)
                        .build())
                .build();
    }

    public boolean showOnScreen() {
        return showOnScreen;
    }

    public boolean showInGame() {
        return showInGame;
    }
}
