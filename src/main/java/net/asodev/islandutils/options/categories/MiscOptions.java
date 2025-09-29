package net.asodev.islandutils.options.categories;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.asodev.islandutils.options.saving.Ignore;
import net.asodev.islandutils.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;

import java.awt.*;

public class MiscOptions implements OptionsCategory {
    @Ignore
    private static final MiscOptions defaults = new MiscOptions();

    boolean pauseConfirm = true;
    boolean showFriendsInGame = true;
    boolean showFriendsInLobby = true;
    boolean silverPreview = true;
    boolean channelSwitchers = true;
    boolean showFishingUpgradeIcon = true;
    boolean showProgressBar = true;
    boolean enableConfigButton = true;
    boolean debugMode = false;
    int progressBarColorARGB = ARGB.opaque(Color.WHITE.getRGB());

    public boolean isPauseConfirm() {
        return pauseConfirm;
    }
    public boolean isShowFriendsInGame() {
        return showFriendsInGame;
    }
    public boolean isShowFriendsInLobby() {
        return showFriendsInLobby;
    }

    public boolean isSilverPreview() {
        return silverPreview;
    }
    public boolean isEnableConfigButton() {
        return enableConfigButton;
    }
    public boolean showChannelSwitchers() {
        return channelSwitchers;
    }
    public boolean isShowFishingUpgradeIcon() {
        return showFishingUpgradeIcon;
    }
    public boolean isShowProgressBar() {
        return showProgressBar;
    }
    public int getProgressBarColorARGB() {
        return progressBarColorARGB;
    }

    public boolean isDebugMode() {
        return debugMode && !Utils.isLunarClient();
    }

    @Override
    public ConfigCategory getCategory() {
        Option<Boolean> pauseOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.pauseConfirm"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.pauseConfirm.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.pauseConfirm, () -> pauseConfirm, value -> this.pauseConfirm = value)
                .build();
        Option<Boolean> showFriendsOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showFriendsInGame"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.showFriendsInGame.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.showFriendsInGame, () -> showFriendsInGame, value -> this.showFriendsInGame = value)
                .build();
        Option<Boolean> showFriendsLobbyOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showFriendsInLobby"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.showFriendsInLobby.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.showFriendsInLobby, () -> showFriendsInLobby, value -> this.showFriendsInLobby = value)
                .build();
        Option<Boolean> silverOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.silverPreview"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.silverPreview.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.silverPreview, () -> silverPreview, value -> this.silverPreview = value)
                .build();
        Option<Boolean> channelsOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.channelSwitchers"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.channelSwitchers.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.channelSwitchers, () -> channelSwitchers, value -> this.channelSwitchers = value)
                .build();
        Option<Boolean> fishingUpgradeOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showFishingUpgradeIcon"))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.showFishingUpgradeIcon, () -> showFishingUpgradeIcon, value -> this.showFishingUpgradeIcon = value)
                .build();
        Option<Boolean> buttonOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.enableConfigButton"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.enableConfigButton.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.enableConfigButton, () -> enableConfigButton, value -> this.enableConfigButton = value)
                .build();
        Option<Boolean> progressBarOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.showProgressBar"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.showProgressBar.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.showProgressBar, () -> showProgressBar, value -> this.showProgressBar = value)
                .build();
        Option<Color> progressBarColorOption = Option.<Color>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.progressBarColor"))
                .controller(ColorControllerBuilder::create)
                .binding(
                    new Color(defaults.progressBarColorARGB & 0xFFFFFF),
                    () -> new Color(progressBarColorARGB & 0xFFFFFF),
                    value -> this.progressBarColorARGB = ARGB.opaque(value.getRGB())
                )
                .build();
        Option<Boolean> debugOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.debugMode"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.debugMode.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .available(!Utils.isLunarClient()) // disable on lunar client
                .binding(defaults.debugMode, () -> debugMode, value -> this.debugMode = value)
                .build();

        return ConfigCategory.createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.category.misc"))
                .option(pauseOption)
                .group(OptionGroup.createBuilder()
                        .name(Component.translatable("text.autoconfig.islandutils.group.friendsNotifier"))
                        .collapsed(false)
                        .option(showFriendsOption)
                        .option(showFriendsLobbyOption)
                        .build())
                .option(silverOption)
                .option(channelsOption)
                .option(fishingUpgradeOption)
                .option(buttonOption)
                .option(progressBarOption)
                .option(progressBarColorOption)
                .group(OptionGroup.createBuilder()
                        .name(Component.translatable("text.autoconfig.islandutils.group.debugOptions"))
                        .collapsed(true)
                        .option(debugOption)
                        .build())
                .build();
    }
}
