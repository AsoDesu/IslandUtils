package net.asodev.islandutils.mixins;

import net.asodev.islandutils.discord.DiscordPresenceUpdator;
import net.asodev.islandutils.state.*;
import net.asodev.islandutils.state.cosmetics.CosmeticSlot;
import net.asodev.islandutils.state.cosmetics.CosmeticState;
import net.asodev.islandutils.state.faction.FactionComponents;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.MusicUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.WeighedSoundEvents;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(ClientPacketListener.class)
public abstract class PacketListenerMixin {

    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "handleAddObjective", at = @At("TAIL"))
    public void handleAddObjective(ClientboundSetObjectivePacket clientboundSetObjectivePacket, CallbackInfo ci) {
        if (!MccIslandState.isOnline()) return;
        Component displayName = clientboundSetObjectivePacket.getDisplayName();
        if (displayName == null) return;
        String title = displayName.getString();
        if (title == null) return;

        if (!isGameDisplayName(displayName)) {
            MccIslandState.setGame(STATE.HUB);
        } else {
            if (title.contains("HOLE IN THE WALL")) {
                MccIslandState.setGame(STATE.HITW);
            } else if (title.contains("TGTTOS")) {
                MccIslandState.setGame(STATE.TGTTOS);
            } else if (title.contains("SKY BATTLE")) {
                MccIslandState.setGame(STATE.SKY_BATTLE);
            } else if (title.contains("BATTLE BOX")) {
                MccIslandState.setGame(STATE.BATTLE_BOX);
            } else {
                System.out.println("wat");
                MccIslandState.setGame(STATE.HUB);
            }
        }
        DiscordPresenceUpdator.updatePlace();
    }

    @Inject(method = "handleSetExperience", at = @At("TAIL"))
    public void handleSetExperience(ClientboundSetExperiencePacket clientboundSetExperiencePacket, CallbackInfo ci) {
        if (!MccIslandState.isOnline()) return;
        DiscordPresenceUpdator.setLevel(clientboundSetExperiencePacket.getExperienceLevel());
    }

    String lastCheckedActionBar = "";
    @Inject(method = "setActionBarText", at = @At("TAIL"))
    public void handleSetExperience(ClientboundSetActionBarTextPacket clientboundSetActionBarTextPacket, CallbackInfo ci) {
        if (!MccIslandState.isOnline()) return;
        String text = clientboundSetActionBarTextPacket.getText().getString();
        if (Objects.equals(lastCheckedActionBar, text)) return;
        lastCheckedActionBar = text;

        FactionComponents.comps.forEach((faction, component) -> {
            if (text.contains(component.getString())) MccIslandState.setFaction(faction);
        });
    }

    private boolean isGameDisplayName(Component component) {
        for (Component sibling : component.getSiblings()) {
            if (sibling.getStyle().getColor() == TextColor.fromLegacyFormat(ChatFormatting.AQUA))
                return true;
        }
        return false;
    }

    @Inject(method = "handleSetPlayerTeamPacket", at = @At("TAIL"))
    public void handleSetPlayerTeamPacket(ClientboundSetPlayerTeamPacket clientboundSetPlayerTeamPacket, CallbackInfo ci) {
        Optional<ClientboundSetPlayerTeamPacket.Parameters> optional = clientboundSetPlayerTeamPacket.getParameters();
        optional.ifPresent((parameters) -> {
            try {
                Component prefixComponent = parameters.getPlayerPrefix();
                String playerPrefix = prefixComponent.getString().toUpperCase();

                // FIXME: idk how to properly put it outside of this methos, so yeah, it's here for now
                final Map<String, Pattern> scoreboardPatterns = Map.of(
                        "MAP", Pattern.compile("MAP: (?<map>\\w+(?:,? \\w+)*)"),
                        "MODIFIER", Pattern.compile("MODIFIER: (?<modifier>\\w+(?:,? \\w+)*)")
                );

                for (Map.Entry<String, Pattern> entry : scoreboardPatterns.entrySet()) {
                    Matcher matcher = entry.getValue().matcher(playerPrefix);
                    if (!matcher.find()) continue;
                    String value = matcher.group(1);

                    switch (entry.getKey()) {
                        case "MAP" -> MccIslandState.setMap(value);
                        case "MODIFIER" -> MccIslandState.setModifier(value);
                    }

                    ChatUtils.debug("ScoreboardUpdate - Current %s: \"%s\"", entry.getKey(), value);
                }
            } catch (Exception ignored) {}
        });
    }

    @Inject(method = "handleCustomSoundEvent", at = @At("HEAD"), cancellable = true)
    public void handleCustomSoundEvent(ClientboundCustomSoundPacket clientboundCustomSoundPacket, CallbackInfo ci) {
        PacketUtils.ensureRunningOnSameThread(clientboundCustomSoundPacket, (ClientPacketListener) (Object) this, this.minecraft);
        if (!MccIslandState.isOnline()) return;

        // Create a sound instance of the sound that is being played with this packed
        SoundInstance instance = new SimpleSoundInstance(
                clientboundCustomSoundPacket.getName(),
                SoundSource.RECORDS,
                clientboundCustomSoundPacket.getVolume(),
                clientboundCustomSoundPacket.getPitch(),
                RandomSource.create(clientboundCustomSoundPacket.getSeed()),
                false,
                0,
                SoundInstance.Attenuation.LINEAR,
                clientboundCustomSoundPacket.getX(),
                clientboundCustomSoundPacket.getY(),
                clientboundCustomSoundPacket.getZ(),
                false);

        // Attempt to get the underlying sound file from the played sound
        // We have to do this because Noxcrew obfuscated the sound ids, and may change should the resource pack update
        ResourceLocation soundLoc;
        try {
            WeighedSoundEvents sounds = instance.resolve(Minecraft.getInstance().getSoundManager());
            soundLoc = sounds.getSound(RandomSource.create()).getLocation();
        } catch (Exception e) {
            return;
        }

        // End:
        // games.global.timer.round_end
        // games.global.music.roundendmusic
        // games.global.music.overtime_intro_music
        // games.global.music.gameendmusic

        // Start:
        // games.global.countdown.go
        // games.global.music.gameintro

        // If we aren't in a game, dont play music
        if (MccIslandState.getGame() != STATE.HUB) {
            // Use the sound files above to determine what just happend in the game
            if (MccIslandState.getGame() != STATE.BATTLE_BOX) {
                if (Objects.equals(soundLoc.getPath(), "games.global.countdown.go")) {
                    // The game started. Start the music!!
                    MusicUtil.startMusic(clientboundCustomSoundPacket);
                    return;
                }
            } else {
                if (Objects.equals(soundLoc.getPath(), "games.global.music.gameintro")) {
                    MusicUtil.startMusic(clientboundCustomSoundPacket);
                    ChatUtils.debug("[PacketListener] Canceling gameintro");
                    ci.cancel();
                    return;
                }
            }
            if (Objects.equals(soundLoc.getPath(), "games.global.timer.round_end") ||
                    Objects.equals(soundLoc.getPath(), "games.global.music.roundendmusic") ||
                    Objects.equals(soundLoc.getPath(), "games.global.music.overtime_intro_music") ||
                    Objects.equals(soundLoc.getPath(), "games.global.music.gameendmusic")) {
                // The game ended or is about to end. Stop the music!!
                MusicUtil.stopMusic();
            }
        }
    }

    @Inject(method = "handleContainerContent", at = @At("HEAD"))
    private void containerContent(ClientboundContainerSetContentPacket clientboundContainerSetContentPacket, CallbackInfo ci) {
        if (Minecraft.getInstance().player == null) return;
        if (clientboundContainerSetContentPacket.getContainerId() != 0) return;

        for (int i = 0; i < clientboundContainerSetContentPacket.getItems().size(); i++) {
            ItemStack item = clientboundContainerSetContentPacket.getItems().get(i);
            COSMETIC_TYPE type = CosmeticState.getType(item);
            if (type == COSMETIC_TYPE.ACCESSORY) CosmeticState.accessorySlot.set = new CosmeticSlot(item);
            else if (type == COSMETIC_TYPE.HAT) CosmeticState.hatSlot.set = new CosmeticSlot(item);
        }
    }

    @Inject(method = "handleRespawn", at = @At("HEAD"))
    private void handleRespawn(ClientboundRespawnPacket clientboundRespawnPacket, CallbackInfo ci) {
        LocalPlayer localPlayer = this.minecraft.player;
        if (localPlayer == null) return;
        ResourceKey<Level> resourceKey = clientboundRespawnPacket.getDimension();
        if (resourceKey != localPlayer.level.dimension()) {
            MusicUtil.stopMusic();
        }
    }

    @Inject(method = "handleBossUpdate", at = @At("HEAD"))
    private void handleBossUpdate(ClientboundBossEventPacket clientboundBossEventPacket, CallbackInfo ci) {
        ClientboundBossEventPacket.Handler bossbarHandler = new ClientboundBossEventPacket.Handler(){
            @Override
            public void updateName(UUID uUID, Component component) {
                if (!component.getString().contains(":")) return;
                try {
                    String[] split = component.getString().split(":");
                    String minsText = split[0];
                    String secsText = split[1];

                    int mins = Integer.parseInt( minsText.substring( Math.max(minsText.length() - 2, 0)) );
                    int secs = Integer.parseInt( secsText.substring(0, 2) );

                    long finalUnix = System.currentTimeMillis() + (((mins * 60L) + secs+1) * 1000);

                    DiscordPresenceUpdator.timeLeftBossbar = uUID;
                    DiscordPresenceUpdator.updateTimeLeft(finalUnix);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void remove(UUID uUID) {
                if (DiscordPresenceUpdator.timeLeftBossbar == uUID)
                    DiscordPresenceUpdator.updateTimeLeft(null);
            }
        };
        clientboundBossEventPacket.dispatch(bossbarHandler);
    }

}
