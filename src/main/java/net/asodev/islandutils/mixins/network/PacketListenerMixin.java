package net.asodev.islandutils.mixins.network;

import com.mojang.brigadier.CommandDispatcher;
import net.asodev.islandutils.IslandUtilsEvents;
import net.asodev.islandutils.discord.DiscordPresenceUpdator;
import net.asodev.islandutils.modules.ClassicAnnouncer;
import net.asodev.islandutils.modules.cosmetics.CosmeticSlot;
import net.asodev.islandutils.modules.cosmetics.CosmeticState;
import net.asodev.islandutils.modules.cosmetics.CosmeticType;
import net.asodev.islandutils.modules.music.MusicManager;
import net.asodev.islandutils.modules.splits.LevelTimer;
import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.state.Game;
import net.asodev.islandutils.state.MccIslandState;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.IslandSoundEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(ClientPacketListener.class)
public abstract class PacketListenerMixin extends ClientCommonPacketListenerImpl {

    // I should really separate these mixins...

    @Shadow
    private CommandDispatcher<CommandSourceStack> commands;

    protected PacketListenerMixin(Minecraft minecraft, Connection connection, CommonListenerCookie commonListenerCookie) {
        super(minecraft, connection, commonListenerCookie);
    }

    @Inject(method = "handleSoundEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V", shift = At.Shift.AFTER), cancellable = true)
    public void handleCustomSoundEvent(ClientboundSoundPacket clientboundCustomSoundPacket, CallbackInfo ci) {
        if (!MccIslandState.isOnline()) return;

        ResourceLocation soundLoc = clientboundCustomSoundPacket.getSound().value().location();
        if (!soundLoc.getNamespace().equals("mcc")) return;

        if (soundLoc.getPath().startsWith("music.")) {
            ChatUtils.debug("Starting music " + soundLoc);
            MusicManager.onMusicSoundPacket(clientboundCustomSoundPacket, this.minecraft);
            ci.cancel();
        }
        if (MccIslandState.getGame() == Game.PARKOUR_WARRIOR_DOJO) {
            LevelTimer.onSound(clientboundCustomSoundPacket);
        }
    }

    @Inject(method = "handleStopSoundEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V", shift = At.Shift.AFTER), cancellable = true)
    public void handleStopSoundEvent(ClientboundStopSoundPacket clientboundStopSoundPacket, CallbackInfo ci) {
        if (!MccIslandState.isOnline()) return;
        ResourceLocation soundLoc = clientboundStopSoundPacket.getName();
        if (soundLoc == null || !soundLoc.getNamespace().equals("mcc")) return;

        if (soundLoc.getPath().startsWith("music.")) {
            ChatUtils.debug("Stopping music " + soundLoc);
            MusicManager.onMusicStopPacket(clientboundStopSoundPacket, this.minecraft);
            ci.cancel();
        }
    }

    @Inject(method = "handleContainerContent", at = @At("TAIL"))
    // Cosmetic previews, whenever we get our cosmetics back after closing menu
    private void containerContent(ClientboundContainerSetContentPacket clientboundContainerSetContentPacket, CallbackInfo ci) {
        if (!MccIslandState.isOnline()) return;
        Player player = Minecraft.getInstance().player;
        if (player == null) return; // If no player, stop
        if (clientboundContainerSetContentPacket.containerId() != 0) return; // If this is a chest, stop

        Inventory inventory = player.getInventory();
        CosmeticState.hatSlot.setOriginal(new CosmeticSlot(CosmeticType.HAT.getItem(inventory)));
        CosmeticState.accessorySlot.setOriginal(new CosmeticSlot(CosmeticType.ACCESSORY.getItem(inventory)));
    }

    private static Pattern timerPattern = Pattern.compile("(\\d+:\\d+)");

    @Inject(method = "handleBossUpdate", at = @At("HEAD")) // Discord presence, time left
    private void handleBossUpdate(ClientboundBossEventPacket clientboundBossEventPacket, CallbackInfo ci) {
        if (!MccIslandState.isOnline()) return;
        // Create a handler for the bossbar
        ClientboundBossEventPacket.Handler bossbarHandler = new ClientboundBossEventPacket.Handler() {
            @Override
            public void updateName(UUID uUID, Component component) {
                if (!component.getString().contains(":")) return; // If we don't have a timer, move on with our lives
                try {
                    String text = component.getString();
                    Matcher matcher = timerPattern.matcher(text);
                    if (!matcher.find()) return;
                    String timer = matcher.group(1);
                    String[] split = timer.split(":"); // Split by the timer
                    String minsText = split[0]; // Get the left side
                    String secsText = split[1]; // Get the right side

                    int mins = Integer.parseInt(minsText.substring(Math.max(minsText.length() - 2, 0))); // Get the last 2 character of the left side
                    int secs = Integer.parseInt(secsText.substring(0, 2)); // Get the first 2 on the right side

                    long secondsLeft = ((mins * 60L) + secs + 1);
                    long finalUnix = System.currentTimeMillis() + (secondsLeft * 1000); // Get the timestamp when the game will end

                    DiscordPresenceUpdator.timeLeftBossbar = uUID; // why do i do this again?
                    DiscordPresenceUpdator.updateTimeLeft(finalUnix); // Update our time left!!
                } catch (Exception ignored) {
                }
            }

            @Override
            public void remove(UUID uUID) { // tbf, i don't this is ever called, but y'know, just to be sure
                if (DiscordPresenceUpdator.timeLeftBossbar == uUID)
                    DiscordPresenceUpdator.updateTimeLeft(null);
            }
        };
        clientboundBossEventPacket.dispatch(bossbarHandler); // Execute the handler!
    }

    TextColor textColor = ChatUtils.parseColor("#FFA800"); // Trap Title Text Color
    Style style = Style.EMPTY.withColor(textColor); // Style for the trap color

    @Inject(method = "setSubtitleText", at = @At("HEAD"), cancellable = true)
    private void titleText(ClientboundSetSubtitleTextPacket clientboundSetSubtitleTextPacket, CallbackInfo ci) {
        if (MccIslandState.getGame() == Game.HITW) {
            ClassicAnnouncer.handleTrap(clientboundSetSubtitleTextPacket, ci);
        } else if (MccIslandState.getGame() == Game.PARKOUR_WARRIOR_DOJO) {
            LevelTimer instance = LevelTimer.getInstance();
            if (instance == null) return;
            instance.handleSubtitle(clientboundSetSubtitleTextPacket, ci);
        }
    }

    @Inject(method = "setTitleText", at = @At("HEAD")) // Game Over Sound Effect
    private void gameOver(ClientboundSetTitleTextPacket clientboundSetTitleTextPacket, CallbackInfo ci) {
        if (MccIslandState.getGame() != Game.HITW) return; // Make sure we're playing HITW
        if (!IslandOptions.getClassicHITW().isClassicHITW()) return; // Requires isClassicHITW
        String title = clientboundSetTitleTextPacket.text().getString().toUpperCase(); // Get the title in upper case

        if (title.contains("GAME OVER")) { // If we got game over title, play sound
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(IslandSoundEvents.ANNOUNCER_GAME_OVER, 1f)); // Play the sound!!
        }
    }

    @Inject(method = "handleSystemChat", at = @At("HEAD"), cancellable = true)
    private void onChat(ClientboundSystemChatPacket clientboundSystemChatPacket, CallbackInfo ci) {
        if (clientboundSystemChatPacket.overlay() || !MccIslandState.isOnline()) return;

        IslandUtilsEvents.Modifier<Component> modifier = new IslandUtilsEvents.Modifier<>();
        IslandUtilsEvents.CHAT_MESSAGE.invoker().onChatMessage(clientboundSystemChatPacket, modifier);

        modifier.withCancel(value -> ci.cancel());
        modifier.withReplacement(replacement -> {
            ci.cancel();
            this.minecraft.getChatListener().handleSystemMessage(replacement, false);
        });
    }

}
