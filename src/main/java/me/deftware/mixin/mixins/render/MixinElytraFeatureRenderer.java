package me.deftware.mixin.mixins.render;

import me.deftware.mixin.imp.IMixinAbstractClientPlayer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.SkinTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ElytraFeatureRenderer.class)
public class MixinElytraFeatureRenderer {

    /* TODO
    @Redirect(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V",
            at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;skinTextures:Lnet/minecraft/client/util/SkinTextures;")
    )
    private SkinTextures onRender(PlayerEntityRenderState instance) {
        var texture = ((IMixinAbstractClientPlayer) instance).getCustomSkinTexture();
        if (texture != null) {
            return texture;
        }
        return instance.getSkinTextures();
    }*/

}
