package me.deftware.mixin.mixins.game;


import me.deftware.client.framework.message.Message;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import me.deftware.client.framework.event.events.EventSound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundEngine.class)
public class MixinSoundSystem {

    @Shadow
    @Final
    private SoundManager soundManager;

    /*
    * inject after checks to make sure it's a real sound and is playable,
    * this also means we don't have to worry about getSoundSet being null.
    */
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/sounds/SoundInstance;getVolume()F"), method = "play(Lnet/minecraft/client/resources/sounds/SoundInstance;)Lnet/minecraft/client/sounds/SoundEngine$PlayResult;", cancellable = true)
    public void onPlay(SoundInstance instance, CallbackInfoReturnable<SoundEngine.PlayResult> info) {
        var soundEvents = instance.getOrResolve(soundManager);
        Component soundName = soundEvents != null ? soundEvents.getSubtitle() : null;
        EventSound event = new EventSound(instance, (Message) soundName);
        event.broadcast();
        if (event.isCanceled()) {
            info.setReturnValue(SoundEngine.PlayResult.NOT_STARTED);
        }
    }

}
