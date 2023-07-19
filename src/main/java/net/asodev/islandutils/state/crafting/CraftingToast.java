package net.asodev.islandutils.state.crafting;

import net.asodev.islandutils.state.crafting.state.CraftingItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class CraftingToast implements Toast {
    private static ResourceLocation ISLAND_TOASTS_TEXTURE = new ResourceLocation("island", "textures/gui/toasts.png");

    ItemStack itemStack;
    Component displayName;
    final Component description = Component.literal("Has finished crafting!").withStyle(ChatFormatting.WHITE);

    public CraftingToast(CraftingItem craftingItem) {
        itemStack = craftingItem.getStack();
        displayName = craftingItem.getTitle();
    }

    @Override
    public Toast.Visibility render(GuiGraphics guiGraphics, ToastComponent toastComponent, long l) {
        guiGraphics.blit(ISLAND_TOASTS_TEXTURE, 0, 0, 0, 0, this.width(), this.height());
        guiGraphics.drawString(toastComponent.getMinecraft().font, displayName, 30, 7, -11534256, false);
        guiGraphics.drawString(toastComponent.getMinecraft().font, description, 30, 18, -16777216, false);

        guiGraphics.renderFakeItem(itemStack, 8, 8);
        return (double)l >= 5000.0 * toastComponent.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }
}
