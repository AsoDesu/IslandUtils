package net.asodev.islandutils.modules.crafting;

import net.asodev.islandutils.modules.crafting.state.CraftingItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static net.asodev.islandutils.util.Utils.MCC_HUD_FONT;

public class CraftingToast implements Toast {
    private static final ResourceLocation ISLAND_TOASTS_TEXTURE = ResourceLocation.fromNamespaceAndPath("island", "textures/gui/sprites/toasts.png");
    private static final long DISPLAY_TIME = 5000; // 5s

    ItemStack itemStack;
    Component displayName;
    final Component description = Component.literal("CRAFTING COMPLETE!")
            .withStyle(MCC_HUD_FONT.withColor(ChatFormatting.WHITE));

    private Visibility wantedVisibility = Visibility.HIDE;

    public CraftingToast(CraftingItem craftingItem) {
        itemStack = craftingItem.getStack();
        displayName = craftingItem.getTitle();
    }

    @Override
    public Toast.Visibility render(GuiGraphics guiGraphics, ToastComponent toastComponent, long l) {
        guiGraphics.blit(ISLAND_TOASTS_TEXTURE, 0, 0, 0, 0, this.width(), this.height());
        int y = 7;
        guiGraphics.drawString(toastComponent.getMinecraft().font, description, 30, y, -16777216, false);
        y += 5 + 4;
        guiGraphics.drawString(toastComponent.getMinecraft().font, displayName, 30, y, -11534256, false);

        guiGraphics.renderFakeItem(itemStack, 8, 8);
        return (double)l >= 5000.0 * toastComponent.getNotificationDisplayTimeMultiplier() ? Toast.Visibility.HIDE : Toast.Visibility.SHOW;
    }
}
