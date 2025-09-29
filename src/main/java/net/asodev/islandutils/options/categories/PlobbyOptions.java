package net.asodev.islandutils.options.categories;

import dev.isxander.yacl3.api.ConfigCategory;
import net.asodev.islandutils.options.saving.Ignore;
import net.minecraft.network.chat.Component;

public class PlobbyOptions implements OptionsCategory {
    @Ignore
    private static final PlobbyOptions defaults = new PlobbyOptions();

    @Override
    public ConfigCategory getCategory() {
        return ConfigCategory.createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.category.plobbyIntegration"))
                .build();
    }
}
