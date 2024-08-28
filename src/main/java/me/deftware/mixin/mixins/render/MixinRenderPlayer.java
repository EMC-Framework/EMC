package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.event.events.EventSetModelVisibilities;
import me.deftware.mixin.imp.IMixinAbstractClientPlayer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.SkinTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntityRenderer.class)
public class MixinRenderPlayer {

    @Redirect(method = "updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V",
            at = @At(value = "INVOKE", target = "net/minecraft/client/network/AbstractClientPlayerEntity.isSpectator()Z", opcode = 180))
    private boolean setModelVisibilities_isSpectator(AbstractClientPlayerEntity self) {
        EventSetModelVisibilities event = new EventSetModelVisibilities(self.isSpectator());
        event.broadcast();
        return event.isSpectator();
    }

    @Redirect(method = "updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getSkinTextures()Lnet/minecraft/client/util/SkinTextures;"))
    private SkinTextures onUpdateRenderState(AbstractClientPlayerEntity instance) {
        var texture = ((IMixinAbstractClientPlayer) instance).getCustomSkinTexture();
        if (texture != null) {
            return texture;
        }
        return instance.getSkinTextures();
    }

}
