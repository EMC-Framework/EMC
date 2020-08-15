package me.deftware.client.framework.apis.oauth;

import me.deftware.mixin.imp.IMixinNetHandlerLoginClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.server.S02PacketLoginSuccess;
import net.minecraft.util.IChatComponent;

public class OAuthNetHandler extends NetHandlerLoginClient {

	public OAuth.OAuthCallback callback;

	public OAuthNetHandler(NetworkManager networkManagerIn, Minecraft mcIn, GuiScreen previousScreenIn,
						   OAuth.OAuthCallback callback) {
		super(networkManagerIn, mcIn, previousScreenIn);
		this.callback = callback;
	}

	@Override
	public void onDisconnect(IChatComponent reason) {
		callback.callback(false, "", "");
	}

	@Override
	public void handleLoginSuccess(S02PacketLoginSuccess packetIn) {
		((IMixinNetHandlerLoginClient) this).setGameProfile(packetIn.getProfile());
		((IMixinNetHandlerLoginClient) this).getNetworkManager().setConnectionState(EnumConnectionState.PLAY);
		((IMixinNetHandlerLoginClient) this).getNetworkManager().setNetHandler(new OAuthNetHandlerPlayClient(Minecraft.getMinecraft(), null,
				((IMixinNetHandlerLoginClient) this).getNetworkManager(), ((IMixinNetHandlerLoginClient) this).getGameProfile(), callback));
	}

}