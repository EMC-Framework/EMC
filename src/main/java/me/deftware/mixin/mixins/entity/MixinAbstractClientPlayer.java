package me.deftware.mixin.mixins.entity;

import com.mojang.authlib.GameProfile;
import me.deftware.client.framework.event.events.EventFovModifier;
import me.deftware.client.framework.event.events.EventSpectator;
import me.deftware.client.framework.cosmetics.PlayerTexture;
import me.deftware.client.framework.cosmetics.CosmeticProvider;
import me.deftware.mixin.imp.IMixinAbstractClientPlayer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.world.ClientWorld;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class MixinAbstractClientPlayer implements IMixinAbstractClientPlayer {

	@Shadow
	private PlayerListEntry playerListEntry;

	@Inject(method = "isSpectator", at = @At(value = "TAIL"), cancellable = true)
	private void onIsSpectator(CallbackInfoReturnable<Boolean> cir) {
		EventSpectator event = new EventSpectator(cir.getReturnValue());
		cir.setReturnValue(event.isSpectator());
	}

	@ModifyVariable(method = "getFovMultiplier", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D"))
	private float onGetSpeed(float fov) {
		EventFovModifier event = new EventFovModifier(fov);
		event.broadcast();
		return event.getFov();
	}

	@Unique
	private SkinTextures customSkinTexture;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onInit(ClientWorld world, GameProfile profile, CallbackInfo ci) {
		UUID id = profile.getId();
		for (CosmeticProvider provider : CosmeticProvider.PROVIDERS) {
			provider.load(id, () -> {
				PlayerTexture texture = provider.getPlayerTexture(id);
				if (texture != null && playerListEntry != null) {
					var playerTexture = playerListEntry.getSkinTextures();
					var cape = texture.getCapeTexture();
					customSkinTexture = new SkinTextures(
							playerTexture.texture(), playerTexture.textureUrl(),
								cape, cape, playerTexture.model(), playerTexture.secure()
					);
				}
			});
		}
	}

	@Unique
	@Override
	public PlayerListEntry getPlayerNetworkInfo() {
		return playerListEntry;
	}

	@Unique
	@Override
	public SkinTextures getCustomSkinTexture() {
		return customSkinTexture;
	}

}
