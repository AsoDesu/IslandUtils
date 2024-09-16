package net.asodev.islandutils.modules.crafting;

import net.asodev.islandutils.modules.crafting.state.CraftingItem;
import net.asodev.islandutils.util.ChatUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static net.asodev.islandutils.util.Utils.MCC_HUD_FONT;

public class CraftingToast implements Toast {
    private static ResourceLocation ISLAND_TOASTS_TEXTURE = ResourceLocation.fromNamespaceAndPath("island", "textures/gui/toasts.png");

    ItemStack itemStack;
    Component displayName;

    public CraftingToast(CraftingItem craftingItem) {
        itemStack = craftingItem.getStack();
        displayName = craftingItem.getTitle();
    }

    @Override
    public Toast.Visibility render(GuiGraphics guiGraphics, ToastComponent toastComponent, long l) {
        // If mcc:hub font can't render all the symbols in the string
        // use the default font. Used for localization.
        Component description = ChatUtils.checkForHudUnsupportedSymbols(I18n.get("islandutils.message.crafting.toastNotif")) ?
                Component.translatable("islandutils.message.crafting.toastNotif").withStyle(MCC_HUD_FONT.withColor(ChatFormatting.WHITE)) :
                Component.translatable("islandutils.message.crafting.toastNotif").withStyle(ChatFormatting.WHITE);
        
        guiGraphics.blit(ISLAND_TOASTS_TEXTURE, 0, 0, 0, 0, this.width(), this.height());
        int y = 7;
        guiGraphics.drawString(toastComponent.getMinecraft().font, description, 30, y, -16777216, false);
        y += 5 + 4;
        guiGraphics.drawString(toastComponent.getMinecraft().font, displayName, 30, y, -11534256, false);

        guiGraphics.renderFakeItem(itemStack, 8, 8);
        return (double)l >= 5000.0 * toastComponent.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }
}
