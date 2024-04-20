package net.asodev.islandutils.modules;

import net.asodev.islandutils.options.IslandOptions;
import net.asodev.islandutils.util.ChatUtils;
import net.asodev.islandutils.util.MusicUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.network.chat.Component.literal;

public class ClassicAnnouncer {

    public static long lastTrapTimestamp = 0;
    public static String trap;

    static TextColor textColor = ChatUtils.parseColor("#FFA800"); // Trap Title Text Color
    static Style style = Style.EMPTY.withColor(textColor); // Style for the trap color
    public static void handleTrap(ClientboundSetSubtitleTextPacket clientboundSetSubtitleTextPacket, CallbackInfo ci) {
        if (!IslandOptions.getClassicHITW().isClassicHITW()) return; // Requires isClassicHITW
        String trap = clientboundSetSubtitleTextPacket.text().getString(); // Get the string version of the subtitle

        boolean isTrap = false; // Get all the elements in this component
        for (Component component : clientboundSetSubtitleTextPacket.text().toFlatList()) {
            if (component.getStyle().isObfuscated()) { return; } // If this component is obfuscated, it's the animation before the trap
            if (component.getStyle().getColor() != null && component.getStyle().getColor().equals(textColor))
                isTrap = true; // If it's the gold color of the trap subtitle, it's a trap!
        }
        if (!isTrap) return; // If we didn't find the trap, we can just stop

        String change = changeName(trap); // Check for the changed trap names
        if (change != null) { // If we have changed the name of the trap
            Minecraft.getInstance().gui.setSubtitle(literal(change).withStyle(style)); // Send our own subtitle
            ci.cancel(); // Cancel minecraft executing futher
        }

        long timestamp = System.currentTimeMillis(); // This just ensures we don't play the sound twice
        if ((timestamp - ClassicAnnouncer.lastTrapTimestamp) < 50) return; // 50ms delay
        ClassicAnnouncer.lastTrapTimestamp = timestamp;

        trap = trap.replaceAll("([ \\-!])","").toLowerCase(); // Convert the trap to a lowercase space-less string

        try {
            ClassicAnnouncer.trap = trap; // Set the trap to the one we just found
            ResourceLocation sound = new ResourceLocation("island", "announcer." + trap); // island:announcer.(trap) -> The sound location
            Minecraft.getInstance().getSoundManager().play(MusicUtil.createSoundInstance(sound)); // Play the sound!!
        } catch (Exception e) {
            e.printStackTrace(); // Something went horribly wrong, probably an invalid character
        }
    }

    private static String changeName(String originalTrap) {
        return switch (originalTrap) {
            case "Feeling Hot" -> "What in the Blazes";
            case "Hot Coals" -> "Feeling Hot";
            case "Blast-Off" -> "Kaboom";
            case "Pillagers" -> "So Lonely";
            case "Leg Day" -> "Molasses";
            case "Snowball Fight" -> "Jack Frost";
            default -> null;
        };
    }

}
