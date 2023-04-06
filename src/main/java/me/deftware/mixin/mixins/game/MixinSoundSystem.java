package me.deftware.mixin.mixins.game;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundManager.class)
public class MixinSoundSystem {

    @Shadow
    @Final
    private SoundHandler sndHandler;

    /*
    * inject after checks to make sure it's a real sound and is playable,
    * this also means we don't have to worry about getSoundSet being null.
    */
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/audio/ISound;getVolume()F", opcode = 180), method = "playSound(Lnet/minecraft/client/audio/ISound;)V", cancellable = true)
    public void onPlay(ISound instance, CallbackInfo info) {
        /* FIXME
        ITextComponent soundName = instance.createAccessor(sndHandler).getSubtitle();
        EventSound event = new EventSound(instance, (Message) soundName);
        event.broadcast();
        if (event.isCanceled()) {
            info.cancel();
        }*/
    }

}
