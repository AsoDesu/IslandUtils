package dev.asodesu.islandutils.mixin.serverlist;

import dev.asodesu.islandutils.api.notifier.ServerListNotificationRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.SelectableEntry;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerSelectionList.OnlineServerEntry.class)
public abstract class ServerListEntryRenderMixin extends ServerSelectionList.Entry implements SelectableEntry {

    @Inject(method = "renderContent", at = @At("TAIL"))
    private void render(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean bl, float f, CallbackInfo ci) {
        ServerListNotificationRenderer.INSTANCE.render(
                guiGraphics,
                (ServerSelectionList.OnlineServerEntry)(Object)this,
                getY(),
                getX(),
                mouseX,
                mouseY
        );
    }

}
