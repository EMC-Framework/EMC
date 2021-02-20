package me.deftware.mixin.mixins.render;

import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head;
import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LayerDeadmau5Head.class)
public class MixinLayerDeadmau5Head {

    @Redirect(method = "doRenderLayer", at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z"))
    private boolean render(String deadmau5, Object playerName) {
        String usernames = GameMap.INSTANCE.get(GameKeys.DEADMAU_EARS, "");
        for (String username : usernames.split(",")) {
            if (username.equalsIgnoreCase(playerName.toString())) {
                return true;
            }
        }
        return deadmau5.equals(playerName);
    }
}
