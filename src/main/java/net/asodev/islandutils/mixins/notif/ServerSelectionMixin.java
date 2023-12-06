package net.asodev.islandutils.mixins.notif;

import net.asodev.islandutils.state.MccIslandNotifs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerSelectionList;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;

@Mixin(ServerSelectionList.OnlineServerEntry.class)
public class ServerSelectionMixin {
    @Shadow @Final private JoinMultiplayerScreen screen;
    @Shadow @Final private ServerData serverData;
    private static ResourceLocation NOTIF_TEXTURE = new ResourceLocation("textures/gui/sprites/icon/unseen_notification.png");
    private static Component NOTIF_TITLE = Component.literal("Notifications").withStyle(Style.EMPTY.withUnderlined(true));

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/multiplayer/ServerSelectionList$OnlineServerEntry;drawIcon(Lnet/minecraft/client/gui/GuiGraphics;IILnet/minecraft/resources/ResourceLocation;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void render(GuiGraphics guiGraphics, int index, int y, int x, int ew, int eh, int mouseX, int mouseY, boolean hovered, float d, CallbackInfo ci) {
        if (!this.serverData.ip.toLowerCase().contains("mccisland.net")) return;
        List<Component> notifs = MccIslandNotifs.getNotifLines();
        if (notifs.isEmpty()) return;

        List<Component> tooltip = new ArrayList<>();
        tooltip.add(NOTIF_TITLE);
        tooltip.add(Component.empty());
        tooltip.addAll(notifs);

        int nx = x - 10 - 2;
        int ny = y + 8 + 2;

        if (mouseX >= nx && mouseY >= ny && mouseX < nx + 10 && mouseY < ny + 10) {
            this.screen.setToolTip(tooltip);
        }

        guiGraphics.blit(NOTIF_TEXTURE, nx, ny, 0, 0, 10, 10, 10, 10);
    }

}
