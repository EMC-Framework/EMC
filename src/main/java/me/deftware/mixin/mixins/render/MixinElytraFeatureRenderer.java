package me.deftware.mixin.mixins.render;

import me.deftware.mixin.imp.IMixinAbstractClientPlayer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.util.SkinTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ElytraFeatureRenderer.class)
public class MixinElytraFeatureRenderer {

    @Redirect(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/LivingEntity;FFFFFF)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;method_52814()Lnet/minecraft/client/util/SkinTextures;")
    )
    private SkinTextures onRender(AbstractClientPlayerEntity instance) {
        var texture = ((IMixinAbstractClientPlayer) instance).getCustomSkinTexture();
        if (texture != null) {
            return texture;
        }
        return instance.method_52814();
    }

}
