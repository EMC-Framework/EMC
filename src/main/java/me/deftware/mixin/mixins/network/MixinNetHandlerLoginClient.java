package me.deftware.mixin.mixins.network;

import com.mojang.authlib.GameProfile;
import me.deftware.mixin.imp.IMixinNetHandlerLoginClient;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.network.NetworkManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NetHandlerLoginClient.class)
public class MixinNetHandlerLoginClient implements IMixinNetHandlerLoginClient {

    @Final
    @Shadow
    protected NetworkManager networkManager;

    @Shadow
    private GameProfile gameProfile;

    @Override
    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    @Override
    public GameProfile getGameProfile() {
        return gameProfile;
    }

    @Override
    public void setGameProfile(GameProfile profile) {
        gameProfile = profile;
    }

}
