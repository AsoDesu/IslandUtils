package net.asodev.islandutils.options.categories;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.asodev.islandutils.options.saving.Ignore;
import net.minecraft.network.chat.Component;

public class PlobbyOptions implements OptionsCategory {
    @Ignore
    private static final PlobbyOptions defaults = new PlobbyOptions();

    private boolean showOnScreen = false;

    @Override
    public ConfigCategory getCategory() {
        Option<Boolean> showOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showOnScreen"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.showOnScreen.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.showOnScreen, () -> showOnScreen, value -> this.showOnScreen = value)
                .build();

        return ConfigCategory.createBuilder()
                .name(Component.literal("Plobby Integration"))
                .option(showOption)
                .build();
    }

    public boolean showOnScreen() {
        return showOnScreen;
    }
}
