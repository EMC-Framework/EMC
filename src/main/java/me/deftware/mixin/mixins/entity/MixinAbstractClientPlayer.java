package me.deftware.mixin.mixins.entity;

import com.mojang.authlib.GameProfile;
import me.deftware.client.framework.event.events.EventFovModifier;
import me.deftware.client.framework.cosmetics.PlayerTexture;
import me.deftware.client.framework.cosmetics.CosmeticProvider;
import me.deftware.mixin.imp.IMixinAbstractClientPlayer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer implements IMixinAbstractClientPlayer {

	@Shadow
	private PlayerInfo playerInfo;

	@ModifyVariable(method = "getFieldOfViewModifier", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;isUsingItem()Z"), ordinal = 1)
	private float onGetSpeed(float fov) {
		EventFovModifier event = new EventFovModifier(fov);
		event.broadcast();
		return event.getFov();
	}

	@Unique
	private PlayerSkin customSkinTexture;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onInit(ClientLevel world, GameProfile profile, CallbackInfo ci) {
		UUID id = profile.id();
		for (CosmeticProvider provider : CosmeticProvider.PROVIDERS) {
			provider.load(id, () -> {
				PlayerTexture texture = provider.getPlayerTexture(id);
				if (texture != null && playerInfo != null) {
					var playerTexture = playerInfo.getSkin();
					var cape = texture.getCapeTexture();
					var capeAsset = new net.minecraft.core.ClientAsset.ResourceTexture(cape, cape);
					customSkinTexture = new PlayerSkin(
							playerTexture.body(),
							capeAsset, capeAsset, playerTexture.model(), playerTexture.secure()
					);
				}
			});
		}
	}

	@Unique
	@Override
	public PlayerInfo getPlayerNetworkInfo() {
		return playerInfo;
	}

	@Unique
	@Override
	public PlayerSkin getCustomSkinTexture() {
		return customSkinTexture;
	}

}
