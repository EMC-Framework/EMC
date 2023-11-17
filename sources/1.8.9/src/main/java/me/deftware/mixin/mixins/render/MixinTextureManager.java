package me.deftware.mixin.mixins.render;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TextureManager.class)
public class MixinTextureManager {

    @ModifyVariable(method = "bindTexture", at = @At("HEAD"))
    private ResourceLocation bindTexture(ResourceLocation resource) {
        if (GameMap.INSTANCE.get(GameKeys.RAINBOW_ITEM_GLINT, false)) {
            if (resource.getResourcePath().contains("enchanted_item_glint")) {
                resource = new ResourceLocation(
                        "emc/enchanted_item_glint_rainbow.png");
            }
        }
        return resource;
    }


}
