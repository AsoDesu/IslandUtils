package net.asodev.islandutils.modules.crafting;

import net.asodev.islandutils.modules.crafting.state.CraftingItem;
import net.asodev.islandutils.util.ChatUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderPipelines;
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
        // If mcc:hub font can't render all the symbols in the string
        // use the default font. Used for localization.
        Component description = ChatUtils.checkForHudUnsupportedSymbols(I18n.get("islandutils.message.crafting.toastNotif")) ?
                Component.translatable("islandutils.message.crafting.toastNotif").withStyle(MCC_HUD_FONT.withColor(ChatFormatting.WHITE)) :
                Component.translatable("islandutils.message.crafting.toastNotif").withStyle(ChatFormatting.WHITE);


        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, ISLAND_TOASTS_TEXTURE, 0, 0, this.width(), this.height());

        List<FormattedCharSequence> sequences = font.split(displayName, 125);
        if (sequences.size() == 1) {
            int y = 7;
            guiGraphics.drawString(font, description, 30, y, -16777216, false);
            y += 5 + 4;
            guiGraphics.drawString(font, displayName, 30, y, -11534256, false);
        } else {
            float fadeDuration = 300.0f;
            if(l < 1500L){
                guiGraphics.drawString(font, description, 30, 11, 1 | (Mth.floor(Mth.clamp((float) (1500L - l) / fadeDuration, 0.0f, 1.0f) * 255.0f) << 24 | 67108864), false);
            } else {
                int rows = this.height() / 2;
                int sequenceSize = sequences.size();
                Objects.requireNonNull(font);
                int row = rows - sequenceSize * 9 / 2;

                for (Iterator<FormattedCharSequence> sequenceIterator = sequences.iterator(); sequenceIterator.hasNext(); row += 9){
                    FormattedCharSequence sequence = sequenceIterator.next();
                    guiGraphics.drawString(font, sequence, 30, row, 16777215 | (Mth.floor(Mth.clamp((float)(l - 1500L) / fadeDuration, 0.0F, 1.0F) * 252.0F) << 24 | 67108864), false);
                    Objects.requireNonNull(font);
                }
            }
        }
        
        guiGraphics.renderFakeItem(itemStack, 8, 8);
    }
}
