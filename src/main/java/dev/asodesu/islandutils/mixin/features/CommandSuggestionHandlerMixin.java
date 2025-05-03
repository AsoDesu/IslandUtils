package dev.asodesu.islandutils.mixin.features;

import dev.asodesu.islandutils.api.extentions.MinecraftExtKt;
import dev.asodesu.islandutils.features.FriendsInGame;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundCommandSuggestionsPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class CommandSuggestionHandlerMixin {

    @Inject(method = "handleCommandSuggestions", cancellable = true, at = @At("HEAD"))
    private void commandSuggestionsResponse(ClientboundCommandSuggestionsPacket clientboundCommandSuggestionsPacket, CallbackInfo ci) {
        if (!MinecraftExtKt.isOnline()) return;

        // check for a FriendsInGame response
        if (clientboundCommandSuggestionsPacket.id() == FriendsInGame.TRANSACTION_ID) {
            FriendsInGame.INSTANCE.receiveSuggestionCallback(clientboundCommandSuggestionsPacket.suggestions());
            ci.cancel();
        }
    }

}
