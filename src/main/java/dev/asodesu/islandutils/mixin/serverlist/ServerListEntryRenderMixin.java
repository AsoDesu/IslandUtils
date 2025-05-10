package dev.asodesu.islandutils.mixin.serverlist;

import dev.asodesu.islandutils.api.notifier.ServerListNotificationRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerSelectionList.OnlineServerEntry.class)
public class ServerListEntryRenderMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void render(GuiGraphics guiGraphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
        ServerListNotificationRenderer.INSTANCE.render(
                guiGraphics,
                (ServerSelectionList.OnlineServerEntry)(Object)this,
                index,
                y,
                x,
                entryWidth,
                entryHeight,
                mouseX,
                mouseY
        );
    }

}
