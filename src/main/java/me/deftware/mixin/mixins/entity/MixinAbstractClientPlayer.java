package me.deftware.mixin.mixins.entity;

import com.mojang.authlib.GameProfile;
import me.deftware.client.framework.event.events.EventFovModifier;
import me.deftware.client.framework.event.events.EventSpectator;
import me.deftware.mixin.imp.IMixinAbstractClientPlayer;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import me.deftware.client.framework.cosmetics.PlayerTexture;
import me.deftware.client.framework.cosmetics.CosmeticProvider;
import me.deftware.client.framework.util.minecraft.MinecraftIdentifier;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer implements IMixinAbstractClientPlayer {

	@Shadow
	private NetworkPlayerInfo playerInfo;

	@Inject(method = "isSpectator", at = @At(value = "TAIL"), cancellable = true)
	private void onIsSpectator(CallbackInfoReturnable<Boolean> cir) {
		EventSpectator event = new EventSpectator(cir.getReturnValue());
		cir.setReturnValue(event.isSpectator());
	}

	@ModifyVariable(method = "getFovModifier", ordinal = 0, at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/client/entity/AbstractClientPlayer;getAttribute(Lnet/minecraft/entity/ai/attributes/IAttribute;)Lnet/minecraft/entity/ai/attributes/IAttributeInstance;"))
	private float onGetSpeed(float f) {
		EventFovModifier event = new EventFovModifier(f);
		event.broadcast();
		return event.getFov();
	}

	@Unique
	private PlayerTexture texture;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onInit(World worldIn, GameProfile profile, CallbackInfo ci) {
		UUID id = profile.getId();
		for (CosmeticProvider provider : CosmeticProvider.PROVIDERS) {
			provider.load(id, () -> {
				PlayerTexture texture = provider.getPlayerTexture(id);
				if (texture != null) {
					this.texture = texture;
				}
			});
		}
	}

	@Inject(method = "getLocationCape", at = @At("TAIL"), cancellable = true)
	public void onGetCapeTexture(CallbackInfoReturnable<ResourceLocation> ci) {
		if (texture != null) {
			MinecraftIdentifier identifier = texture.getCapeTexture();
			if (identifier != null) {
				ci.setReturnValue(identifier);
			}
		}
	}

	@Override
	public NetworkPlayerInfo getPlayerNetworkInfo() {
		return playerInfo;
	}

}
