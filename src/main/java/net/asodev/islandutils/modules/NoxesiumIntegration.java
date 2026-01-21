package net.asodev.islandutils.modules;

import com.noxcrew.noxesium.core.mcc.ClientboundMccServerPacket;
import com.noxcrew.noxesium.core.mcc.MccPackets;
import net.asodev.islandutils.state.Game;
import net.asodev.islandutils.state.MccIslandState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoxesiumIntegration {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoxesiumIntegration.class);

    public void init() {
        MccPackets.CLIENTBOUND_MCC_SERVER.addListener(this, ClientboundMccServerPacket.class, (any, packet, ctx) -> handleServerPacket(packet));
    }

    private void handleServerPacket(ClientboundMccServerPacket packet) {
        MccIslandState.setTypes(packet.types());
        try {
            MccIslandState.setGame(Game.fromPacket(packet));
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

}
