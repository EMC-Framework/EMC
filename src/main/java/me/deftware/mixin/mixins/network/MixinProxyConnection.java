package me.deftware.mixin.mixins.network;

import io.netty.channel.Channel;
import me.deftware.client.framework.network.PacketRegistry;
import me.deftware.client.framework.network.SocksProxy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/network/NetworkManager$2")
public class MixinProxyConnection {

	@Inject(method = "initChannel(Lio/netty/channel/Channel;)V", remap = false, at = @At("HEAD"))
	public void connect(Channel channel, CallbackInfo cir) {
		SocksProxy proxy = PacketRegistry.INSTANCE.getProxy();
		if (proxy != null)
			channel.pipeline().addFirst(proxy.getProxyHandler());
	}

}
