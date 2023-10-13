package net.asodev.islandutils.modules;

import net.asodev.islandutils.IslandUtils;
import net.asodev.islandutils.state.MccIslandState;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class Packets {

    public static void init() {
        ClientPlayConnectionEvents.JOIN.register((packetListener, sender, minecraft) -> {
            if (!MccIslandState.isOnline()) return;

            String version = IslandUtils.version;
            FabricPacket packet = new IslandUtilsJoinPacket(version);
            sender.sendPacket(packet);
        });
    }

    record IslandUtilsJoinPacket(String version) implements FabricPacket {
        public IslandUtilsJoinPacket(FriendlyByteBuf buf) {
            this(buf.readUtf());
        }

        @Override
        public void write(FriendlyByteBuf buf) {
            buf.writeUtf(this.version);
        }

        @Override
        public PacketType<?> getType() {
            return PacketType.create(new ResourceLocation("islandutils", "join"), IslandUtilsJoinPacket::new);
        }
    }

}
