package me.deftware.client.framework.oauth;

import net.minecraft.client.MinecraftClient;
import net.minecraft.network.NetworkState;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;

import java.net.InetAddress;
import java.util.Optional;

/**
 * API to authenticate with <a href="https://auth.aristois.net/">Minecraft oAuth</a>
 *
 * @author Deftware
 */
public class OAuth {

    private static final String ip = "auth.aristois.net";
    private static final int port = 25565;

    public static void oAuth(OAuthCallback callback) {
        new Thread(() -> {
            Thread.currentThread().setName("OAuth thread");
            try {
                MinecraftClient client = MinecraftClient.getInstance();
                InetAddress inetaddress = InetAddress.getByName(OAuth.ip);
                OAuthNetworkManager manager = OAuthNetworkManager.connect(inetaddress, OAuth.port,
                        client.options.useNativeTransport, callback);
                manager.setPacketListener(new OAuthNetHandler(manager, client, null, callback));
                manager.send(new HandshakeC2SPacket(OAuth.ip, OAuth.port, NetworkState.LOGIN));
                manager.send(new LoginHelloC2SPacket(client.getSession().getUsername(), client.getProfileKeys().method_45104().join(), Optional.ofNullable(
                        client.getSession().getProfile().getId()
                )));
            } catch (Exception ex) {
                callback.callback(false, "", "");
            }
        }).start();
    }

    @FunctionalInterface
    public interface OAuthCallback {

        /**
         * Called after oAuth attempt
         *
         * @param success If the user successfully authenticated
         * @param code    The users oAuth code
         * @param time    How long before the oAuth code expires
         */
        void callback(boolean success, String code, String time);

    }

}
