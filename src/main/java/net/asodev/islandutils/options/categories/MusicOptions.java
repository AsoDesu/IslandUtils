package net.asodev.islandutils.options.categories;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.asodev.islandutils.modules.music.MusicManager;
import net.asodev.islandutils.modules.music.MusicModifier;
import net.asodev.islandutils.options.saving.Ignore;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class MusicOptions implements OptionsCategory {
    @Ignore
    private static final MusicOptions defaults = new MusicOptions();


    @Override
    public ConfigCategory getCategory() {
        List<Option<Boolean>> modifierOptions = new ArrayList<>();
        for (MusicModifier modifier : MusicManager.getModifiers()) {
            if (!modifier.hasOption()) continue;

            var option = Option.<Boolean>createBuilder()
                    .name(modifier.name())
                    .description(OptionDescription.of(modifier.desc()))
                    .controller(TickBoxControllerBuilder::create)
                    .binding(modifier.defaultOption(), modifier::isEnabled, modifier::setEnabled)
                    .build();
            modifierOptions.add(option);
        }

        return ConfigCategory.createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.category.music"))
                .group(OptionGroup.createBuilder()
                        .name(Component.translatable("text.autoconfig.islandutils.group.modifiers"))
                        .options(modifierOptions)
                        .build())
                .build();
    }

}