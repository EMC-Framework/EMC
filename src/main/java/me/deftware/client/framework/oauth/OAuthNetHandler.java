package me.deftware.client.framework.oauth;

import me.deftware.mixin.imp.IMixinNetHandlerLoginClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.network.ClientLoginNetworkHandler;
import net.minecraft.client.network.packet.LoginSuccessS2CPacket;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkState;
import net.minecraft.network.chat.Component;

/**
 * @author Deftware
 */
public class OAuthNetHandler extends ClientLoginNetworkHandler {

    public OAuth.OAuthCallback callback;

    public OAuthNetHandler(ClientConnection networkManagerIn, Minecraft mcIn, Screen previousScreenIn,
                           OAuth.OAuthCallback callback) {
        super(networkManagerIn, mcIn, previousScreenIn, fakeConsumer -> {
        });
        this.callback = callback;
    }

    @Override
    public void onDisconnected(Component reason) {
        callback.callback(false, "", "");
    }

    @Override
    public void onLoginSuccess(LoginSuccessS2CPacket packetIn) {
        ((IMixinNetHandlerLoginClient) this).setGameProfile(packetIn.getProfile());
        ((IMixinNetHandlerLoginClient) this).getNetworkManager().setState(NetworkState.PLAY);
        ((IMixinNetHandlerLoginClient) this).getNetworkManager().setPacketListener(new OAuthNetHandlerPlayClient(net.minecraft.client.Minecraft.getInstance(), null,
                ((IMixinNetHandlerLoginClient) this).getNetworkManager(), ((IMixinNetHandlerLoginClient) this).getGameProfile(), callback));
    }

}
