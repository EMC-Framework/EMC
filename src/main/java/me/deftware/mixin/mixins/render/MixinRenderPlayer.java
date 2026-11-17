package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.event.events.EventSetModelVisibilities;
import me.deftware.mixin.imp.IMixinAbstractClientPlayer;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AvatarRenderer.class)
public class MixinRenderPlayer {

    @Redirect(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Avatar;isSpectator()Z"))
    private boolean setModelVisibilities_isSpectator(Avatar self) {
        EventSetModelVisibilities event = new EventSetModelVisibilities(self.isSpectator());
        event.broadcast();
        return event.isSpectator();
    }

    @Redirect(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/ClientAvatarEntity;getSkin()Lnet/minecraft/world/entity/player/PlayerSkin;"))
    private PlayerSkin onUpdateRenderState(ClientAvatarEntity instance) {
        if (instance instanceof AbstractClientPlayer player) {
            var texture = ((IMixinAbstractClientPlayer) player).getCustomSkinTexture();
            if (texture != null) {
                return texture;
            }
        }
        return instance.getSkin();
    }

}
