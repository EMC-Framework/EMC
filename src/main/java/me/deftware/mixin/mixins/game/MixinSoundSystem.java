package me.deftware.mixin.mixins.game;

import me.deftware.client.framework.chat.ChatMessage;
import me.deftware.client.framework.event.events.EventSound;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(SoundManager.class)
public class MixinSoundSystem {

    @Shadow
    @Final
    private SoundHandler sndHandler;

    /*
    * inject after checks to make sure it's a real sound and is playable,
    * this also means we don't have to worry about getSoundSet being null.
    */
    @Inject(at = @At(value = "INVOKE", target = "Lpaulscode/sound/SoundSystem;getMasterVolume()F", opcode = 180), method = "playSound(Lnet/minecraft/client/audio/ISound;)V", cancellable = true)
    public void onPlay(ISound instance, CallbackInfo info) {
        /*TextComponent soundName = instance.(sndHandler).getSubtitle(); FIXME
        EventSound event = new EventSound(instance, soundName == null ? null : new ChatMessage().fromText(soundName));
        event.broadcast();
        if (event.isCanceled()) {
            info.cancel();
        }*/
    }

}
