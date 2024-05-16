package net.asodev.islandutils.modules;

import com.noxcrew.noxesium.network.NoxesiumPackets;
import com.noxcrew.noxesium.network.clientbound.ClientboundMccServerPacket;
import net.asodev.islandutils.state.Game;
import net.asodev.islandutils.state.MccIslandState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoxesiumIntegration {
    private static Logger LOGGER = LoggerFactory.getLogger(NoxesiumIntegration.class);

    public void init() {
        NoxesiumPackets.MCC_SERVER.addListener(this, (any, packet, ctx) -> {
            handleServerPacket(packet);
        });
    }

    private void handleServerPacket(ClientboundMccServerPacket packet) {
        if (packet.serverType().startsWith("lobby")) {
            MccIslandState.setGame(Game.HUB);
        } else {
            try {
                MccIslandState.setGame(Game.fromPacket(packet));
            } catch (Exception e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

}
