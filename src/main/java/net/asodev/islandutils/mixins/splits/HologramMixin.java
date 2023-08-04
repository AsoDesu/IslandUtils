package net.asodev.islandutils.mixins.splits;

import net.asodev.islandutils.state.Game;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.modules.splits.SplitManager;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.TimeUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(ClientPacketListener.class)
public class HologramMixin {
    TextColor redColor = TextColor.fromLegacyFormat(ChatFormatting.RED);
    TextColor yellowColor = TextColor.fromLegacyFormat(ChatFormatting.YELLOW);

    @Inject(method = "handleSetEntityData", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void handleEntityData(ClientboundSetEntityDataPacket clientboundSetEntityDataPacket, CallbackInfo ci, Entity entity) {
        if (!(entity instanceof AreaEffectCloud hologram)) return;
        if (!MccIslandState.isOnline() || MccIslandState.getGame() != Game.PARKOUR_WARRIOR_DOJO) return;

        Component customName = hologram.getCustomName();
        if (customName == null) return;
        TextColor color = customName.getStyle().getColor();
        if (color == null) return;
        if (!color.equals(redColor) && !color.equals(yellowColor)) return;

        String name = customName.getString();
        long seconds = TimeUtil.getTimeSeconds(name);

        ChatUtils.debug("Found course expiry: " + name + " (" + seconds + ")");
        SplitManager.setCurrentCourseExpiry(System.currentTimeMillis() + (seconds * 1000L));
    }

}
