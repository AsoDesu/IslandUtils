package net.asodev.islandutils.modules.crafting;

import net.asodev.islandutils.modules.crafting.state.CraftingItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static net.asodev.islandutils.util.Utils.MCC_HUD_FONT;

public class CraftingToast implements Toast {
    private static final ResourceLocation ISLAND_TOASTS_TEXTURE = ResourceLocation.fromNamespaceAndPath("island", "toasts");
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
    public Visibility getWantedVisibility() {
        return wantedVisibility;
    }

    @Override
    public void update(ToastManager toastManager, long l) {
        this.wantedVisibility = (l >= DISPLAY_TIME * toastManager.getNotificationDisplayTimeMultiplier()) ? Visibility.HIDE : Visibility.SHOW;
    }

    @Override
    public void render(GuiGraphics guiGraphics, Font font, long l) {
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, ISLAND_TOASTS_TEXTURE, 0, 0, this.width(), this.height());

        List<FormattedCharSequence> a = font.split(displayName, 125);
        if (a.size() == 1) {
            int y = 7;
            guiGraphics.drawString(font, description, 30, y, -16777216, false);
            y += 5 + 4;
            guiGraphics.drawString(font, displayName, 30, y, -11534256, false);
        } else {
            float f = 300.0f;
            int k;
            if(l < 1500L){
                k = Mth.floor(Mth.clamp((float) (1500L - l) / f, 0.0f, 1.0f) * 255.0f) << 24 | 67108864;
                guiGraphics.drawString(font, description, 30, 11, 1 | k, false);
            } else {
                k = Mth.floor(Mth.clamp((float)(l - 1500L) / f, 0.0F, 1.0F) * 252.0F) << 24 | 67108864;
                int b = this.height() / 2;
                int c = a.size();
                Objects.requireNonNull(font);
                int d = b - c * 9 / 2;

                for (Iterator<FormattedCharSequence> e = a.iterator(); e.hasNext(); d += 9){
                    FormattedCharSequence g = e.next();
                    guiGraphics.drawString(font, g, 30, d, 16777215 | k, false);
                    Objects.requireNonNull(font);
                }
            }
        }
        
        guiGraphics.renderFakeItem(itemStack, 8, 8);
    }
}
