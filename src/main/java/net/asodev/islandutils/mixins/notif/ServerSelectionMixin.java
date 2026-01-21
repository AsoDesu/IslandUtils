package net.asodev.islandutils.mixins.notif;

import net.asodev.islandutils.state.MccIslandNotifs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList.OnlineServerEntry;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(OnlineServerEntry.class)
public class ServerSelectionMixin {
    @Shadow
    @Final
    private ServerData serverData;

    @Unique
    private static final Identifier NOTIF_TEXTURE = Identifier.withDefaultNamespace("textures/gui/sprites/icon/unseen_notification.png");
    @Unique
    private static final Component NOTIF_TITLE = Component.translatable("islandutils.message.core.notifTitle").withStyle(Style.EMPTY.withUnderlined(true));

    @Inject(
            method = "renderContent",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/multiplayer/ServerSelectionList$OnlineServerEntry;drawIcon(Lnet/minecraft/client/gui/GuiGraphics;IILnet/minecraft/resources/Identifier;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void render(GuiGraphics guiGraphics, int i, int j, boolean bl, float f, CallbackInfo ci) {
        if (!this.serverData.ip.toLowerCase().contains("mccisland.net")) return;
        List<Component> notifs = MccIslandNotifs.getNotifLines();
        if (notifs.isEmpty()) return;

        List<Component> tooltip = new ArrayList<>();
        tooltip.add(NOTIF_TITLE);
        tooltip.add(Component.empty());
        tooltip.addAll(notifs);
        OnlineServerEntry thisEntry = (OnlineServerEntry)(Object) this;
        int nx = thisEntry.getContentX() - 10 - 2;
        int ny = thisEntry.getContentY() + 8 + 2;

        if (i >= nx && j >= ny && i < nx + 10 && j < ny + 10) {
            guiGraphics.setTooltipForNextFrame(Minecraft.getInstance().font, tooltip, Optional.empty(), i, j);
        }

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, NOTIF_TEXTURE, nx, ny, 0, 0, 10, 10, 10, 10);
    }

}
