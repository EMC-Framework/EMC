package me.deftware.mixin.mixins;

import me.deftware.client.framework.maps.SettingsMap;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.ResourceTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TextureManager.class)
public class MixinTextureManager {

    /*
    @ModifyVariable(method = "bindTextureInner", at = @At("HEAD"))
    private Identifier bindTexture(Identifier resource) {
        if ((boolean) SettingsMap.getValue(SettingsMap.MapKeys.RENDER, "RAINBOW_ITEM_GLINT", false)) {
            if (resource.getPath().contains("enchanted_item_glint")) {
                resource = new Identifier(
                        "emc/enchanted_item_glint_rainbow.png");
            }
        }
        return resource;
    }*/

}
