package net.asodev.islandutils.options.categories;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.network.chat.Component;

public class ClassicOptions implements OptionsCategory {

    boolean classicHITW = false;
    boolean classicHITWMusic = false;

    public boolean isClassicHITW() {
        return classicHITW;
    }

    public boolean isClassicHITWMusic() {
        return classicHITWMusic;
    }

    @Override
    public ConfigCategory getCategory() {
        Option<Boolean> classicHITWOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.classicHITW"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.classicHITW.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(classicHITW, () -> classicHITW, value -> this.classicHITW = value)
                .build();
        Option<Boolean> classicMusicOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.classicHITWMusic"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.classicHITWMusic.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(classicHITWMusic, () -> classicHITWMusic, value -> this.classicHITWMusic = value)
                .build();

        return ConfigCategory.createBuilder()
                .name(Component.literal("Classic HITW"))
                .option(classicHITWOption)
                .option(classicMusicOption)
                .build();
    }
}
