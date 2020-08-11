
package me.deftware.mixin.mixins;

import me.deftware.client.framework.maps.SettingsMap;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.world.WorldSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NetworkPlayerInfo.class)
public class MixinPlayerListEntry {

    @Inject(method = "getGameType", at = @At(value = "TAIL"), cancellable = true)
    private void onGetGameMode(CallbackInfoReturnable<WorldSettings.GameType> cir) {
        if ((boolean) SettingsMap.getValue(SettingsMap.MapKeys.ENTITY_SETTINGS, "FAKE_SPEC", false)) {
            cir.setReturnValue(WorldSettings.GameType.SPECTATOR);
        }
    }
}