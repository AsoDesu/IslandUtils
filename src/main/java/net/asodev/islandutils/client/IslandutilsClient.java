package net.asodev.islandutils.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class IslandutilsClient implements ClientModInitializer {
    public static KeyMapping previewKeyBind;

    @Override
    public void onInitializeClient() {
        previewKeyBind = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.islandutils.preview", // The translation key of the keybinding's name
                InputConstants.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_P, // The keycode of the key
                "category.islandutils.keys" // The translation key of the keybinding's category.
        ));
    }
}
