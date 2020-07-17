package me.deftware.client.framework.apis.oauth;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.DisconnectS2CPacket;

public class OAuthNetHandlerPlayClient extends ClientPlayNetworkHandler {

    private OAuth.OAuthCallback callback;

    public OAuthNetHandlerPlayClient(MinecraftClient mcIn, Screen p_i46300_2_, ClientConnection networkManagerIn,
                                     GameProfile profileIn, OAuth.OAuthCallback callback) {
        super(mcIn, p_i46300_2_, networkManagerIn, profileIn);
        this.callback = callback;
    }

    @Override
    public void onDisconnect(DisconnectS2CPacket packetIn) {
        String code = packetIn.getReason().getString().split("\n")[0].split("\"")[1].replace("\"", "");
        String time = packetIn.getReason().getString().split("\n")[2]
                .substring("Your code will expire in ".length() + 1);
        callback.callback(true, code, time);
        getConnection().disconnect(packetIn.getReason());
    }

}
