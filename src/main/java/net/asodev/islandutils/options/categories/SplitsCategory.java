package net.asodev.islandutils.options.categories;

import dev.isxander.yacl3.api.ButtonOption;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.asodev.islandutils.modules.splits.SplitManager;
import net.asodev.islandutils.modules.splits.SplitType;
import net.asodev.islandutils.options.saving.Ignore;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class SplitsCategory implements OptionsCategory {
    @Ignore
    private static final SplitsCategory defaults = new SplitsCategory();

    boolean enablePkwSplits = true;
    boolean sendSplitTime = true;
    boolean showTimer = true;
    boolean showSplitImprovements = true;
    int showTimerImprovementAt = -3;
    SplitType saveMode = SplitType.BEST;

    @Override
    public ConfigCategory getCategory() {
        Option<Boolean> enableOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.enablePkwSplits"))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.enablePkwSplits, () -> enablePkwSplits, value -> this.enablePkwSplits = value)
                .build();
        Option<Boolean> sendOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.sendSplitTime"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.sendSplitTime.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.sendSplitTime, () -> sendSplitTime, value -> this.sendSplitTime = value)
                .build();
        Option<Boolean> showOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showTimer"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.showTimer.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.showTimer, () -> showTimer, value -> this.showTimer = value)
                .build();
        Option<Boolean> showImprovesOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showSplitImprovements"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.showSplitImprovements.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.showSplitImprovements, () -> showSplitImprovements, value -> this.showSplitImprovements = value)
                .build();
        Option<Integer> showImprovesAtOption = Option.<Integer>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showTimerImprovementAt"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.showTimerImprovementAt.@Tooltip")))
                .controller(IntegerFieldControllerBuilder::create)
                .binding(defaults.showTimerImprovementAt, () -> showTimerImprovementAt, value -> this.showTimerImprovementAt = value)
                .build();
        Option<SplitType> saveOption = Option.<SplitType>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.saveMode"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.saveMode.@Tooltip")))
                .controller((opt) -> EnumControllerBuilder.create(opt).enumClass(SplitType.class))
                .binding(defaults.saveMode, () -> saveMode, value -> this.saveMode = value)
                .build();

        Option<?> clearSplits = ButtonOption.createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.clearSplits"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.clearSplits.@Tooltip")))
                .action((screen, b) -> doClearSplits(screen))
                .build();
        return ConfigCategory.createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.category.pkw_splits"))
                .option(enableOption)
                .option(sendOption)
                .option(showOption)
                .option(showImprovesOption)
                .option(showImprovesAtOption)
                .option(saveOption)
                .option(clearSplits)
                .build();
    }

    private void doClearSplits(Screen parent) {
        ConfirmScreen confirmScreen = new ConfirmScreen((bl) -> {
            if (bl) SplitManager.clearSplits();
            Minecraft.getInstance().setScreen(parent);
        }, Component.translatable("islandutils.clearSplits.confirmScreen.title").withStyle(ChatFormatting.RED),
                Component.translatable("islandutils.clearSplits.confirmScreen.subtitle").withStyle(ChatFormatting.DARK_RED));
        Minecraft.getInstance().setScreen(confirmScreen);
    }

    public boolean isEnablePkwSplits() {
        return enablePkwSplits;
    }
    public boolean isSendSplitTime() {
        return enablePkwSplits && sendSplitTime;
    }
    public boolean isShowTimer() {
        return enablePkwSplits && showTimer;
    }
    public boolean isShowSplitImprovements() {
        return enablePkwSplits && showSplitImprovements;
    }
    public int getShowTimerImprovementAt() {
        return showTimerImprovementAt;
    }
    public SplitType getSaveMode() {
        return saveMode;
    }
}
