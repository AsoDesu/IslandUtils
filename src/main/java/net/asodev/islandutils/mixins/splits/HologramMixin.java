package net.asodev.islandutils.mixins.splits;

import com.llamalad7.mixinextras.sugar.Local;
import net.asodev.islandutils.modules.splits.SplitManager;
import net.asodev.islandutils.state.Game;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.TimeUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Pattern;

@Mixin(value = ClientPacketListener.class, priority = 990)
public class HologramMixin {
    @Shadow
    @Final
    private static Logger LOGGER;

    @Unique
    private static final Pattern pattern = Pattern.compile("\\d+d \\d+h \\d+m");

    @Inject(method = "handleSetEntityData", at = @At("RETURN"))
    private void handleEntityData(ClientboundSetEntityDataPacket clientboundSetEntityDataPacket, CallbackInfo ci, @Local Entity entity) {
        if (!(entity instanceof Display.TextDisplay hologram)) return;
        if (!MccIslandState.isOnline() || MccIslandState.getGame() != Game.PARKOUR_WARRIOR_DOJO) return;

        Component customName = hologram.getCustomName(); // Noxcrew. you use text display. but set CUSTOM NAME TO DISPLAY THE TEXT???
        if(customName == null) return;

        var matcher = pattern.matcher(customName.getString());
        if (!matcher.find()) return;
        var seconds = TimeUtil.getTimeSeconds(matcher.group());
        if (seconds == -1) {
            LOGGER.error("Failed parsing time for course expiry");
            return;
        }
        ChatUtils.debug("Found course expiry: " + seconds);
        SplitManager.setCurrentCourseExpiry(System.currentTimeMillis() + (seconds * 1000L));
    }
}
