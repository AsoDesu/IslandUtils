package net.asodev.islandutils.options;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.asodev.islandutils.state.splits.SplitType;
import net.minecraft.client.gui.screens.Screen;

@Config(name = "islandutils")
public class IslandOptions implements ConfigData {

    @ConfigEntry.Category("music")
    boolean tgttosMusic = true;
    @ConfigEntry.Category("music")
    boolean hitwMusic = true;
    @ConfigEntry.Category("music")
    boolean bbMusic = true;
    @ConfigEntry.Category("music")
    boolean sbMusic = true;
    @ConfigEntry.Category("music")
    boolean pkwMusic = true;
    @ConfigEntry.Category("music")
    boolean pkwsMusic = true;
    @ConfigEntry.Category("music")
    boolean tgttosDoubleTime = true;
    @ConfigEntry.Category("music")
    boolean tgttosToTheDome = true;

    @ConfigEntry.Category("cosmetics")
    boolean showPlayerPreview = true;
    @ConfigEntry.Category("cosmetics")
    boolean showOnHover = true;
    @ConfigEntry.Category("cosmetics")
    boolean showOnOnlyCosmeticMenus = true;

    @ConfigEntry.Category("discord")
    public boolean discordPresence = true;
    @ConfigEntry.Category("discord")
    @ConfigEntry.Gui.Tooltip()
    public boolean showGame = true;
    @ConfigEntry.Category("discord")
    @ConfigEntry.Gui.Tooltip()
    public boolean showGameInfo = true;
    @ConfigEntry.Category("discord")
    @ConfigEntry.Gui.Tooltip()
    public boolean showTimeRemaining = true;
    @ConfigEntry.Category("discord")
    @ConfigEntry.Gui.Tooltip()
    public boolean showTimeElapsed = true;
    @ConfigEntry.Category("discord")
    @ConfigEntry.Gui.Tooltip()
    public boolean showFactionLevel = true;

    @ConfigEntry.Category("classic_hitw")
    @ConfigEntry.Gui.Tooltip()
    boolean classicHITW = false;
    @ConfigEntry.Category("classic_hitw")
    @ConfigEntry.Gui.Tooltip()
    boolean classicHITWMusic = false;

    @ConfigEntry.Category("crafting_notfis")
    boolean enableCraftingNotifs = true;
    @ConfigEntry.Category("crafting_notfis")
    @ConfigEntry.Gui.Tooltip()
    boolean toastNotif = true;
    @ConfigEntry.Category("crafting_notfis")
    @ConfigEntry.Gui.Tooltip()
    boolean chatNotif = true;
    @ConfigEntry.Category("crafting_notfis")
    @ConfigEntry.Gui.Tooltip()
    boolean notifyServerList = true;

    @ConfigEntry.Category("pkw_splits")
    boolean enablePkwSplits = false;

    @ConfigEntry.Category("pkw_splits")
    boolean sendSplitTime = true;

    @ConfigEntry.Category("pkw_splits")
    @ConfigEntry.Gui.Tooltip()
    boolean showTimer = true;

    @ConfigEntry.Category("pkw_splits")
    @ConfigEntry.Gui.Tooltip()
    boolean showSplitImprovements = true;

    @ConfigEntry.Category("pkw_splits")
    @ConfigEntry.Gui.Tooltip()
    SplitType saveMode = SplitType.BEST;

    @ConfigEntry.Category("misc")
    boolean pauseConfirm = true;

    @ConfigEntry.Category("misc")
    @ConfigEntry.Gui.Tooltip()
    boolean showFriendsInGame = true;

    @ConfigEntry.Category("misc")
    @ConfigEntry.Gui.Tooltip()
    boolean hideSliders = true;

    @ConfigEntry.Category("misc")
    @ConfigEntry.Gui.Tooltip()
    boolean enableConfigButton = true;

    @ConfigEntry.Category("misc")
    boolean debugMode = false;

    public boolean isTgttosMusic() {
        return tgttosMusic;
    }
    public boolean isHitwMusic() {
        return hitwMusic;
    }
    public boolean isBbMusic() {
        return bbMusic;
    }
    public boolean isSbMusic() {
        return sbMusic;
    }
    public boolean isPkwMusic() {
        return pkwMusic;
    }
    public boolean isPkwsMusic() {
        return pkwsMusic;
    }
    public boolean isTgttosDoubleTime() {
        return tgttosDoubleTime;
    }
    public boolean isTgttosToTheDome() {
        return tgttosToTheDome;
    }

    public boolean isShowPlayerPreview() {
        return showPlayerPreview;
    }
    public boolean isPauseConfirm() {
        return pauseConfirm;
    }
    public boolean isHideSliders() {
        return hideSliders;
    }

    public boolean isShowCosmeticsOnHover() {
        return showOnHover;
    }
    public boolean isShowOnOnlyCosmeticMenus() {
        return showOnOnlyCosmeticMenus;
    }

    public boolean isClassicHITW() { return classicHITW; }
    public boolean isClassicHITWMusic() { return classicHITWMusic; }

    public boolean isEnableCraftingNotifs() {
        return enableCraftingNotifs;
    }
    public boolean isToastNotif() {
        return toastNotif;
    }
    public boolean isChatNotif() {
        return chatNotif;
    }
    public boolean isNotifyServerList() {
        return notifyServerList;
    }

    public boolean isEnablePkwSplits() {
        return enablePkwSplits;
    }
    public boolean isShowPKWTimer() {
        return enablePkwSplits && showTimer;
    }
    public boolean isShowSplitImprovements() {
        return enablePkwSplits && showSplitImprovements;
    }
    public boolean isSendSplitTime() {
        return enablePkwSplits && sendSplitTime;
    }
    public SplitType getSaveMode() {
        return saveMode;
    }

    public boolean isConfigButtonEnabled() {
        return enableConfigButton;
    }
    public boolean isDebugMode() {
        return debugMode;
    }

    public boolean isShowFriendsInGame() {
        return showFriendsInGame;
    }

    public static Screen getScreen(Screen parent) {
        return AutoConfig.getConfigScreen(IslandOptions.class, parent).get();
    }
    public static IslandOptions getOptions() {
        return AutoConfig.getConfigHolder(IslandOptions.class).getConfig();
    }

}
