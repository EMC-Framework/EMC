package me.deftware.mixin.mixins;

import me.deftware.client.framework.event.events.EventFovModifier;
import me.deftware.client.framework.event.events.EventSpectator;
import me.deftware.client.framework.maps.SettingsMap;
import me.deftware.client.framework.path.OSUtils;
import me.deftware.client.framework.utils.HashUtils;
import me.deftware.mixin.imp.IMixinAbstractClientPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer implements IMixinAbstractClientPlayer {

	@Shadow
	private NetworkPlayerInfo playerInfo;

	@Unique
	private boolean capeLoaded = false;

	@Unique
	private ResourceLocation capeIdentifier = null;

	@Inject(method = "isSpectator", at = @At(value = "TAIL"), cancellable = true)
	private void onIsSpectator(CallbackInfoReturnable<Boolean> cir) {
		EventSpectator event = new EventSpectator(cir.getReturnValue());
		cir.setReturnValue(event.isSpectator());
	}

	@ModifyVariable(method = "getFovModifier", ordinal = 0, at = @At(value = "INVOKE", ordinal = 0, target = "Lnet/minecraft/entity/EntityLivingBase;getEntityAttribute(Lnet/minecraft/entity/ai/attributes/IAttribute;)Lnet/minecraft/entity/ai/attributes/IAttributeInstance;"))
	private float onGetSpeed(float f) {
		EventFovModifier event = new EventFovModifier(f);
		event.broadcast();
		return event.getFov();
	}

	@Inject(method = "getLocationCape", at = @At("TAIL"), cancellable = true)
	public void onGetCapeTexture(CallbackInfoReturnable<ResourceLocation> ci) {
		try {
			String uuid = ((AbstractClientPlayer) (Object) this).getCachedUniqueIdString();
			String uidHash = HashUtils.getSHA(uuid.replace("-", "")).toLowerCase();
            String id = SettingsMap.hasValue(SettingsMap.MapKeys.CAPES_TEXTURE, ((AbstractClientPlayer) (Object) this).getGameProfile().getName())
		            ? ((AbstractClientPlayer) (Object) this).getGameProfile().getName() : SettingsMap.hasValue(SettingsMap.MapKeys.CAPES_TEXTURE, uuid.replace("-", ""))
                            ? uuid.replace("-", "") : SettingsMap.hasValue(SettingsMap.MapKeys.CAPES_TEXTURE, uidHash) ? uidHash : null;
            if (id != null) {
            	if (capeLoaded) {
            		ci.setReturnValue(capeIdentifier);
	            } else {
		            capeIdentifier = new ResourceLocation(String.format("capes/%s.png", uidHash));
					ThreadDownloadImageData playerSkinTexture = new ThreadDownloadImageData(new File(String.format("%s/libraries/EMC/capes/%s.png", OSUtils.getMCDir(), uidHash)),
				            (String) SettingsMap.getValue(SettingsMap.MapKeys.CAPES_TEXTURE, id, ""), DefaultPlayerSkin.getDefaultSkinLegacy(), new ImageBufferDownload());
		            capeLoaded = true;
		            Minecraft.getMinecraft().getTextureManager().loadTexture(capeIdentifier, playerSkinTexture);
	            }
            }
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public NetworkPlayerInfo getPlayerNetworkInfo() {
		return playerInfo;
	}

}
