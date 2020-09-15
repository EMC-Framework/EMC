package me.deftware.client.framework.oauth;

import com.mojang.authlib.GameProfile;
import me.deftware.client.framework.chat.ChatMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S40PacketDisconnect;

public class OAuthNetHandlerPlayClient extends NetHandlerPlayClient {

    private final OAuth.OAuthCallback callback;

	public OAuthNetHandlerPlayClient(Minecraft mcIn, GuiScreen p_i46300_2_, NetworkManager networkManagerIn,
									 GameProfile profileIn, OAuth.OAuthCallback callback) {
		super(mcIn, p_i46300_2_, networkManagerIn, profileIn);
		this.callback = callback;
	}

	@Override
	public void handleDisconnect(S40PacketDisconnect packetIn) {
        String[] data = new ChatMessage().fromText(packetIn.getReason(), false).toString(false).split("\n");
        String code = data[0].split("\"")[1].replace("\"", "");
        String time = data[2].substring("Your code will expire in ".length() + 1);
        callback.callback(true, code, time);
        getNetworkManager().closeChannel(packetIn.getReason());
    }

}