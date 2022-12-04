package net.asodev.islandutils.state.faction;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class FactionComponents {

    public final static ResourceLocation MCC_ICONS_19 = new ResourceLocation("mcc", "icon_offset_19");
    public final static Map<FACTION, Component> comps = new HashMap();

    public static void setComponent(FACTION faction, Component component) {
        comps.put(faction, component);
    }

    public static Component getComponent(FACTION faction) {
        return comps.get(faction);
    }

}
