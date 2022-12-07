package net.asodev.islandutils.mixins.sounds;

import net.asodev.islandutils.options.IslandSoundCategories;
import net.minecraft.sounds.SoundSource;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

@Mixin(SoundSource.class)
public class SoundSourceMixin {

    @Invoker("<init>")
    private static SoundSource newSoundCategory(String internalName, int internalId, String name)
    {
        throw new AssertionError();
    }

    @Shadow
    @Final
    @Mutable
    private static SoundSource[] $VALUES;

    @Inject(method = "<clinit>", at = @At(value = "FIELD",
            opcode = Opcodes.PUTSTATIC,
            target = "Lnet/minecraft/sounds/SoundSource;$VALUES:[Lnet/minecraft/sounds/SoundSource;",
            shift = At.Shift.AFTER))
    private static void addCustomVariants(CallbackInfo ci)
    {
        IslandSoundCategories.before = $VALUES;
        ArrayList<SoundSource> categories = new ArrayList<>(Arrays.asList($VALUES));

        IslandSoundCategories.GAME_MUSIC = addVariant(categories, "GAME_MUSIC");
        IslandSoundCategories.CORE_MUSIC = addVariant(categories, "CORE_MUSIC");

        $VALUES = categories.toArray(new SoundSource[0]);
    }

    private static SoundSource addVariant(ArrayList<SoundSource> categories, String name)
    {
        SoundSource cat = newSoundCategory(name.toUpperCase(Locale.ROOT),
                categories.get(categories.size() - 1).ordinal() + 1,
                name.toLowerCase(Locale.ROOT));
        categories.add(cat);
        return cat;
    }

}
