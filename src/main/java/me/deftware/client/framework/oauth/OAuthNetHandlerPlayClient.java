package me.deftware.client.framework.oauth;

import com.mojang.authlib.GameProfile;
import me.deftware.client.framework.chat.ChatMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.packet.DisconnectS2CPacket;
import net.minecraft.network.ClientConnection;

/**
 * @author Deftware
 */
public class OAuthNetHandlerPlayClient extends ClientPlayNetworkHandler {

    private final OAuth.OAuthCallback callback;

    public OAuthNetHandlerPlayClient(Minecraft mcIn, Screen screenIn, ClientConnection networkManagerIn,
                                     GameProfile profileIn, OAuth.OAuthCallback callback) {
        super(mcIn, screenIn, networkManagerIn, profileIn);
        this.callback = callback;
    }

    @Override
    public void onDisconnect(DisconnectS2CPacket packetIn) {
        String[] data = new ChatMessage().fromText(packetIn.getReason()).toString(false).split("\n");
        String code = data[0].split("\"")[1].replace("\"", "");
        String time = data[2].substring("Your code will expire in ".length() + 1);
        callback.callback(true, code, time);
        getClientConnection().disconnect(packetIn.getReason());
    }

}
