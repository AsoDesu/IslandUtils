package net.asodev.islandutils.options.categories;

import dev.isxander.yacl3.api.Binding;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.gui.controllers.BooleanController;
import net.minecraft.network.chat.Component;

public class MusicOptions implements OptionsCategory {

    boolean hitwMusic = true;
    boolean bbMusic = true;
    boolean sbMusic = true;

    boolean tgttosMusic = true;
    boolean tgttosDoubleTime = true;
    boolean tgttosToTheDome = true;

    boolean pkwMusic = true;
    boolean pkwsMusic = true;

    public boolean isHitwMusic() {
        return hitwMusic;
    }
    public boolean isBbMusic() {
        return bbMusic;
    }
    public boolean isSbMusic() {
        return sbMusic;
    }
    public boolean isTgttosMusic() {
        return tgttosMusic;
    }
    public boolean isTgttosDoubleTime() {
        return tgttosDoubleTime;
    }
    public boolean isTgttosToTheDome() {
        return tgttosToTheDome;
    }
    public boolean isPkwMusic() {
        return pkwMusic;
    }
    public boolean isPkwsMusic() {
        return pkwsMusic;
    }

    @Override
    public ConfigCategory getCategory() {
        Option<Boolean> hitwOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.hitwMusic"))
                .controller(TickBoxControllerBuilder::create)
                .binding(hitwMusic, () -> hitwMusic, value -> this.hitwMusic = value)
                .build();
        Option<Boolean> bbOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.bbMusic"))
                .controller(TickBoxControllerBuilder::create)
                .binding(bbMusic, () -> bbMusic, value -> this.bbMusic = value)
                .build();
        Option<Boolean> sbOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.sbMusic"))
                .controller(TickBoxControllerBuilder::create)
                .binding(sbMusic, () -> sbMusic, value -> this.sbMusic = value)
                .build();
        Option<Boolean> tgttosOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.tgttosMusic"))
                .controller(TickBoxControllerBuilder::create)
                .binding(tgttosMusic, () -> tgttosMusic, value -> this.tgttosMusic = value)
                .build();
        Option<Boolean> tgttosDoubleOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.tgttosDoubleTime"))
                .controller(TickBoxControllerBuilder::create)
                .binding(tgttosDoubleTime, () -> tgttosDoubleTime, value -> this.tgttosDoubleTime = value)
                .build();
        Option<Boolean> tgttosToDomeOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.tgttosToTheDome"))
                .controller(TickBoxControllerBuilder::create)
                .binding(tgttosToTheDome, () -> tgttosToTheDome, value -> this.tgttosToTheDome = value)
                .build();
        Option<Boolean> pkwDojoOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.pkwMusic"))
                .controller(TickBoxControllerBuilder::create)
                .binding(pkwMusic, () -> pkwMusic, value -> this.pkwMusic = value)
                .build();
        Option<Boolean> pkwSurviorOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.pkwsMusic"))
                .controller(TickBoxControllerBuilder::create)
                .binding(pkwsMusic, () -> pkwsMusic, value -> this.pkwsMusic = value)
                .build();

        return ConfigCategory.createBuilder()
                .name(Component.literal("Music"))
                .option(hitwOption)
                .option(bbOption)
                .option(sbOption)
                .group(OptionGroup.createBuilder()
                        .name(Component.literal("TGTTOS Music"))
                        .option(tgttosOption)
                        .option(tgttosDoubleOption)
                        .option(tgttosToDomeOption)
                        .build())
                .group(OptionGroup.createBuilder()
                        .name(Component.literal("Parkour Warrior Music"))
                        .option(pkwDojoOption)
                        .option(pkwSurviorOption)
                        .build())
                .build();
    }

}
