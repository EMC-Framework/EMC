package me.deftware.mixin.mixins.network;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import me.deftware.mixin.imp.IMixinNetworkPlayerInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(NetworkPlayerInfo.class)
public class MixinNetworkPlayerInfo implements IMixinNetworkPlayerInfo {

    @Shadow
    private ResourceLocation locationSkin;

    @Shadow
    private ResourceLocation locationCape;

    @Final
    @Shadow
    private GameProfile gameProfile;

    @Shadow
    private String skinType;

    @Override
    public void reloadTextures() {
        locationSkin = new ResourceLocation("");
        locationCape = new ResourceLocation("");
        Minecraft.getMinecraft().getSkinManager().loadProfileTextures(this.gameProfile, (p_210250_1_, p_210250_2_, p_210250_3_) -> {
            switch (p_210250_1_) {
                case SKIN:
                    locationSkin = p_210250_2_;
                    this.skinType = p_210250_3_.getMetadata("model");
                    if (this.skinType == null) {
                        this.skinType = "default";
                    }
                    break;
                case CAPE:
                    locationCape = p_210250_2_;
                    break;
            }

        }, true);
    }

}
