package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.minecraft.GameSetting;
import net.minecraft.client.GameSettings;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightTexture.class)
public class MixinLightmapTextureManager {

    @Redirect(method = "updateLightmap", at = @At(value = "FIELD", target = "Lnet/minecraft/client/GameSettings;gammaSetting:D"))
    private double getGamma(GameSettings instance) {
        return GameSetting.GAMMA.get();
    }

}
