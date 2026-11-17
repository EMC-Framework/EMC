package me.deftware.mixin.mixins.render;

import me.deftware.client.framework.global.GameKeys;
import me.deftware.client.framework.global.GameMap;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class MixinLayerDeadmau5Head {

    @Inject(method = "showExtraEars", at = @At("HEAD"), cancellable = true)
    private void onShowExtraEars(CallbackInfoReturnable<Boolean> cir) {
        String playerName = ((AbstractClientPlayer) (Object) this).getGameProfile().name();
        String usernames = GameMap.INSTANCE.get(GameKeys.DEADMAU_EARS, "");
        for (String username : usernames.split(",")) {
            if (!username.isEmpty() && username.equalsIgnoreCase(playerName)) {
                cir.setReturnValue(true);
                return;
            }
        }
    }
}
