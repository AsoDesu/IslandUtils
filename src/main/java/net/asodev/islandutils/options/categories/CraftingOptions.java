package net.asodev.islandutils.options.categories;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.asodev.islandutils.options.saving.Ignore;
import net.minecraft.network.chat.Component;

public class CraftingOptions implements OptionsCategory {
    @Ignore
    private static final CraftingOptions defaults = new CraftingOptions();

    boolean enableCraftingNotifs = true;
    boolean toastNotif = true;
    boolean chatNotif = true;
    boolean notifyServerList = true;

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

    @Override
    public ConfigCategory getCategory() {
        Option<Boolean> enableOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.enableCraftingNotifs"))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.enableCraftingNotifs, () -> enableCraftingNotifs, value -> this.enableCraftingNotifs = value)
                .build();
        Option<Boolean> toastOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.toastNotif"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.toastNotif.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.toastNotif, () -> toastNotif, value -> this.toastNotif = value)
                .build();
        Option<Boolean> chatOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.chatNotif"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.chatNotif.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.chatNotif, () -> chatNotif, value -> this.chatNotif = value)
                .build();
        Option<Boolean> notifyOption = Option.<Boolean>createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.option.notifyServerList"))
                .description(OptionDescription.of(Component.translatable("text.autoconfig.islandutils.option.notifyServerList.@Tooltip")))
                .controller(TickBoxControllerBuilder::create)
                .binding(defaults.notifyServerList, () -> notifyServerList, value -> this.notifyServerList = value)
                .build();
        return ConfigCategory.createBuilder()
                .name(Component.translatable("text.autoconfig.islandutils.category.crafting_notifs"))
                .option(enableOption)
                .option(toastOption)
                .option(chatOption)
                .option(notifyOption)
                .build();
    }
}
